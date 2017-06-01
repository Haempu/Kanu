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

	public RechnungsController() {
		this.dbController = DBController.getInstance();
	}

	/**
	 * Lädt alle Klubs
	 */
	public ArrayList<Club> ladeAngemeldeteClubs() {
		return (ArrayList<Club>) this.dbController.ladeClubs();
	}

	public void rechnungErstellen(Club club) {
		//TODO: aus DB laden
	/*	String pfad = "C:/Daten/Patrik/";
		Rennen rennen = dbController.getInstance().ladeRennen(2);
		List<String> tabellenname = new ArrayList<String>();
		tabellenname.add("K1-Damen Benjamin");
		tabellenname.add("K1-Damen Schüler");
		tabellenname.add("K1-Herren Schüler");
		List<List<String>> fahrerliste = new ArrayList<>();
		List<String> fahrerZeile = new ArrayList<>();
		fahrerZeile.add("76");
		fahrerZeile.add("");
		fahrerZeile.add("Huber Wayra");
		fahrerZeile.add("20.00CHF");
		fahrerZeile.add("");
		fahrerliste.add(fahrerZeile);
		fahrerZeile.add("78");
		fahrerZeile.add("");
		fahrerZeile.add("Huber Nina");
		fahrerZeile.add("20.00CHF");
		fahrerZeile.add("");
		fahrerliste.add(fahrerZeile);
		/*fahrerZeile.add("78");
		fahrerZeile.add("");
		fahrerZeile.add("Huber Nina");
		fahrerZeile.add("20.00CHF");
		fahrerZeile.add("");
		fahrerliste.add(fahrerZeile);
		try {
			PDFController.generierePdfRechnung(pfad, rennen, club.getName(), tabellenname, fahrerliste);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public void rechnungBezahlen(Club club, boolean bezahlt) {

	}

}