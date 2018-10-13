package it.saydigital.vdr.async.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.text.DocumentException;

import it.saydigital.vdr.download.resourceserver.FolderServer;
import it.saydigital.vdr.download.resourceserver.ResourceServer;
import it.saydigital.vdr.download.resourceserver.ResourceServerFactory;
import it.saydigital.vdr.mail.MailService;
import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.ContentRepository;
import it.saydigital.vdr.util.EnvHandler;

public class FullDownloadTask extends BackgroundTask {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ContentRepository contentRepository;

	@Autowired
	private ResourceServerFactory serverFactory;

	@Autowired
	private MailService mailService;

	private MarketingEntity mktEntity;

	public FullDownloadTask(String id, User requestor, MarketingEntity mktEntity) {
		super(id, "Full Download" , requestor);
		this.mktEntity = mktEntity;
	}


	public MarketingEntity getMktEntity() {
		return mktEntity;
	}

	public void setMktEntity(MarketingEntity mktEntity) {
		this.mktEntity = mktEntity;
	}





	@Override
	public void executeTask(Map<String, Object> params)  {


		log.info("Starting full download background task for user " + this.getRequestor().getEmail() + "and entity " + this.mktEntity.getName());

		try {

			String zipFileName = this.mktEntity.getName().replaceAll(" ", "_")+"_"+this.getRequestor().getId()+"_"+System.currentTimeMillis();

			String folderToZip = "temp"+File.separator+zipFileName+File.separator;
			File fileToZip = new File(folderToZip);
			fileToZip.mkdir();

			List<Content> roots = contentRepository.findRootsByEntityId(this.mktEntity.getId());
			this.setSteps(roots.size()+2); 

			this.createStructure(folderToZip, this.getRequestor().getEmail(), roots);
			String externalDocumentsPath = EnvHandler.getProperty("app.external_contents_folder");

			File zipFile = new File(externalDocumentsPath+File.separator+zipFileName+".zip");
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);

			FolderServer folderServer = new FolderServer();
			folderServer.addDirToArchive(zos, fileToZip, zipFileName+File.separator);
			zos.close();
			this.setDone(this.getDone() + 1);

			String baseUrl = (String) params.get("baseUrl");

			try {
				mailService.sendMailFullDonwload(baseUrl+"extDocs/"+zipFileName, this.getRequestor(), this.mktEntity.getTmEmail());
				this.setDone(this.getDone() + 1);
				this.setStatus(TaskStatus.COMPLETED);
			} catch (MessagingException e) {
				log.error("Failed to send mail during full download background task for user " + this.getRequestor().getEmail() + "and entity " + this.mktEntity.getName());
				e.printStackTrace();
				this.setStatus(TaskStatus.ERROR);
			}

		} catch (Exception e) {
			log.error("Error exectuing Full download Background task for user " + this.getRequestor().getEmail() + "and entity " + this.mktEntity.getName());
			e.printStackTrace();
			this.setStatus(TaskStatus.ERROR);
		}

	}


	private void createStructure(String rootPath, String watermark, List<Content> elements) throws IOException, DocumentException {


		for (Content content : elements) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (content.getType().toString().equalsIgnoreCase("FOLDER")) {
				String newPath = rootPath+File.separator+content.getName();
				File file = new File(newPath);
				file.mkdir();
				List<Content> childs = contentRepository.findByFather(content.getId());
				createStructure(newPath, watermark, childs);
			}else {
				String newPath = rootPath+File.separator+content.getContent().getFilename();
				ResourceServer server = serverFactory.createResourceServer(content);
				byte[] bytes = server.serveResource(content, watermark);
				FileUtils.writeByteArrayToFile(new File(newPath), bytes);

			}
			if (content.getFather()==null) {
				this.setDone(this.getDone() + 1);
			}

		}

	}


}
