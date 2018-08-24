package it.saydigital.vdr.download;

import java.io.File;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.saydigital.vdr.configuration.AppConfig;
import it.saydigital.vdr.download.resourceserver.ResourceServer;
import it.saydigital.vdr.download.resourceserver.ResourceServerFactory;
import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.ContentLink;
import it.saydigital.vdr.repository.ContentRepository;

@Component
public class DownloadManager {
	
	@Autowired
	private AppConfig appConfig;
	
    @Autowired
    private ContentRepository contentRepository;
	
	public byte[] serveResource(long contentId) {
		String path;
		String type;
		Optional<Content> optionalContent = contentRepository.findById(contentId);
		if (optionalContent.isPresent()) {
			Content content = optionalContent.get();
			ContentLink contentLink = content.getContent();
			if (contentLink.getIsExternal()) {
				path = contentLink.getUri(); 
			}else {
				path = appConfig.getContentFolder()+File.separator+content.getMktEntityId()+File.separator+contentLink.getFilename();
			}
			type = contentLink.getType().toString();
			ResourceServer server = ResourceServerFactory.createResourceServer(type); 
			return server.serveResource(path);
		}else{
			return null;
		}
		
	}
    

}
