package it.saydigital.vdr.watermark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;


public class WatermarkPdf {
	
	private static final DateTimeFormatter formatter =  DateTimeFormatter.ISO_LOCAL_DATE;
	
	public static String applyWatermark(String filePath, String text) throws DocumentException, IOException {
		LocalDateTime now = LocalDateTime.now();
		
		String pathOfWateredFile = "temp"+File.separator+System.currentTimeMillis()+"_text_watermarked.pdf";
		
		PdfReader reader = new PdfReader(filePath);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pathOfWateredFile));
        // text watermark
        Font FONT = new Font(Font.FontFamily.TIMES_ROMAN, 34, Font.BOLD, new GrayColor(0.5f));
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
	

}
