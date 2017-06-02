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
	private DBController dbController;
	private RechnungsView rechnungsView;

	public RechnungsController() {
		this.dbController = DBController.getInstance();
	}

	/**
	 * Funktion lädt alle Clubs.
	 * 
	 */
	public ArrayList<Club> ladeAngemeldeteClubs() {
		return (ArrayList<Club>) this.dbController.ladeClubs();
	}

	/**
	 * Funktion erstellt die Rechnung eines Rennens für einen bestimmten Club.
	 * 
	 * @param club - Club, für welchen die Rechnung erstellt werden soll
	 * @param rennenID - Rennen, für welches die Rechnung erstellt werden soll
	 */
	public String rechnungErstellen(Club club, Integer rennenID) {
		// Lade alle zu einem Rennen angemeldeten Fahrer einres Clubs
		List<FahrerResultat> fahrer = this.dbController.ladeAngemeldetenFahrerByClub(club.getClubID(), rennenID);
		AltersKategorie aktuelleKat = null;
		List<AltersKategorie> angemeldeteKategorien = new ArrayList<AltersKategorie>();
		List<List<String>> alleFahrer = new ArrayList<List<String>>();
		// Jede Zeile der Späteren Tabelle auf der Rechnung muss aus einer Liste von Strings bestehen. Sämtliche Zeilen in eine Liste abfüllen und dem PDFController übergeben.
		for (int i = 0; i < fahrer.size(); i++) {
			List<String> zeile = new ArrayList<>();
			zeile.add(fahrer.get(i).getKategorie().getName());
			zeile.add(Integer.toString(fahrer.get(i).getStartnummer()));
			zeile.add(fahrer.get(i).getFahrer().getName() + " " + fahrer.get(i).getFahrer().getVorname());
			zeile.add(Integer.toString(fahrer.get(i).getKategorie().getGebuehr()));
			alleFahrer.add(zeile);
		}
		String pfad = "";
		try {
			pfad = PDFController.generierePdfRechnung("C:/Daten/Patrik/", this.dbController.ladeRennen(rennenID), club.getName(), alleFahrer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pfad;
	}

	/**
	 * Funktion schreibt in die DB, dass ein Club seine Rechnung schon bezahlt hat.
	 * @param club - Club, welcher die Rechnung bezahlt hat
	 * @param bezahlt - true wenn bezahlt, sonst false
	 */
	public void rechnungBezahlen(Club club, boolean bezahlt) {
		// TODO: In DB schreiben
	}

}