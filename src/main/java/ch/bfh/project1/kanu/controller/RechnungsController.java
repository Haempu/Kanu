package ch.bfh.project1.kanu.controller;

import java.util.ArrayList;

import ch.bfh.project1.kanu.model.Club;
import ch.bfh.project1.kanu.view.RechnungsView;

/**
 * Die Klasse RechnungsController beinhaltet die Logik der Klasse RecnungsView.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class RechnungsController {
	// private static String[] HEADER {""};
	private DBController dbController;
	private RechnungsView rechnungsView;

	/**
	 * Lädt alle Klubs, die mindestens einen Fahrer an das Rennen angemeldet
	 * haben.
	 */
	public ArrayList<Club> ladeAngemeldeteClubs() {
		return dbController.ladeAngemeldeteClubs();
	}

	public void rechnungErstellen(Club club) {
		// dbController.
		// PDFController.generierePDF(pfad, tabellentitel, HEADER, daten);
	}

	public void rechnungBezahlen(Club club, boolean bezahlt) {

	}

}