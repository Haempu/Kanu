package ch.bfh.project1.kanu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import ch.bfh.project1.kanu.model.Rennen;

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
	// TODO: Nur zum testen.
	public static void main(String args[]) throws IOException, DocumentException { 
		Rennen rennen = new Rennen();
	  rennen.setTitel("45. Aaremeisterschaft"); 
	  rennen.setName("Kanuslalom");
	  rennen.setVeranstalter("Kanu-Club Grenchen"); 
	  rennen.setDatumVon(new Date(3000)); 
	  rennen.setDatumBis(new Date(7000)); 
	  String dest = "C:/Daten/Patrik/";
	  List<String> tabellenname = new ArrayList();
	  tabellenname.add("C1 Herren allgemein");
	  tabellenname.add("C1 Damen allgemein");
	  tabellenname.add("Titel1");
	  tabellenname.add("Titel2");
	  tabellenname.add("Titel1");
	  tabellenname.add("Titel2");
	  tabellenname.add("Titel1");
	  tabellenname.add("Titel2");
	  tabellenname.add("Titel1");
	  List<List<String>> tabelleninhalt = new ArrayList<>();
	  List<List<List<String>>> daten = new ArrayList<>();
	  String[] HEADER = {"Rang", "Startnr", "Name", "Club", "1. Lauf", "2. Lauf"};
	  List<String> fahrer = new ArrayList<>(); List<String> fahrer1 = new ArrayList<>();
	  fahrer.add("1");
	  fahrer.add("Zarn/Gieriet (Junioren)");
	  fahrer.add("Solothurn West");
	  fahrer.add("167.30");
	  fahrer.add("62");
	  fahrer.add("229.30");
	  fahrer.add("4");
	  fahrer.add("149.32");
	  fahrer.add("14");
	  fahrer.add("163.32");
	  fahrer.add("1");
	  fahrer.add("5555.32");
	  fahrer.add("555.50");
	  fahrer1.add("1");
	  fahrer1.add("5");
	  fahrer1.add("Fellman/Shresta Sereina/Selina (Damen)");
	  fahrer1.add("KCBM");
	  fahrer1.add("167.30");
	  fahrer1.add("62");
	  fahrer1.add("229.30");
	  fahrer1.add("4");
	  fahrer1.add("149.32");
	  fahrer1.add("14");
	  fahrer1.add("163.32");
	  fahrer1.add("1");
	  fahrer1.add("163.32");
	  fahrer1.add("55.50");
	  tabelleninhalt.add(fahrer);
	  tabelleninhalt.add(fahrer1);
	  tabelleninhalt.add(fahrer);
	  tabelleninhalt.add(fahrer1);
	  tabelleninhalt.add(fahrer);
	  tabelleninhalt.add(fahrer1);
	  tabelleninhalt.add(fahrer);
	  tabelleninhalt.add(fahrer1);
	  tabelleninhalt.add(fahrer);
	  tabelleninhalt.add(fahrer1);
	  new PDFController().generierePdfRangliste(dest, rennen, tabellenname, tabelleninhalt);
	  }
	
	// Allgemein
	private static float LAENGE_A4_SEIT = 842;
	private static float BREITE_A4_SEITE = 595;
	private static float EINZUG_RECHTS = 36;
	private static float EINZUG_LINKS = 36;
	//private static float SPACING_VOR_TABELLE = 10;
	private static float TABELLENPADDING = 2;
	private static float OFFSET_EINZUG_INHALT = 50;
	private static float TABELLENBREITE = BREITE_A4_SEITE - EINZUG_RECHTS - EINZUG_LINKS; // 595 -36 -36 = 523
	private static float TABELLENBREITE_PROZENT = 100;
	private static float SPACING_VOR_TABELLENTITEL = 10;
	
	// Schriftgrössen
	private static float SCHRIFT_GR_STANDARDHEADER = 20;
	private static float SCHRIFT_GR_TABELLENHEADER = 8;
	private static float SCHRIFT_GR_DOKUMENTNAME = 16;
	private static float SCHRIFT_GR_TABELLENNAME = 10;
	private static float SCHRIFT_GR_TABELLENINHALT = 8;
	
	// Zellengrössen
	private static float[] ZELLENGROESSE_RANGLISTE = {5, 3, 20, 7, 7, 7, 7, 5, 7, 7, 7, 5, 7, 6};
	private static float[] ZELLENGROESSE_STARTLISTE = {3, 20, 7, 20, 10, 10, 10, 10, 10};
	
	// Tabellenheaderinhalt
	private static String[] INHALT_RANGLISTE = {"Rang", "Nr.", "Name", "Club", "Zeit", "Fehler", "Total", "Rang", "Zeit", "Fehler", "Total", "Rang", "Total", "Diff"};
	private static String[] INHALT_STARTLISTE = {"Nr.", "Name", "Club", "Wohnort", "Block", "1. Lauf", "", "2. Lauf", ""}; // Leerstrings weil die Anzahl der Objekte im Array die Anzahl Tabellenspalten bestimmt
	
	// Tabellenoffset
	private static float OFFSET_RANGLISTE = SCHRIFT_GR_TABELLENHEADER + 2 * TABELLENPADDING;
	private static float OFFSET_STARTLISTE = SCHRIFT_GR_TABELLENHEADER + 2 * TABELLENPADDING;
	
	// Dokumentheader
	private static int HEADER_STARTZEILE = 0;
	private static int HEADER_ENDZEILE = -1;
	private static float EINZUG_OBEN = 36;
	private static float HEADER_Y_POS = LAENGE_A4_SEIT - EINZUG_OBEN;
	private static float HEADER_ABSCHLUSS = SCHRIFT_GR_TABELLENHEADER + 2 * TABELLENPADDING;
	private static BaseColor HEADER_HINTERGRUNDFARBE = BaseColor.GRAY;
	
	// Dokumentinhalt
	private static float INHALT_EINZUG_UNTEN = 36;
	private static float INHALT_EINZUG_OBEN = EINZUG_OBEN + (4.0f * (SCHRIFT_GR_TABELLENHEADER + 2 * TABELLENPADDING) + SCHRIFT_GR_DOKUMENTNAME + OFFSET_EINZUG_INHALT);
	//private static float INHALT_Y_POS = EINZUG_OBEN + 4*(SCHRIFT_GR_TABELLENHEADER + 2 * TABELLENPADDING);

	// Fonts
	private static Font FONT_STANDARDHEADER = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_STANDARDHEADER, Font.BOLDITALIC, BaseColor.BLACK);
	private static Font FONT_TABELLENHEADER = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_TABELLENHEADER, Font.NORMAL, BaseColor.BLACK);
	private static Font FONT_TABELLENHEADER_FETT = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_TABELLENHEADER, Font.BOLD, BaseColor.BLACK);
	private static Font FONT_DOKUMENTNAME = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_DOKUMENTNAME, Font.BOLDITALIC, BaseColor.BLACK);
	private static Font FONT_TABELLENNAME = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_TABELLENNAME, Font.BOLDITALIC, BaseColor.BLACK);
	private static Font FONT_TABELLENINHALT = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_TABELLENINHALT, Font.NORMAL, BaseColor.BLACK);
	private static Font FONT_TABELLENINHALT_FETT = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_TABELLENINHALT, Font.BOLD, BaseColor.BLACK);
	
	/**
	 * Die innere Klasse "Dokumentheader" erweitert die Klasse
	 * "PdfPageEventHelper", welche von der iText-Library bereitgestellt wird.
	 * Der Dokumentheader ist auf jeder Seite zu sehen und besteht aus dem Standardheader und dem tabellenheader.
	 * 
	 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
	 * @date 09.05.2017
	 * @version 1.0
	 *
	 */
	static class Dokumentheader extends PdfPageEventHelper {
		protected PdfPTable standardheader;
		protected PdfPTable tabellenheader;

		public Dokumentheader(PdfPTable standardheader, PdfPTable tabellenheader) {
			this.standardheader = standardheader;
			this.tabellenheader = tabellenheader;
		}

		// Wird aufgerufen bevor eine Seite fertig ist. Genau vor dem eigentlichen Generieren einer Seite.
		public void onEndPage(PdfWriter writer, Document dokument) {
			this.standardheader.writeSelectedRows(HEADER_STARTZEILE, HEADER_ENDZEILE, EINZUG_OBEN, HEADER_Y_POS, writer.getDirectContent());
			this.tabellenheader.writeSelectedRows(HEADER_STARTZEILE, HEADER_ENDZEILE, EINZUG_OBEN, HEADER_Y_POS - standardheader.getTotalHeight(), writer.getDirectContent());
		}
	}	 

	private static PdfPTable erstelleStandardheader(Rennen rennen) {
		PdfPTable tabelleHeader = new PdfPTable(1);
		tabelleHeader.setTotalWidth(TABELLENBREITE);
		tabelleHeader.setWidthPercentage(TABELLENBREITE_PROZENT);
		PdfPCell headerZelle = new PdfPCell(new Phrase(rennen.getTitel(), FONT_STANDARDHEADER));
		headerZelle.setBorder(Rectangle.NO_BORDER);
		headerZelle.setFixedHeight(SCHRIFT_GR_STANDARDHEADER + 2 * TABELLENPADDING);
		headerZelle.setPadding(TABELLENPADDING);
		headerZelle.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerZelle.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabelleHeader.addCell(headerZelle);
		headerZelle = new PdfPCell(new Phrase(rennen.getName() + ", " + rennen.getDatumVon().getTime() + " - " + rennen.getDatumBis().getTime(), FONT_STANDARDHEADER));
		headerZelle.setBorder(Rectangle.NO_BORDER);
		headerZelle.setFixedHeight(SCHRIFT_GR_STANDARDHEADER + 2 * TABELLENPADDING);
		headerZelle.setPadding(TABELLENPADDING);
		headerZelle.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerZelle.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabelleHeader.addCell(headerZelle);
		headerZelle = new PdfPCell(new Phrase(rennen.getVeranstalter(), FONT_STANDARDHEADER));
		headerZelle.setBorder(Rectangle.NO_BORDER);
		headerZelle.setFixedHeight(SCHRIFT_GR_STANDARDHEADER + 2 * TABELLENPADDING);
		headerZelle.setPadding(TABELLENPADDING);
		headerZelle.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerZelle.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabelleHeader.addCell(headerZelle);
		headerZelle = new PdfPCell(new Phrase());
		headerZelle.setBorder(Rectangle.NO_BORDER);
		headerZelle.setFixedHeight(HEADER_ABSCHLUSS * 0.75f);
		tabelleHeader.addCell(headerZelle);
		headerZelle = new PdfPCell(new Phrase());
		headerZelle.setBorder(Rectangle.NO_BORDER);
		headerZelle.setFixedHeight(HEADER_ABSCHLUSS * 0.25f);
		headerZelle.setBackgroundColor(HEADER_HINTERGRUNDFARBE);
		tabelleHeader.addCell(headerZelle);
		return tabelleHeader;
	}
	
	public static void generierePdfRangliste(String pfad, Rennen rennen, List<String> tabellenname, List<List<String>> tabelleninhalt) throws IOException, DocumentException {
		// Neues File generieren
		pfad = pfad + "Rangliste.pdf";
		File file = new File(pfad);
		file.getParentFile().mkdirs();
		Document dokument = new Document(PageSize.A4, EINZUG_LINKS, EINZUG_RECHTS, INHALT_EINZUG_OBEN + OFFSET_RANGLISTE, INHALT_EINZUG_UNTEN);
		PdfWriter writer = PdfWriter.getInstance(dokument, new FileOutputStream(pfad));
		// Dokumentheader definieren
		Dokumentheader dokumentheader = new Dokumentheader(erstelleStandardheader(rennen), erstelleRanglistenheader());
		writer.setPageEvent(dokumentheader);
		// Dokument erstellen
		dokument.open();
		// Dokumentinhalt definieren
		Paragraph paragDokumentname = new Paragraph("Rangliste", FONT_DOKUMENTNAME);
		paragDokumentname.setSpacingBefore(SPACING_VOR_TABELLENTITEL);
		paragDokumentname.setAlignment(Element.ALIGN_CENTER);
		dokument.add(paragDokumentname);
		// Für jede Tabelle...
		for (int i = 0; i < tabellenname.size(); i++) {
			Paragraph paragTabellenname = new Paragraph(tabellenname.get(i), FONT_TABELLENNAME);
			PdfPTable tabelle = new PdfPTable(tabelleninhalt.get(0).size());
			tabelle.setTotalWidth(TABELLENBREITE);
			tabelle.setWidths(ZELLENGROESSE_RANGLISTE);
			tabelle.setWidthPercentage(TABELLENBREITE_PROZENT);
			tabelle.setHorizontalAlignment(Element.ALIGN_CENTER);
			// ...zu jeder Zeile...
			for (int j = 0; j < tabelleninhalt.size(); j ++) {
				// ...alle Zellen hinzufügen
				for (int k = 0; k < tabelleninhalt.get(j).size(); k++) {
					PdfPCell zelle = new PdfPCell();
					if (k == 12) {
						zelle.setPhrase(new Phrase(tabelleninhalt.get(j).get(k), FONT_TABELLENINHALT_FETT));
					} else {
						zelle.setPhrase(new Phrase(tabelleninhalt.get(j).get(k), FONT_TABELLENINHALT));
					}
					zelle.setBorder(Rectangle.NO_BORDER);
					tabelle.addCell(zelle);
				}
			}
			// Daten zum Dokument hinzufügen
			paragTabellenname.add(tabelle);
			dokument.add(paragTabellenname);
		}
		// Dokument schliessen
		dokument.close();
	}
	
	public static void generierePdfStartliste(String pfad, Rennen rennen, List<String> tabellenname, List<List<String>> tabelleninhalt) throws IOException, DocumentException {
		// Neues File generieren
		pfad = pfad + "Startliste.pdf";
		File file = new File(pfad);
		file.getParentFile().mkdirs();
		Document dokument = new Document(PageSize.A4, EINZUG_LINKS, EINZUG_RECHTS, INHALT_EINZUG_OBEN + OFFSET_STARTLISTE, INHALT_EINZUG_UNTEN);
		PdfWriter writer = PdfWriter.getInstance(dokument, new FileOutputStream(pfad));
		// Dokumentheader definieren
		Dokumentheader dokumentheader = new Dokumentheader(erstelleStandardheader(rennen), erstelleStartlistenheader());
		writer.setPageEvent(dokumentheader);
		// Dokument erstellen
		dokument.open();
		// Dokumentinhalt definieren
		Paragraph paragDokumentname = new Paragraph("Startliste", FONT_DOKUMENTNAME);
		paragDokumentname.setSpacingBefore(SPACING_VOR_TABELLENTITEL);
		paragDokumentname.setAlignment(Element.ALIGN_CENTER);
		dokument.add(paragDokumentname);
		// Für jede Tabelle...
		for (int i = 0; i < tabellenname.size(); i++) {
			Paragraph paragTabellenname = new Paragraph(tabellenname.get(i), FONT_TABELLENNAME);
			PdfPTable tabelle = new PdfPTable(tabelleninhalt.get(0).size());
			tabelle.setTotalWidth(TABELLENBREITE);
			tabelle.setWidths(ZELLENGROESSE_STARTLISTE);
			tabelle.setWidthPercentage(TABELLENBREITE_PROZENT);
			tabelle.setHorizontalAlignment(Element.ALIGN_CENTER);
			// ...zu jeder Zeile...
			for (int j = 0; j < tabelleninhalt.size(); j ++) {
				// ...alle Zellen hinzufügen
				for (int k = 0; k < tabelleninhalt.get(j).size(); k++) {
					PdfPCell zelle = new PdfPCell();
					if (k == 12) {
						zelle.setPhrase(new Phrase(tabelleninhalt.get(j).get(k), FONT_TABELLENINHALT_FETT));
					} else {
						zelle.setPhrase(new Phrase(tabelleninhalt.get(j).get(k), FONT_TABELLENINHALT));
					}
					zelle.setBorder(Rectangle.NO_BORDER);
					tabelle.addCell(zelle);
				}
			}
			// Daten zum Dokument hinzufügen
			paragTabellenname.add(tabelle);
			dokument.add(paragTabellenname);
		}
		// Dokument schliessen
		dokument.close();
	}
	
	private static PdfPTable erstelleRanglistenheader() throws DocumentException {
		PdfPTable ranglistenheader = new PdfPTable(INHALT_RANGLISTE.length);
		ranglistenheader.setTotalWidth(TABELLENBREITE);
		ranglistenheader.setWidthPercentage(TABELLENBREITE_PROZENT);
		ranglistenheader.setWidths(ZELLENGROESSE_RANGLISTE);
		for (int i = 0; i < INHALT_RANGLISTE.length; i++) {
			PdfPCell zelle = new PdfPCell();
			if (i == 12) {
				zelle.setPhrase(new Phrase(INHALT_RANGLISTE[i], FONT_TABELLENHEADER_FETT));
			} else {
				zelle.setPhrase(new Phrase(INHALT_RANGLISTE[i], FONT_TABELLENHEADER));
			}
			zelle.setBorder(Rectangle.NO_BORDER);
			zelle.setFixedHeight(SCHRIFT_GR_TABELLENHEADER + 2 * TABELLENPADDING);
			zelle.setPadding(TABELLENPADDING);
			zelle.setVerticalAlignment(Element.ALIGN_MIDDLE);
			zelle.setBackgroundColor(HEADER_HINTERGRUNDFARBE);
			ranglistenheader.addCell(zelle);
		}
		return ranglistenheader;
	}
	
	private static PdfPTable erstelleStartlistenheader() throws DocumentException {
		PdfPTable ranglistenheader = new PdfPTable(INHALT_STARTLISTE.length);
		ranglistenheader.setTotalWidth(TABELLENBREITE);
		ranglistenheader.setWidthPercentage(TABELLENBREITE_PROZENT);
		ranglistenheader.setWidths(ZELLENGROESSE_STARTLISTE);
		for (int i = 0; i < INHALT_STARTLISTE.length; i++) {
			PdfPCell zelle = new PdfPCell();
			if (i == 12) {
				zelle.setPhrase(new Phrase(INHALT_STARTLISTE[i], FONT_TABELLENHEADER_FETT));
			} else {
				zelle.setPhrase(new Phrase(INHALT_STARTLISTE[i], FONT_TABELLENHEADER));
			}
			zelle.setBorder(Rectangle.NO_BORDER);
			zelle.setFixedHeight(SCHRIFT_GR_TABELLENHEADER + 2 * TABELLENPADDING);
			zelle.setPadding(TABELLENPADDING);
			zelle.setVerticalAlignment(Element.ALIGN_MIDDLE);
			zelle.setBackgroundColor(HEADER_HINTERGRUNDFARBE);
			ranglistenheader.addCell(zelle);
		}
		return ranglistenheader;
	}
	
	/**
	 * Generiert eine oder mehrere Tabellen in einem PDF-Dokument.
	 * 
	 * @param pfad
	 *            - Speicherort des Dokuments
	 * @param rennen
	 *            - Aktuelles Rennen
	 * @param dokumenttitel
	 *            - Titel des Dokuments
	 * @param tabellentitel
	 *            - Liste von Strings mit den jeweiligen Tabellentiteln.
	 *            ACHTUNG: Muss gleich viele Einträge haben wie @param daten!
	 * @param header
	 *            - Enthält den Header der Tabellen. Ist immer bei allen
	 *            Tabellen der gleiche. ACHTUNG: Muss so viele Einträge haben
	 *            wie eine Tabelle Spalten hat!
	 * @param daten
	 *            - Enthält die Daten der Tabellen in drei Ebenen. Ebene 1
	 *            ("Daten"): Liste von Tabellen. Jeder Eintrag ist eine ganze
	 *            Tabelle. Muss gleich viele Einträge haben wie @param
	 *            tabellentitel. Ebene 2 ("Tabelle"): Liste von einzelnen
	 *            Tabelleneinträgen. Jeder Eintrag ist eine Tabellenzeile. Ebene
	 *            3 ("Zeile"): Liste von Zelleneinträgen. Jeder Eintrag ist ein
	 *            String, der Schlussendlich in einer einzelnen Zelle steht.
	 * @throws IOException
	 * @throws DocumentException
	 *//*
	public static void generierePDF(String pfad, Rennen rennen, String dokumenttitel, List<String> tabellentitel,
			String[] header, List<List<List<String>>> daten) throws IOException, DocumentException {
		// Daten bereitstellen
		int anzTabellen = daten.size();
		int anzSpalten = header.length;
		int anzZeilen = daten.get(0).size();

		// Neues File generieren
		File file = new File(pfad);
		file.getParentFile().mkdirs();
		Document dokument = new Document(PageSize.A4, EINZUG_LINKS, EINZUG_RECHTS, EINZUG_INHALT_OBEN, EINZUG_UNTEN);
		PdfWriter writer = PdfWriter.getInstance(dokument, new FileOutputStream(pfad));

		// Dokument erstellen
		dokument.open();

		// Dokumentheader
		PdfPTable tabelleHeader = new PdfPTable(1);
		tabelleHeader.setTotalWidth(HEADER_TABELLENBREITE);
		PdfPCell headerZelle = new PdfPCell(new Phrase(rennen.getTitel(), FONT_DOKUMENTHEADER));
		headerZelle.setBorder(Rectangle.NO_BORDER);
		headerZelle.setFixedHeight(HEADER_TABELLENSCHRIFTGROESSE + 2 * HEADER_TABELLENPADDING);
		headerZelle.setPadding(HEADER_TABELLENPADDING);
		headerZelle.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerZelle.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabelleHeader.addCell(headerZelle);
		headerZelle = new PdfPCell(new Phrase(rennen.getName() + ", " + rennen.getDatumVon().getTime() + " - " + rennen.getDatumBis().getTime(), FONT_DOKUMENTHEADER));
		headerZelle.setBorder(Rectangle.NO_BORDER);
		headerZelle.setFixedHeight(HEADER_TABELLENSCHRIFTGROESSE + 2 * HEADER_TABELLENPADDING);
		headerZelle.setPadding(HEADER_TABELLENPADDING);
		headerZelle.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerZelle.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabelleHeader.addCell(headerZelle);
		headerZelle = new PdfPCell(new Phrase(rennen.getVeranstalter(), FONT_DOKUMENTHEADER));
		headerZelle.setBorder(Rectangle.NO_BORDER);
		headerZelle.setFixedHeight(HEADER_TABELLENSCHRIFTGROESSE + 2 * HEADER_TABELLENPADDING);
		headerZelle.setPadding(HEADER_TABELLENPADDING);
		headerZelle.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerZelle.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabelleHeader.addCell(headerZelle);
		headerZelle = new PdfPCell(new Phrase());
		headerZelle.setBorder(Rectangle.NO_BORDER);
		headerZelle.setFixedHeight(HEADER_ABSCHLUSS * 0.75f);
		tabelleHeader.addCell(headerZelle);
		headerZelle = new PdfPCell(new Phrase());
		headerZelle.setBorder(Rectangle.NO_BORDER);
		headerZelle.setFixedHeight(HEADER_ABSCHLUSS * 0.25f);
		headerZelle.setBackgroundColor(HINTERGRUNDFARBE);
		tabelleHeader.addCell(headerZelle);
		// Titel zum Header hinzufügen
		
		
		// Daten an Header übergeben
		Dokumentheader dokumentheader = new Dokumentheader(tabelleHeader);
		writer.setPageEvent(dokumentheader);

		// Dokumenttitel
		Paragraph paragDokumenttitel = new Paragraph(dokumenttitel, FONT_DOKUMENTTITEL);
		paragDokumenttitel.setSpacingBefore(1);
		paragDokumenttitel.setAlignment(Element.ALIGN_CENTER);
		dokument.add(paragDokumenttitel);

		// Tabellen erstellen
		// Ebene "Daten"
		for (int i = 0; i < anzTabellen; i++) {
			// Ebene "Tabelle"
			PdfPTable tabelle = new PdfPTable(anzSpalten);
			for (int j = 0; j < anzSpalten; j++) {
				// Ebene "Zeile"
				PdfPCell zelle = new PdfPCell(new Phrase(header[j], FONT_TABELLENHEADER));
				zelle.setBackgroundColor(HINTERGRUNDFARBE);
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
	}*/
}
