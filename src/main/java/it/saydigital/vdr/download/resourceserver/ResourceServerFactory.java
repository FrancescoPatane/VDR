package it.saydigital.vdr.download.resourceserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.ContentLink;
import it.saydigital.vdr.repository.ContentRepository;

@Component
public class ResourceServerFactory {

	@Autowired
	private ContentRepository contentRepository;

	public ResourceServer createResourceServer(Content content) {
		ResourceServer resourceServer = null;
		if (content.getType().toString().equals("FOLDER")) {
			resourceServer = new FolderServer(contentRepository);
		}else{
			ContentLink contentLink = content.getContent();

			switch (contentLink.getType().toString()) {
			case "DOCUMENT":  
				resourceServer = new DocumentServer();
				break;
			case "IMAGE":  
				resourceServer = new ImageServer();
				break;
			case "ARCHIVE":  
				//iconName = "far fa-file-archive";
				break;
			}
		}
		return resourceServer;
	}
}
