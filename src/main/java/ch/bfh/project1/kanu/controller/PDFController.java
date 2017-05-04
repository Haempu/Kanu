package ch.bfh.project1.kanu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Die Klasse "GenerierePDF" generiert aus einer Liste eine PDF-Datei. Dies
 * geschieht mit der Hilfe der Bibliothek iText.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 01.05.2017
 * @version 1.0
 *
 */
public class PDFController {
	private static String TITEL = "Kanu-Klub Grenchen";
	private static Font FONT_DOKUMENTTITEL = FontFactory.getFont(FontFactory.HELVETICA, 25, Font.BOLDITALIC, BaseColor.BLACK);
	private static Font FONT_TABELLENTITEL = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLDITALIC, BaseColor.BLACK);
	private static Font FONT_TABELLENHEADER = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC, BaseColor.WHITE);
	private static float SPACING_VOR_TABELLENTITEL = 10;
	private static float SPACING_VOR_TABELLE = 10;
	
	/*
	// TODO: Nur zum testen.
	public static void main(String args[]) throws IOException, DocumentException {
		String dest = "C:/Daten/Patrik/test.pdf";
		List<String> titel = new ArrayList();;
		titel.add("Titel1");
		titel.add("Titel2");
		List<List<String>> rangliste = new ArrayList<>();
		List<List<List<String>>> daten = new ArrayList<>();
		String[] HEADER = {"Rang", "Startnr", "Name", "Club", "1. Lauf", "2. Lauf"};
		List<String> fahrer = new ArrayList<>();
		List<String> fahrer1 = new ArrayList<>();
		fahrer.add("Hans");
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
		daten.add(rangliste);
		daten.add(rangliste);
		new PDFController().generierePDF(dest, titel, HEADER, daten);
	}*/

	/**
	 * Generiert eine oder mehrere Tabellen in einem PDF-Dokument.
	 * @param pfad - Speicherort des Dokuments
	 * @param tabellentitel - Liste von Strings mit den jeweiligen Tabellentiteln. ACHTUNG: Muss gleich viele Einträge haben wie @param daten!
	 * @param header - Enthält den Header der Tabellen. Ist immer bei allen Tabellen der gleiche. ACHTUNG: Muss so viele Einträge haben wie eine Tabelle Spalten hat!
	 * @param daten - Enthält die Daten der Tabellen in drei Ebenen.
	 * 					Ebene 1 ("Daten"):		Liste von Tabellen. Jeder Eintrag ist eine ganze Tabelle. Muss gleich viele Einträge haben wie @param tabellentitel.
	 * 					Ebene 2 ("Tabelle"): 	Liste von einzelnen Tabelleneinträgen. Jeder Eintrag ist eine Tabellenzeile.
	 * 					Ebene 3 ("Zeile"):		Liste von Zelleneinträgen. Jeder Eintrag ist ein String, der Schlussendlich in einer einzelnen Zelle steht.
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void generierePDF(String pfad, List<String> tabellentitel, String[] header, List<List<List<String>>> daten)
			throws IOException, DocumentException {
		// Neues File generieren
		File file = new File(pfad);
		file.getParentFile().mkdirs();
		Document dokument = new Document();
		PdfWriter.getInstance(dokument, new FileOutputStream(pfad));

		// Daten bereitstellen
		int anzTabellen = daten.size();
		int anzSpalten = header.length;
		int anzZeilen = daten.get(0).size();
		

		// Dokument erstellen
		dokument.open();
		// Dokumenttitel
		Paragraph paragDokumenttitel = new Paragraph(TITEL, FONT_DOKUMENTTITEL);
		dokument.add(paragDokumenttitel);
		// Tabellen erstellen
		// Ebene "Daten"
		for (int i = 0; i < anzTabellen; i++) {
			// Ebene "Tabelle"
			PdfPTable tabelle = new PdfPTable(anzSpalten);
			for (int j = 0; j < anzSpalten; j++) {
				// Ebene "Zeile"
				PdfPCell zelle = new PdfPCell(new Phrase(header[j], FONT_TABELLENHEADER));
				zelle.setBackgroundColor(BaseColor.BLUE);
				zelle.setBorder(Rectangle.NO_BORDER);
				zelle.setVerticalAlignment(Element.ALIGN_MIDDLE);
				zelle.setHorizontalAlignment(Element.ALIGN_CENTER);
				tabelle.addCell(zelle);
			}
			for (int k = 0; k < anzZeilen; k++) {
				// Ebene "Tabelle"
				for (int l = 0; l < anzSpalten; l++) {
					// Ebene "Zeile"
					tabelle.addCell(daten.get(i).get(k).get(l));
				}
			}
			// Tabellentitel und Tabelle an Dokument übergeben
			Paragraph paragTabellentitel = new Paragraph(tabellentitel.get(i), FONT_TABELLENTITEL);
			paragTabellentitel.setSpacingBefore(SPACING_VOR_TABELLENTITEL);
			dokument.add(paragTabellentitel);
			tabelle.setSpacingBefore(SPACING_VOR_TABELLE);
			dokument.add(tabelle);
		}
		dokument.close();
	}
}
