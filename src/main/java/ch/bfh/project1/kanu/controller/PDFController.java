package ch.bfh.project1.kanu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rangliste;
import ch.bfh.project1.kanu.model.Rennen;

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
	// Allgemein
	private static float LAENGE_A4_SEIT = 842;
	private static float BREITE_A4_SEITE = 595;
	private static float EINZUG_RECHTS = 36;
	private static float EINZUG_LINKS = 36;
	private static float TABELLENPADDING = 2;
	private static float OFFSET_EINZUG_INHALT = 50;
	private static float TABELLENBREITE = BREITE_A4_SEITE - EINZUG_RECHTS - EINZUG_LINKS;
	private static float TABELLENBREITE_PROZENT = 100;
	private static float SPACING_VOR_TABELLENTITEL = 10;
	
	// Schriftgrössen
	private static float SCHRIFT_GR_STANDARDHEADER = 20;
	private static float SCHRIFT_GR_TABELLENHEADER = 8;
	private static float SCHRIFT_GR_DOKUMENTNAME = 16;
	private static float SCHRIFT_GR_TABELLENNAME = 10;
	private static float SCHRIFT_GR_TABELLENINHALT = 8;
	private static float SCHRIFT_GR_TABELLENINHALT_GROSS = 16;
	private static float SCHRIFT_GR_TABELLENNAME_GROSS = 16;
	
	// Zellengrössen
	private static float[] ZELLENGROESSE_RANGLISTE = {5, 3, 17, 10, 8, 8, 8, 8, 8, 8, 8, 8};
	//private static float[] ZELLENGROESSE_STARTLISTE = {3, 20, 7, 20, 10, 10, 10, 10, 10};
	//private static float[] ZELLENGROESSE_RECHNUNG_TITEL = {40, 20, 40};
	private static float[] ZELLENGROESSE_RECHNUNG = {5, 5, 50, 20, 20};
	
	// Tabellenheaderinhalt
	private static String[] INHALT_RANGLISTE = {"Rang", "Nr.", "Name", "Club", "Zeit", "Fehler", "Total", "Zeit", "Fehler", "Total", "Total", "Differenz"};
	//private static String[] INHALT_STARTLISTE = {"Nr.", "Name", "Club", "Wohnort", "Block", "1. Lauf", "", "2. Lauf", ""}; // Leerstrings weil die Anzahl der Objekte im Array die Anzahl Tabellenspalten bestimmt
	
	// Tabellenoffset
	private static float OFFSET_RANGLISTE = SCHRIFT_GR_TABELLENHEADER + 2 * TABELLENPADDING;
	//private static float OFFSET_STARTLISTE = SCHRIFT_GR_TABELLENHEADER + 2 * TABELLENPADDING;
	
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

	// Fonts
	private static Font FONT_STANDARDHEADER = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_STANDARDHEADER, Font.BOLDITALIC, BaseColor.BLACK);
	private static Font FONT_TABELLENHEADER = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_TABELLENHEADER, Font.NORMAL, BaseColor.BLACK);
	private static Font FONT_TABELLENHEADER_FETT = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_TABELLENHEADER, Font.BOLD, BaseColor.BLACK);
	private static Font FONT_DOKUMENTNAME = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_DOKUMENTNAME, Font.BOLDITALIC, BaseColor.BLACK);
	private static Font FONT_TABELLENNAME = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_TABELLENNAME, Font.BOLDITALIC, BaseColor.BLACK);
	private static Font FONT_TABELLENINHALT = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_TABELLENINHALT, Font.NORMAL, BaseColor.BLACK);
	private static Font FONT_TABELLENINHALT_FETT = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_TABELLENINHALT, Font.BOLD, BaseColor.BLACK);
	private static Font FONT_TABELLENINHALT_GROSS = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_TABELLENINHALT_GROSS, Font.NORMAL, BaseColor.BLACK);
	private static Font FONT_TABELLENINHALT_GROSS_FETT = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_TABELLENINHALT_GROSS, Font.BOLD, BaseColor.BLACK);
	private static Font FONT_TABELLENNAME_GROSS = FontFactory.getFont(FontFactory.HELVETICA, SCHRIFT_GR_TABELLENNAME_GROSS, Font.BOLDITALIC, BaseColor.BLACK);
	
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

		/**
		 * Konstruktor der inneren Klasse "Dokumentheader".
		 * 
		 * @param standardheader - Enthält die Tabelle für den Standardheader, welcher jedes Dokument hat.
		 * @param tabellenheader - Enthält die Tabelle für den spezifischen Header, welcher für jedes Dokument anders sein kann.
		 */
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
	
	/**
	 * Funktion generiert ein PDF, welches dem Layout der Rangliste entspricht.
	 * 
	 * @param pfad - Ablageort des PDF
	 * @param rangliste - Ranglistenobjekt von welchem die Rangliste generiert werden soll
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static String generierePdfRangliste(String pfad, Rangliste rangliste) throws IOException, DocumentException {
		// Neues File generieren
		pfad = "Rangliste.pdf";
		//File file = new File(pfad);
		//file.getParentFile().mkdirs();
		Document dokument = new Document(PageSize.A4, EINZUG_LINKS, EINZUG_RECHTS, INHALT_EINZUG_OBEN + OFFSET_RANGLISTE, INHALT_EINZUG_UNTEN);
		PdfWriter writer = PdfWriter.getInstance(dokument, new FileOutputStream(pfad));
		// Dokumentheader definieren
		Dokumentheader dokumentheader = new Dokumentheader(erstelleStandardheader(rangliste.getRennen()), erstelleRanglistenheader());
		writer.setPageEvent(dokumentheader);
		// Dokument erstellen
		dokument.open();
		// Dokumentinhalt definieren
		Paragraph paragDokumentname = new Paragraph("Rangliste", FONT_DOKUMENTNAME);
		paragDokumentname.setSpacingBefore(SPACING_VOR_TABELLENTITEL);
		paragDokumentname.setAlignment(Element.ALIGN_CENTER);
		dokument.add(paragDokumentname);
		String tabellentitel = "";
		int tabellenerster = 0;
		int rang = 0;
		Paragraph paragTabellenname = null;
		PdfPTable tabelle = null;
		List<FahrerResultat> resultate = rangliste.getResultate();
		resultate.add(new FahrerResultat(new AltersKategorie(-2, ""))); //Dummy Element, um Abschluss zu finden
		for (int i = 0; i < resultate.size() - 1; i++) { //-1 wegen Dummy Element
			FahrerResultat fr = resultate.get(i);
			// Neue Tabelle mit Titel wenn Rennkategorie gewechselt hat (daten.get(i).get(0) = Rennkategorie)
			if (!tabellentitel.equals(fr.getKategorie().getName())) {
				rang = 1;
				tabellenerster = i;
				tabellentitel = fr.getKategorie().getName();
				paragTabellenname = new Paragraph(tabellentitel, FONT_TABELLENNAME);
				tabelle = new PdfPTable(ZELLENGROESSE_RANGLISTE);
				tabelle.setTotalWidth(TABELLENBREITE);
				tabelle.setWidthPercentage(TABELLENBREITE_PROZENT);
				tabelle.setHorizontalAlignment(Element.ALIGN_CENTER);
			}
			// Einzelne Zeilen abfüllen
			SimpleDateFormat sdfMin = new SimpleDateFormat("mm:ss.S");
			//SimpleDateFormat sdfSek = new SimpleDateFormat("ss.S");
			PdfPCell zelle = new PdfPCell();
			zelle.setPhrase(new Phrase(""+rang, FONT_TABELLENINHALT));
			zelle.setBorder(Rectangle.NO_BORDER);
			tabelle.addCell(zelle);
			zelle.setPhrase(new Phrase("" + fr.getStartnummer(), FONT_TABELLENINHALT));
			zelle.setBorder(Rectangle.NO_BORDER);
			tabelle.addCell(zelle);
			zelle.setPhrase(new Phrase(fr.getFahrer().getName() + " " + fr.getFahrer().getVorname(), FONT_TABELLENINHALT));
			zelle.setBorder(Rectangle.NO_BORDER);
			tabelle.addCell(zelle);
			zelle.setPhrase(new Phrase(fr.getFahrer().getClub().getKennung(), FONT_TABELLENINHALT));
			zelle.setBorder(Rectangle.NO_BORDER);
			tabelle.addCell(zelle);
			zelle.setPhrase(new Phrase(sdfMin.format(new Date(fr.getZeitErsterLauf())), FONT_TABELLENINHALT));
			zelle.setBorder(Rectangle.NO_BORDER);
			tabelle.addCell(zelle);
			zelle.setPhrase(new Phrase(fr.getStrafzeit1().toString(), FONT_TABELLENINHALT));
			zelle.setBorder(Rectangle.NO_BORDER);
			tabelle.addCell(zelle);
			zelle.setPhrase(new Phrase(renderMilli(fr.getGesamtzeit1()), FONT_TABELLENINHALT));
			zelle.setBorder(Rectangle.NO_BORDER);
			tabelle.addCell(zelle);
			zelle.setPhrase(new Phrase(sdfMin.format(new Date(fr.getZeitZweiterLauf())), FONT_TABELLENINHALT));
			zelle.setBorder(Rectangle.NO_BORDER);
			tabelle.addCell(zelle);
			zelle.setPhrase(new Phrase(fr.getStrafzeit2().toString(), FONT_TABELLENINHALT));
			zelle.setBorder(Rectangle.NO_BORDER);
			tabelle.addCell(zelle);
			zelle.setPhrase(new Phrase(renderMilli(fr.getGesamtzeit2()), FONT_TABELLENINHALT));
			zelle.setBorder(Rectangle.NO_BORDER);
			tabelle.addCell(zelle);
			zelle.setPhrase(new Phrase(renderMilli(fr.getZeitTotal()), FONT_TABELLENINHALT_FETT));
			zelle.setBorder(Rectangle.NO_BORDER);
			tabelle.addCell(zelle);
			zelle.setPhrase(new Phrase(renderMilli((fr.getZeitTotal() - resultate.get(tabellenerster).getZeitTotal())), FONT_TABELLENINHALT));
			zelle.setBorder(Rectangle.NO_BORDER);
			tabelle.addCell(zelle);
			rang++;
			// Checken ob Abschluss der Tabelle erreicht
			if (!tabellentitel.equals(resultate.get(i+1).getKategorie().getName())) {
				paragTabellenname.add(tabelle);
				dokument.add(paragTabellenname);
			}
		}
		dokument.close();
		return pfad;
	}
	
	/**
	 * Funktion generiert ein PDF, welches dem Layout der Abrechnung entpsricht.
	 * 
	 * @param pfad - Ablageort des PDF
	 * @param rennen - Aktuelles Rennobjekt von welchem die Rechnung generiert werden soll
	 * @param clubname - Name des Klubs für welchen die Rechnung generiert werden soll
	 * @param daten - Daten enthält die Informationen welcher Fahrer des Klubs welche Rennen gefahren ist und entsprechend wieviel geschuldet wird
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static String generierePdfRechnung(String pfad, Rennen rennen, String clubname, List<List<String>> daten) throws IOException, DocumentException {
		// Neues File generieren
		pfad = "Abrechnung_" + clubname + ".pdf";
		File file = new File(pfad);
		file.getParentFile().mkdirs();
		Document dokument = new Document(PageSize.A4, EINZUG_LINKS, EINZUG_RECHTS, INHALT_EINZUG_OBEN + OFFSET_RANGLISTE, INHALT_EINZUG_UNTEN);
		PdfWriter writer = PdfWriter.getInstance(dokument, new FileOutputStream(pfad));
		// Dokumentheader definieren
		Dokumentheader dokumentheader = new Dokumentheader(erstelleStandardheader(rennen), erstelleAbrechnungsheader());
		writer.setPageEvent(dokumentheader);
		// Dokument erstellen
		dokument.open();
		// Dokumentinhalt definieren
		Paragraph paragDokumentname = new Paragraph("Abrechnung", FONT_DOKUMENTNAME);
		paragDokumentname.setSpacingBefore(SPACING_VOR_TABELLENTITEL);
		paragDokumentname.setAlignment(Element.ALIGN_CENTER);
		Paragraph paragClubname = new Paragraph(clubname, FONT_DOKUMENTNAME);
		paragClubname.setSpacingBefore(SPACING_VOR_TABELLENTITEL);
		dokument.add(paragDokumentname);
		String tabellentitel = "";
		Paragraph paragTabellenname = null;
		PdfPTable tabelle = null;
		//boolean neueTabelle = true;
		int anzZeilen = 0;
		int gesamtsumme = 0;
		// Für jede Tabelle...
		for (int i = 0; i < daten.size(); i++) {
			// Neue Tabelle mit Titel wenn Rennkategorie gewechselt hat (daten.get(i).get(0) = Rennkategorie)
			if (!tabellentitel.equals(daten.get(i).get(0))) {
				anzZeilen = 0;
				tabellentitel = daten.get(i).get(0);
				paragTabellenname = new Paragraph(tabellentitel, FONT_TABELLENNAME_GROSS);
				tabelle = new PdfPTable(ZELLENGROESSE_RECHNUNG);
				tabelle.setTotalWidth(TABELLENBREITE);
				tabelle.setWidthPercentage(TABELLENBREITE_PROZENT);
				tabelle.setHorizontalAlignment(Element.ALIGN_CENTER);
			}
			// Einzelne Zeile abfüllen
			anzZeilen++;
			for (int j = 0; j < ZELLENGROESSE_RECHNUNG.length; j ++) {
				
				PdfPCell zelle = new PdfPCell();
				if (j == 0 || j == 4) {
					// 1. Zelle mit "" abfüllen
					zelle.setPhrase(new Phrase(""));
				} else if (j == 3) {
					zelle.setPhrase(new Phrase(daten.get(i).get(j)+ ".00 CHF", FONT_TABELLENINHALT_GROSS));
				} else {
					zelle.setPhrase(new Phrase(daten.get(i).get(j), FONT_TABELLENINHALT_GROSS));
				}
				zelle.setBorder(Rectangle.NO_BORDER);
				tabelle.addCell(zelle);
			}

			if (i < daten.size()-1) {
				// Checken, ob nächste Rennkategorie die gleiche ist.
				if (!tabellentitel.equals(daten.get(i+1).get(0))) {
					PdfPCell weitereZeile = new PdfPCell(new Phrase(""));
					weitereZeile.setBorder(Rectangle.NO_BORDER);
					for (int k = 0; k < ZELLENGROESSE_RECHNUNG.length; k++) {
						if (k == 3) {
							// Total der einzelnen Tabelle setzen
							weitereZeile.setPhrase(new Phrase("" + Integer.parseInt(daten.get(i).get(3)) * anzZeilen + ".00 CHF", FONT_TABELLENINHALT_GROSS_FETT));
							tabelle.addCell(weitereZeile);
							weitereZeile.setPhrase(new Phrase(""));
						} else {
							tabelle.addCell(weitereZeile);
						}
					}
					gesamtsumme = gesamtsumme + (Integer.parseInt(daten.get(i).get(3)) * anzZeilen);
					// Daten zum Dokument hinzufügen
					paragTabellenname.add(tabelle);
					dokument.add(paragTabellenname);
				}
			} else {
				PdfPCell weitereZeile = new PdfPCell(new Phrase("", FONT_TABELLENINHALT_GROSS_FETT));
				weitereZeile.setBorder(Rectangle.NO_BORDER);
				for (int k = 0; k < ZELLENGROESSE_RECHNUNG.length; k++) {
					if (k == 3) {
						// Total der einzelnen Tabelle setzen
						weitereZeile.setPhrase(new Phrase("" + Integer.parseInt(daten.get(i).get(3)) * anzZeilen + ".00 CHF", FONT_TABELLENINHALT_GROSS_FETT));
						tabelle.addCell(weitereZeile);
						weitereZeile.setPhrase(new Phrase("", FONT_TABELLENINHALT_GROSS_FETT));
					} else {
						tabelle.addCell(weitereZeile);
					}
				}
				gesamtsumme = gesamtsumme + (Integer.parseInt(daten.get(i).get(3)) * anzZeilen);
				tabelle.addCell(weitereZeile);
				tabelle.addCell(weitereZeile);
				weitereZeile.setPhrase(new Phrase("Gesamtrechnung", FONT_TABELLENINHALT_GROSS_FETT));
				tabelle.addCell(weitereZeile);
				weitereZeile.setPhrase(new Phrase("", FONT_TABELLENINHALT_GROSS_FETT));
				tabelle.addCell(weitereZeile);
				weitereZeile.setPhrase(new Phrase(gesamtsumme + ".00 CHF", FONT_TABELLENINHALT_GROSS_FETT));
				tabelle.addCell(weitereZeile);
				// Daten zum Dokument hinzufügen
				paragTabellenname.add(tabelle);
				dokument.add(paragTabellenname);
			}
		}
		// Dokument schliessen
		dokument.close();
		return pfad;
	}
	
	/**
	 * Funktion erstellt den Standardheader für alle PDF-Vorlagen. Jede PDF-Vorlage hat den gleichen Standardheader.
	 * 
	 * @param rennen - Rennen von welchem das Dokument erzeugt wurde
	 * @return
	 */
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		headerZelle = new PdfPCell(new Phrase(rennen.getName() + ", " + dateFormat.format(rennen.getDatumVon()) + " - " + dateFormat.format(rennen.getDatumBis()), FONT_STANDARDHEADER));
		headerZelle.setBorder(Rectangle.NO_BORDER);
		headerZelle.setFixedHeight(SCHRIFT_GR_STANDARDHEADER + 2 * TABELLENPADDING);
		headerZelle.setPadding(TABELLENPADDING);
		headerZelle.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerZelle.setHorizontalAlignment(Element.ALIGN_CENTER);
		tabelleHeader.addCell(headerZelle);
		headerZelle = new PdfPCell(new Phrase(rennen.getVeranstalter().getName(), FONT_STANDARDHEADER));
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
	
	/**
	 * Funktion erstellt den spezifischen Header zur Rangliste. Dieser ist ein Zusatzt zum Standardheader.
	 * 
	 * @return
	 * @throws DocumentException
	 */
	private static PdfPTable erstelleRanglistenheader() throws DocumentException {
		PdfPTable ranglistenheader = new PdfPTable(INHALT_RANGLISTE.length);
		ranglistenheader.setTotalWidth(TABELLENBREITE);
		ranglistenheader.setWidthPercentage(TABELLENBREITE_PROZENT);
		ranglistenheader.setWidths(ZELLENGROESSE_RANGLISTE);
		for (int i = 0; i < INHALT_RANGLISTE.length; i++) {
			PdfPCell zelle = new PdfPCell();
			if (i == 10) {
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

	/**
	 * Funktion erstellt den spezifischen Header zur Abrechnung. Dieser ist ein Zusatzt zum Standardheader.
	 * 
	 * @return
	 * @throws DocumentException
	 */
	private static PdfPTable erstelleAbrechnungsheader() throws DocumentException {
		PdfPTable tabelleHeader = new PdfPTable(1);
		tabelleHeader.setTotalWidth(TABELLENBREITE);
		tabelleHeader.setWidthPercentage(TABELLENBREITE_PROZENT);
		PdfPCell headerZelle = new PdfPCell(new Phrase(""));
		headerZelle = new PdfPCell(new Phrase());
		headerZelle.setBorder(Rectangle.NO_BORDER);
		headerZelle.setFixedHeight(HEADER_ABSCHLUSS * 2.0f);
		headerZelle.setBackgroundColor(HEADER_HINTERGRUNDFARBE);
		tabelleHeader.addCell(headerZelle);
		return tabelleHeader;
	}
	
	/**
	 * Gibt eine Zahl in ms im Format mm:ss.S aus.
	 * @param zahl - Zahl in ms
	 * @return
	 */
	private static String renderMilli(Integer zahl)
	{
		Integer hs = (zahl % 1000) / 100;
		Integer s = (zahl / 1000) % 60;
		Integer m = zahl / 60000;
		String string = String.format("%02d:%02d.%d", Math.abs(m), Math.abs(s), Math.abs(hs));
		if(zahl < 0)
			string = "-" + string;
		return string;
	}
}
