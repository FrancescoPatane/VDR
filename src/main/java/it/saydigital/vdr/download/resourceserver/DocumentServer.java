package it.saydigital.vdr.download.resourceserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.text.DocumentException;

import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.ContentLink;
import it.saydigital.vdr.util.EnvHandler;
import it.saydigital.vdr.watermark.Watermarker;


public class DocumentServer implements ResourceServer{

	private final String mimeType = "application/pdf";

	@Override
	public byte[] serveResource(Content content, String text) throws DocumentException, IOException {
		ContentLink contentLink = content.getContent();
		String resourcePath = contentLink.getPath();
		if (resourcePath == null)
			resourcePath = EnvHandler.getProperty("app.content_folder")+File.separator+"documents"+File.separator+contentLink.getFilename();
		String wateredFilePath = Watermarker.applyWatermarkToPdf(resourcePath, text);
		Path path = Paths.get(wateredFilePath);
		byte[] bytes = null;
		bytes = Files.readAllBytes(path);
		path.toFile().delete();
		return bytes;
	}

	@Override
	public String getMimetype() {
		return this.mimeType;
	}






}
