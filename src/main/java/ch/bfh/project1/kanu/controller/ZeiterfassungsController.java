package ch.bfh.project1.kanu.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Fahrer;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rennen;
import ch.bfh.project1.kanu.view.ZeiterfassungsView;

/**
 * Die Klasse ZeiterfassungsController beinhaltet die Logik der Klasse
 * ZeiterfassungsView.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class ZeiterfassungsController {
	// Membervariablen
	private DBController dbController;
	private ZeiterfassungsView zeiterfassungsView;

	// Konstanten
	private static final SimpleDateFormat DF = new SimpleDateFormat("mm:ss.SS");

	/**
	 * Konstruktor: ZeiterfassungsController;
	 */
	public ZeiterfassungsController() {
		this.dbController = DBController.getInstance();
	}

	/**
	 * Funktion liefert für die eingegebene Laufzeit die Milisekunden zurück.
	 * 
	 * @param laufzeit
	 * @return
	 */
	public int getLaufzeitInMilisekunden(String laufzeit) {
		Date date;
		try {
			date = DF.parse(laufzeit);
		} catch (ParseException e) {
			return 0;
		}
		return (int) date.getTime();
	}

	/**
	 * Funktion wandelt die Milisekunden in das gegeben Format.
	 * 
	 * @param milisekunden
	 * @return
	 */
	public String getLaufzeitInFormat(int milisekunden) {
		Date date = new Date(milisekunden);
		return (DF.format(date));
	}

	/**
	 * Funktion erfasst die Zet für einen Benutzer.
	 * 
	 * @param fahrer
	 * @param resultat
	 */
	public void zeitErfassen(Fahrer fahrer, FahrerResultat resultat) {
		this.dbController.speichereFahrer(fahrer, resultat);
	}

	/**
	 * Funktion ladet alle Fahrer mit der gegebenen Kategorie.
	 * 
	 * @param rennenID
	 * @param kategorieID
	 * @return
	 */
	public List<FahrerResultat> ladeAngemeldeteFahrerMitKategorie(Integer rennenID, Integer kategorieID) {
		return this.dbController.ladeStartliste(rennenID, kategorieID);
	}

	public FahrerResultat ladeAngemeldetenFahrer(Integer fahrerID, Integer kategorieID, Integer rennenID) {
		return this.dbController.ladeFahrerresultatByKategorie(fahrerID, kategorieID, rennenID);
	}

	/**
	 * Funktion ladet alle Fahrer mit der gegebenen Kategorie und einem
	 * Such-String.
	 * 
	 * @param rennenID
	 * @param kategorieID
	 * @param suche
	 * @return
	 */
	public List<FahrerResultat> ladeAngemeldeteFahrerMitKategorieMitSuche(Integer rennenID, Integer kategorieID,
			String suche) {
		return this.dbController.ladeStartlisteMitSuche(rennenID, kategorieID, suche);
	}

	/**
	 * Lädt alle Kategorien.
	 * 
	 * @return
	 */
	public List<AltersKategorie> ladeAlleKategorien() {
		return this.dbController.ladeKategorien();
	}

	/**
	 * Lädt alle Rennen.
	 * 
	 * @return
	 */
	public List<Rennen> ladeAlleRennen() {
		return this.dbController.ladeRennen();
	}
}
