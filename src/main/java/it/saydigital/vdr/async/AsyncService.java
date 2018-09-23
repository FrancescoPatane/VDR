package it.saydigital.vdr.async;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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

@Service
public class AsyncService {
	
	@Autowired
	private ContentRepository contentRepository;
	
	@Autowired
	private ResourceServerFactory serverFactory;
	
	@Autowired
	private MailService mailService;
	
	@Async
	public void fullDowload(MarketingEntity entity, User user, String baseUrl) throws IOException, DocumentException {
		
		String zipFileName = entity.getName()+"_"+user.getId()+"_"+System.currentTimeMillis();
		String folderToZip = "temp"+File.separator+zipFileName+File.separator;
		File fileToZip = new File(folderToZip);
		fileToZip.mkdir();
		List<Content> roots = contentRepository.findRootsByEntityId(entity.getId());
		this.createRoots(folderToZip, user.getEmail(), roots);
		String externalDocumentsPath = EnvHandler.getProperty("app.external_contents_folder");

		File zipFile = new File(externalDocumentsPath+File.separator+zipFileName+".zip");
		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		FolderServer folderServer = new FolderServer();
		folderServer.addDirToArchive(zos, fileToZip, "");
		zos.close();
		
		mailService.sendMailFullDonwload(baseUrl+"extDocs/"+zipFileName, user);
	}
	
	private void createRoots(String rootPath, String watermark, List<Content> roots) throws IOException, DocumentException {
		
		for (Content content : roots) {
			
			if (content.getType().toString().equalsIgnoreCase("FOLDER")) {
				String newPath = rootPath+File.separator+content.getName();
				File file = new File(newPath);
				file.mkdir();
				List<Content> childs = contentRepository.findByFather(content.getId());
				createRoots(newPath, watermark, childs);
			}else {
				String newPath = rootPath+File.separator+content.getContent().getFilename();
				ResourceServer server = serverFactory.createResourceServer(content);
				byte[] bytes = server.serveResource(content, watermark);
				FileUtils.writeByteArrayToFile(new File(newPath), bytes);

			}
		}
		
	}

}
