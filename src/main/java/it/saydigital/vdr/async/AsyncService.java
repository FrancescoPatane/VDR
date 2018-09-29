package it.saydigital.vdr.async;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.DocumentException;

import it.saydigital.vdr.async.task.BackgroundTask;
import it.saydigital.vdr.async.task.TaskStatus;
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
	
	private List<BackgroundTask> runningTasks = new ArrayList<>();
	
	@Async
	@Transactional //to allow hibernate session in new thread
	public void fullDowload(MarketingEntity entity, User user, String baseUrl) throws IOException, DocumentException {
		
		
		
		String zipFileName = entity.getName().replaceAll(" ", "_")+"_"+user.getId()+"_"+System.currentTimeMillis();
		
		BackgroundTask task = new BackgroundTask(zipFileName, user, entity, TaskStatus.RUNNING);
		this.runningTasks.add(task);
		
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
		
		try {
			mailService.sendMailFullDonwload(baseUrl+"extDocs/"+zipFileName, user, entity.getTmEmail());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createRoots(String rootPath, String watermark, List<Content> roots) throws IOException, DocumentException {
		
	/*	int i = 0;
		
		while (i<15) {
			i++;
			System.out.println(i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
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

	public List<BackgroundTask> getRunningTasks() {
		return runningTasks;
	}
	
	public boolean hasRunningTasks(long userId, long entityId) {
		for (BackgroundTask task : this.getRunningTasks()) {
			if (task.getUser().getId() == userId && task.getMktEntity().getId() == entityId && task.getStatus().equals(TaskStatus.RUNNING));
			return true;
		}
		return false;
	}

	
	

}
