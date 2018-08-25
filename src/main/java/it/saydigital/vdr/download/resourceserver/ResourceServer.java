package it.saydigital.vdr.download.resourceserver;

import it.saydigital.vdr.model.Content;

public interface ResourceServer {
	
	public byte[] serveResource(Content content);
	
	public String getMimetype();



}
