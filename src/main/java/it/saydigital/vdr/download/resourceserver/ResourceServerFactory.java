package it.saydigital.vdr.download.resourceserver;

import org.springframework.stereotype.Component;

import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.ContentLink;

@Component
public class ResourceServerFactory {
	
	
	public ResourceServer createResourceServer(Content content) {
		ResourceServer resourceServer = null;
		ContentLink contentLink = content.getContent();
		
		switch (contentLink.getType().toString()) {
		case "DOCUMENT":  
			resourceServer = new DocumentServer();
			break;
		case "IMAGE":  
			resourceServer = new ImageServer();
			break;
		case "ARCHIVE":  
//			iconName = "far fa-file-archive";
			break;
		}
		
		return resourceServer;
	}

}
