package it.saydigital.vdr.download.resourceserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class DocumentServer implements ResourceServer{
	
	public static final String mimeType = "application/pdf";

	@Override
	public byte[] serveResource(String resourcePath) {
		Path path = Paths.get(resourcePath);
	    byte[] content = null;
	    try {
			content = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return content;
	}

	@Override
	public String getMimetype() {
		return mimeType;
	}
	

 

}
