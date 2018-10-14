package it.saydigital.vdr.download.resourceserver;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import it.saydigital.vdr.model.Content;

public interface ResourceServer {
	
	public abstract byte[] serveResource(Content content, String watermark) throws IOException, DocumentException;
	
	public abstract String getMimetype();



}
