package it.saydigital.vdr.download.resourceserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.DocumentException;

import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.ContentLink;
import it.saydigital.vdr.util.EnvHandler;
import it.saydigital.vdr.watermark.Watermarker;

public class ImageServer implements ResourceServer{

	private String extension;

	@Override
	public byte[] serveResource(Content content, String text) throws DocumentException, IOException {
		ContentLink contentLink = content.getContent();
		String resourcePath = contentLink.getPath();
		if (resourcePath == null || resourcePath.length()<=0)
			resourcePath = EnvHandler.getProperty("app.content_folder")+File.separator+"images"+File.separator+contentLink.getFilename();
		this.extension = FilenameUtils.getExtension(resourcePath);
		String wateredFilePath = Watermarker.applyWatermarkToImage(resourcePath, text);
		Path path = Paths.get(wateredFilePath);
		byte[] bytes = null;
		bytes = Files.readAllBytes(path);
		path.toFile().delete();
		return bytes;
	}

	@Override
	public String getMimetype() {
		String mimeType = "";
		switch (this.extension) {
		case "jpeg":
		case "jpg":  
			mimeType = "image/jpeg";
			break;
		case "png":  
			mimeType = "image/png";
		case "gif":  
			mimeType = "image/gif";
			break;
		}
		return mimeType;
	}



}
