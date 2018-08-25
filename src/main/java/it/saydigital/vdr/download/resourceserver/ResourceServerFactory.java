package it.saydigital.vdr.download.resourceserver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.ContentLink;
import it.saydigital.vdr.repository.ContentRepository;
import it.saydigital.vdr.util.EnvHandler;

@Component
public class ResourceServerFactory {
	
    @Autowired
    private ContentRepository contentRepository;
	
	public ResourceServer createResourceServer(Content content) {
		ResourceServer resourceServer = null;
		String path;
		ContentLink contentLink = content.getContent();
		
		switch (contentLink.getType().toString()) {
		case "DOCUMENT":  
			resourceServer = new DocumentServer();
			break;
		case "IMAGE":  
//			iconName = "far fa-file-image";
			break;
		case "ARCHIVE":  
//			iconName = "far fa-file-archive";
			break;
		}
		
		return resourceServer;
	}

}
