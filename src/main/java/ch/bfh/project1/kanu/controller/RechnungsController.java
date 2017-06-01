package ch.bfh.project1.kanu.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;

import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Club;
import ch.bfh.project1.kanu.model.FahrerResultat;
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

	public void rechnungErstellen(Club club, Integer rennenID) {

		List<FahrerResultat> fahrer = this.dbController.ladeAngemeldetenFahrerByClub(club.getClubID(), rennenID);
		
		AltersKategorie aktuelleKat = null;
		List<AltersKategorie> angemeldeteKategorien = new ArrayList<AltersKategorie>();
		List<List<String>> alleFahrer = new ArrayList<List<String>>();
		
		for (int i = 0; i < fahrer.size(); i++) {
			List<String> zeile = new ArrayList<>();
			zeile.add(fahrer.get(i).getKategorie().getName());
			zeile.add(Integer.toString(fahrer.get(i).getStartnummer()));
			zeile.add(fahrer.get(i).getFahrer().getName() + " " + fahrer.get(i).getFahrer().getVorname());
			zeile.add(Integer.toString(fahrer.get(i).getKategorie().getGebuehr()));
			alleFahrer.add(zeile);
		}
		try {
			PDFController.generierePdfRechnung("C:/Daten/Patrik/", this.dbController.ladeRennen(rennenID), club.getName(), alleFahrer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// TODO: aus DB laden
	/*
	 * String pfad = "C:/Daten/Patrik/"; Rennen rennen =
	 * dbController.getInstance().ladeRennen(2); List<String> tabellenname = new
	 * ArrayList<String>(); tabellenname.add("K1-Damen Benjamin");
	 * tabellenname.add("K1-Damen Schüler");
	 * tabellenname.add("K1-Herren Schüler"); List<List<String>> fahrerliste =
	 * new ArrayList<>(); List<String> fahrerZeile = new ArrayList<>();
	 * fahrerZeile.add("76"); fahrerZeile.add("");
	 * fahrerZeile.add("Huber Wayra"); fahrerZeile.add("20.00CHF");
	 * fahrerZeile.add(""); fahrerliste.add(fahrerZeile); fahrerZeile.add("78");
	 * fahrerZeile.add(""); fahrerZeile.add("Huber Nina");
	 * fahrerZeile.add("20.00CHF"); fahrerZeile.add("");
	 * fahrerliste.add(fahrerZeile); /*fahrerZeile.add("78");
	 * fahrerZeile.add(""); fahrerZeile.add("Huber Nina");
	 * fahrerZeile.add("20.00CHF"); fahrerZeile.add("");
	 * fahrerliste.add(fahrerZeile); try {
	 * PDFController.generierePdfRechnung(pfad, rennen, club.getName(),
	 * tabellenname, fahrerliste); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch
	 * (DocumentException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 */

	public void rechnungBezahlen(Club club, boolean bezahlt) {

	}

}