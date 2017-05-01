package ch.bfh.project1.kanu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Die Klasse "GenerierePDF" generiert aus einer Liste eine PDF-Datei. Dies geschieht mit der Hilfe der Bibliothek iText.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 01.05.2017
 * @version 1.0
 *
 */
public class GenerierePDF {
	
	//TODO: Nur zum testen.
	public static void main (String args[]) throws IOException,
	DocumentException {
		String dest = "C:/Daten/Patrik/itext.pdf";
		List<List<String>> rangliste = new ArrayList<>();
		List<String> titel = new ArrayList<>();
		List<String> fahrer = new ArrayList<>();
		List<String> fahrer1 = new ArrayList<>();
		titel.add("Vorname");
		titel.add("Nachname");
		titel.add("Bootsklass");
		titel.add("Schüsch no Sache");
		titel.add("Da geit no meh");
		titel.add("No meh");
		fahrer.add("Hans Hans Hans Hans");
		fahrer.add("Muster");
		fahrer.add("Jet-Ski");
		fahrer.add("böö");
		fahrer.add("böö");
		fahrer.add("böö");
		rangliste.add(fahrer);
		fahrer1.add("Fritz");
		fahrer1.add("Bünzli");
		fahrer1.add("Kanu");
		fahrer1.add("böö");
		fahrer1.add("böö");
		fahrer1.add("böö");
		rangliste.add(fahrer1);
		new GenerierePDF().generierePdf(dest, titel, rangliste);
	}
	
	public void generierePdf(String pfad, List<String> titel, List<List<String>> daten) throws IOException, DocumentException {
        // Neues File generieren
		File file = new File(pfad);
        file.getParentFile().mkdirs();
		Document dokument = new Document();
        PdfWriter.getInstance(dokument, new FileOutputStream(pfad));
        
        // Daten bereitstellen
        int anzSpalten = titel.size();
        int anzZeilen = daten.size();
        
        // Dokument erstellen
        dokument.open();
        PdfPTable tabelle = new PdfPTable(anzSpalten);
        PdfPCell zelle;
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        for (int k = 0; k < anzSpalten; k ++){
        	zelle = new PdfPCell(new Phrase(titel.get(k), font));
        	zelle.setBackgroundColor(BaseColor.BLUE);
        	zelle.setBorder(Rectangle.NO_BORDER);
        	zelle.setVerticalAlignment(Element.ALIGN_MIDDLE);
        	zelle.setHorizontalAlignment(Element.ALIGN_CENTER);
        	tabelle.addCell(zelle);
        }
        for (int i = 0; i < anzZeilen; i++){
        	for (int j = 0; j < anzSpalten; j++){
        		tabelle.addCell(daten.get(i).get(j));
        	}
        }
        dokument.add(tabelle);
        dokument.close();
    }
}
