package it.saydigital.vdr.async;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import it.saydigital.vdr.download.resourceserver.ResourceServer;
import it.saydigital.vdr.download.resourceserver.ResourceServerFactory;
import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.ContentRepository;

@Service
public class AsyncService {
	
	@Autowired
	private ContentRepository contentRepository;
	
	@Autowired
	private ResourceServerFactory serverFactory;
	
	@Async
	public void fullDowload(MarketingEntity entity, User user) throws IOException, DocumentException {
		StringBuilder startFolderPath = new StringBuilder();
		startFolderPath.append("temp"+File.separator+entity.getName()+"_"+user.getId()+"_"+System.currentTimeMillis()+File.separator);
		String folderToZip = startFolderPath.toString();
		File file = new File(startFolderPath.toString());
		file.mkdir();
		List<Content> roots = contentRepository.findRootsByEntityId(entity.getId());
		this.createRoots(folderToZip, user.getEmail(), roots);
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
