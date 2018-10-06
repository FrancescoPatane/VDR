package it.saydigital.vdr.async;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ContentRepository contentRepository;
	
	@Autowired
	private ResourceServerFactory serverFactory;
	
	@Autowired
	private MailService mailService;
	
	private List<BackgroundTask> tasks = new ArrayList<>();
	
	@Async
	@Transactional //to allow hibernate session in new thread
	public void fullDowload(MarketingEntity entity, User user, String baseUrl) throws IOException, DocumentException {
		
		log.info("Starting full download background task for user " + user.getEmail() + "and entity " + entity.getName());
		
		String zipFileName = entity.getName().replaceAll(" ", "_")+"_"+user.getId()+"_"+System.currentTimeMillis();
		
		BackgroundTask task = new BackgroundTask(zipFileName, user, entity, TaskStatus.RUNNING);
		this.tasks.add(task);
		
		String folderToZip = "temp"+File.separator+zipFileName+File.separator;
		File fileToZip = new File(folderToZip);
		fileToZip.mkdir();
		
		List<Content> roots = contentRepository.findRootsByEntityId(entity.getId());
		int steps = roots.size()+2;
		Integer done = 0; //to pass as reference instead of value
		
		this.createStructure(folderToZip, user.getEmail(), roots, done, steps, task);
		String externalDocumentsPath = EnvHandler.getProperty("app.external_contents_folder");

		File zipFile = new File(externalDocumentsPath+File.separator+zipFileName+".zip");
		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);

		FolderServer folderServer = new FolderServer();
		folderServer.addDirToArchive(zos, fileToZip, zipFileName+File.separator);
		zos.close();
		done++;
		task.setCompletePct(done, steps);
		
		try {
			mailService.sendMailFullDonwload(baseUrl+"extDocs/"+zipFileName, user, entity.getTmEmail());
			done++;
			task.setCompletePct(done, steps);
			task.setStatus(TaskStatus.COMPLETED);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			log.error("Failed to send mail during full download background task for user " + user.getEmail() + "and entity " + entity.getName());
			e.printStackTrace();
			task.setStatus(TaskStatus.ERROR);
		}
	}
	
	
	
	private void createStructure(String rootPath, String watermark, List<Content> elements, Integer done, int steps, BackgroundTask task) throws IOException, DocumentException {
		
		
		for (Content content : elements) {
			
			if (content.getType().toString().equalsIgnoreCase("FOLDER")) {
				String newPath = rootPath+File.separator+content.getName();
				File file = new File(newPath);
				file.mkdir();
				List<Content> childs = contentRepository.findByFather(content.getId());
				createStructure(newPath, watermark, childs, done, steps, task);
			}else {
				String newPath = rootPath+File.separator+content.getContent().getFilename();
				ResourceServer server = serverFactory.createResourceServer(content);
				byte[] bytes = server.serveResource(content, watermark);
				FileUtils.writeByteArrayToFile(new File(newPath), bytes);

			}
			if (content.getFather()==null) {
				done++;
				task.setCompletePct(done, steps);
			}
				
		}
		
	}

	public List<BackgroundTask> getTasks() {
		return tasks;
	}
	
	public boolean hasRunningTasks(long userId, long entityId) {
		for (BackgroundTask task : this.getTasks()) {
			System.out.println(task.toString());
			if (task.getUser().getId() == userId && task.getMktEntity().getId() == entityId && task.getStatus().equals(TaskStatus.RUNNING));
			return true;
		}
		return false;
	}
	
	
	public List<BackgroundTask> getTasksForUser(long id) {
		List<BackgroundTask> result = new ArrayList<>();
		for (BackgroundTask task : this.tasks) {
			if (task.getUser().getId()==id)
				result.add(task);
		}
		return result;
	}

	
	

}
