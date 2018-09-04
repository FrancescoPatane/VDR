package it.saydigital.vdr.watermark;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.*;


public class Watermarker {

	private static final DateTimeFormatter formatter =  DateTimeFormatter.ISO_LOCAL_DATE;

	public static String applyWatermarkToPdf(String filePath, String text, String path) throws DocumentException, IOException {
		LocalDateTime now = LocalDateTime.now();

		String pathOfWateredFile = null;
		if (path == null)
			pathOfWateredFile = "temp"+File.separator+System.currentTimeMillis()+"_text_watermarked.pdf";
		else
			pathOfWateredFile = path;

		PdfReader reader = new PdfReader(filePath);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pathOfWateredFile));
		// text watermark
		com.itextpdf.text.Font FONT = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 34, com.itextpdf.text.Font.BOLD, new GrayColor(0.5f));
		Phrase p = new Phrase(text + " " + now.format(formatter), FONT);

		// properties
		PdfContentByte over;
		Rectangle pagesize;
		float x, y;

		// loop over every page
		int n = reader.getNumberOfPages();
		for (int i = 1; i <= n; i++) {

			// get page size and position
			pagesize = reader.getPageSizeWithRotation(i);
			x = (pagesize.getLeft() + pagesize.getRight()) / 2;
			y = (pagesize.getTop() + pagesize.getBottom()) / 2;
			over = stamper.getOverContent(i);
			over.saveState();

			// set transparency
			PdfGState state = new PdfGState();
			state.setFillOpacity(0.2f);
			over.setGState(state);

			// add watermark text 
			ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, -45f);

			over.restoreState();
		}
		stamper.close();
		reader.close();
		return pathOfWateredFile;
	}


	public static String applyWatermarkToImage(String filePath, String text) throws IOException  {
		LocalDateTime now = LocalDateTime.now();

		File file = new File(filePath);

		String extension = FilenameUtils.getExtension(file.getPath());

		String pathOfWateredFile = "temp"+File.separator+System.currentTimeMillis()+"_image_watermarked."+extension;

		ImageIcon photo = new ImageIcon(filePath);

		//Create an image 200 x 200
		BufferedImage bufferedImage = new BufferedImage(photo.getIconWidth(),
				photo.getIconHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

		g2d.drawImage(photo.getImage(), 0, 0, null);

		//Create an alpha composite of 50%
		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		g2d.setComposite(alpha);

		g2d.setColor(Color.white);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2d.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 40));

		FontMetrics fontMetrics = g2d.getFontMetrics();
		Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);

		g2d.drawString(text,
				(photo.getIconWidth() - (int) rect.getWidth()) / 2,
				(photo.getIconHeight() - (int) rect.getHeight()) / 2);

		String time = now.format(formatter);

		rect = fontMetrics.getStringBounds(time, g2d);


		g2d.drawString(now.format(formatter),
				(photo.getIconWidth() - (int) rect.getWidth()) / 2,
				(photo.getIconHeight() - (int) rect.getHeight())*2 / 3);

		//Free graphic resources
		g2d.dispose();


		//Write the image as a jpg
		File outputfile = new File(pathOfWateredFile);
		ImageIO.write(bufferedImage, "jpg", outputfile);
		return pathOfWateredFile;
	}




}
