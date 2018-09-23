package it.saydigital.vdr.download.resourceserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.itextpdf.text.DocumentException;

import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.repository.ContentRepository;
import it.saydigital.vdr.util.EnvHandler;
import it.saydigital.vdr.watermark.Watermarker;

public class FolderServer implements ResourceServer{

	private final String mimeType = "application/zip";
	private ContentRepository contentRepository;
	
	public FolderServer() {
		
	}

	public FolderServer(ContentRepository contentRepository) {
		this.contentRepository = contentRepository;
	}

	@Override
	public byte[] serveResource(Content content, String watermark) throws IOException, DocumentException {
		StringBuilder startFolderPath = new StringBuilder();
		startFolderPath.append("temp"+File.separator+watermark+"_"+System.currentTimeMillis()+File.separator);
		String folderToZip = startFolderPath.toString();
		File file = new File(startFolderPath.toString());
		file.mkdir();
		createFile(content, watermark, startFolderPath );
		File toZip = new File(folderToZip);
		File zipFile = new File("temp"+File.separator+content.getName()+".zip");
		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		this.addDirToArchive(zos, toZip, "");
		zos.close();
		byte[] bytes = Files.readAllBytes(zipFile.toPath());
		return bytes;
	}

	public void createFile(Content content, String watermark, StringBuilder path) throws DocumentException, IOException {
		long folderId = content.getId();
		if (content.getType().toString().equalsIgnoreCase("FOLDER")) {
			path.append(content.getName());
			File file = new File(path.toString());
			file.mkdir();
		}else {
			String resourcePath = content.getContent().getPath();
			if (resourcePath == null || resourcePath.length()<=0)
				resourcePath = EnvHandler.getProperty("app.content_documents_folder")+File.separator+content.getContent().getFilename();
			path.append(content.getContent().getFilename());
			String wateredFilePath = Watermarker.applyWatermarkToPdf(resourcePath, watermark, path.toString());
			new File(wateredFilePath);
		}
		List<Content> childs = contentRepository.findByFather(folderId);
		for (Content child : childs) {
			createFile(child, watermark, path.append(File.separator));
		}
	}
	
	
	//used to zip the file from fulldownload
	public void addDirToArchive(ZipOutputStream zos, File srcFile, String path) {
		File[] files = srcFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// if the file is directory, use recursion
			if (files[i].isDirectory()) {
				addDirToArchive(zos, files[i], path+files[i].getName()+File.separator);
				continue;
			}
			try {
				// create byte buffer
				byte[] buffer = new byte[1024];
				FileInputStream fis = new FileInputStream(files[i]);
				zos.putNextEntry(new ZipEntry(path+files[i].getName()));
				int length;
				while ((length = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}
				zos.closeEntry();
				// close the InputStream
				fis.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

		}
	}





	@Override
	public String getMimetype() {
		return this.mimeType;
	}

}
