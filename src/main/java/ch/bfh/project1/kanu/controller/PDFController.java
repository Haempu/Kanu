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
	private static float SPACING_VOR_TABELLENTITEL = 10;
	private static float SPACING_VOR_TABELLE = 10;
	private static float EINZUG_RECHTS = 36;
	private static float EINZUG_LINKS = 36;
	private static float EINZUG_OBEN = 36;
	private static float EINZUG_UNTEN = 36;
	private static int HEADER_STARTZEILE = 0;
	private static int HEADER_ENDZEILE = -1;
	private static float LAENGE_A4_SEIT = 845;
	private static float BREITE_A4_SEITE = 595;
	private static float HEADER_TABELLENPADDING = 2;
	private static float HEADER_TABELLENSCHRIFTGROESSE = 30;
	private static Font FONT_DOKUMENTHEADER = FontFactory.getFont(FontFactory.HELVETICA, HEADER_TABELLENSCHRIFTGROESSE,
			Font.BOLDITALIC, BaseColor.BLACK);
	private static Font FONT_DOKUMENTTITEL = FontFactory.getFont(FontFactory.HELVETICA, 25, Font.BOLDITALIC,
			BaseColor.BLACK);
	private static Font FONT_TABELLENTITEL = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLDITALIC,
			BaseColor.BLACK);
	private static Font FONT_TABELLENHEADER = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC,
			BaseColor.BLACK);
	private static BaseColor HINTERGRUNDFARBE = BaseColor.GRAY;

	private static float EINZUG_INHALT_OBEN = 4.0f * (HEADER_TABELLENSCHRIFTGROESSE + 2 * HEADER_TABELLENPADDING)
			+ EINZUG_OBEN + SPACING_VOR_TABELLE;
	private static float HEADER_Y_POS = LAENGE_A4_SEIT - EINZUG_OBEN;
	private static float HEADER_TABELLENBREITE = BREITE_A4_SEITE - EINZUG_RECHTS - EINZUG_LINKS;
	private static float HEADER_ABSCHLUSS = HEADER_TABELLENSCHRIFTGROESSE + 2 * HEADER_TABELLENPADDING;

	/**
	 * Die innere Klasse "Dokumentheader" erweitert die Klasse
	 * "PdfPageEventHelper", welche von der iText-Library bereitgestellt wird.
	 * Damit ein Dokumentheader erzeugt werden kann, der auf jeder Seite
	 * erscheint. Muss diese Klasse existieren.
	 * 
	 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
	 * @date 09.05.2017
	 * @version 1.0
	 *
	 */
	static class Dokumentheader extends PdfPageEventHelper {
		protected PdfPTable header;

		public Dokumentheader(PdfPTable header) {
			this.header = header;
		}

		// Wird aufgerufen bevor eine Seite fertig ist. Genau vor dem
		// eigentlichen Generieren einer Seite.
		public void onEndPage(PdfWriter writer, Document dokument) {
			header.writeSelectedRows(HEADER_STARTZEILE, HEADER_ENDZEILE, EINZUG_OBEN, HEADER_Y_POS,
					writer.getDirectContent());
			// header.writeSelectedRows(3, HEADER_ENDZEILE, 138, 809,
			// writer.getDirectContent());
		}
	}
	/*
	 * // TODO: Nur zum testen. public static void main(String args[]) throws
	 * IOException, DocumentException { Rennen rennen = new Rennen();
	 * rennen.setTitel("45. Aaremeisterschaft"); rennen.setName("Kanuslalom");
	 * rennen.setVeranstalter("Kanu-Club Grenchen"); rennen.setDatumVon(new
	 * Date(3000)); rennen.setDatumBis(new Date(7000)); String dest =
	 * "C:/Daten/Patrik/test.pdf"; List<String> titel = new ArrayList();;
	 * titel.add("C1 Herren allgemein"); titel.add("C1 Damen allgemein");
	 * titel.add("Titel1"); titel.add("Titel2"); titel.add("Titel1");
	 * titel.add("Titel2"); titel.add("Titel1"); titel.add("Titel2");
	 * titel.add("Titel1"); List<List<String>> rangliste = new ArrayList<>();
	 * List<List<List<String>>> daten = new ArrayList<>(); String[] HEADER =
	 * {"Rang", "Startnr", "Name", "Club", "1. Lauf", "2. Lauf"}; List<String>
	 * fahrer = new ArrayList<>(); List<String> fahrer1 = new ArrayList<>();
	 * fahrer.add("Hans"); fahrer.add("Muster"); fahrer.add("Jet-Ski");
	 * fahrer.add("böö"); fahrer.add("böö"); fahrer.add("böö");
	 * rangliste.add(fahrer); fahrer1.add("Fritz"); fahrer1.add("Bünzli");
	 * fahrer1.add("Kanu"); fahrer1.add("böö"); fahrer1.add("böö");
	 * fahrer1.add("böö"); rangliste.add(fahrer1); daten.add(rangliste);
	 * daten.add(rangliste); daten.add(rangliste); daten.add(rangliste);
	 * daten.add(rangliste); daten.add(rangliste); daten.add(rangliste);
	 * daten.add(rangliste); daten.add(rangliste); new
	 * PDFController().generierePDF(dest, rennen, "Startliste", titel, HEADER,
	 * daten); }
	 */

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
	 */
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
		headerZelle = new PdfPCell(new Phrase(
				rennen.getName() + ", " + rennen.getDatumVon().getTime() + " - " + rennen.getDatumBis().getTime(),
				FONT_DOKUMENTHEADER));
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
	}
}
