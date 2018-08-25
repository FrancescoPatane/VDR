package it.saydigital.vdr.download.resourceserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.ContentLink;
import it.saydigital.vdr.util.EnvHandler;


public class DocumentServer implements ResourceServer{
	
	private final String mimeType = "application/pdf";

	@Override
	public byte[] serveResource(Content content) {
		ContentLink contentLink = content.getContent();
		String resourcePath;
		if (contentLink.getIsExternal()) {
			resourcePath = contentLink.getUri(); 
		}else {
			resourcePath = EnvHandler.getProperty("app.content_folder")+File.separator+content.getMktEntityId()+File.separator+contentLink.getFilename();
		}
		Path path = Paths.get(resourcePath);
	    byte[] bytes = null;
	    try {
	    	bytes = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Couldn't read file");
		}
	    return bytes;
	}

	@Override
	public String getMimetype() {
		return this.mimeType;
	}


	

 

}
