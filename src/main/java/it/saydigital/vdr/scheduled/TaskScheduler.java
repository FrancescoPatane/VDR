package it.saydigital.vdr.scheduled;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import it.saydigital.vdr.util.EnvHandler;

@Service
public class TaskScheduler {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Scheduled(cron="0 0 1 * * ?")
	public void deleteExpiredExternalDocuments() throws IOException {
		log.info("Starting deleteExpiredExternalDocuments");
		int daysBeforeExpiration = Integer.parseInt(EnvHandler.getProperty("app.external_contents_validity"));
		String externalContentsPath = EnvHandler.getProperty("app.external_contents_folder");
		File externalContentsDir = new File(externalContentsPath);
		
		for (File file : externalContentsDir.listFiles()) {
				BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
				LocalDateTime fileCreationDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(attr.creationTime().toMillis()), ZoneId.systemDefault());
				LocalDateTime fileExpirationDate = fileCreationDate.plusDays(daysBeforeExpiration);
				if (LocalDateTime.now().isAfter(fileExpirationDate)) {
					file.delete();
					log.info("Deleted external content file: " + file.getName());
				}
		}
	}

}
