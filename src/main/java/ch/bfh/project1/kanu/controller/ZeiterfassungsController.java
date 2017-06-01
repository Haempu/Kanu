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
	private DBController dbController;
	private ZeiterfassungsView zeiterfassungsView;
	private static final SimpleDateFormat df = new SimpleDateFormat("mm:ss.SS");

	public ZeiterfassungsController() {
		this.dbController = DBController.getInstance();
	}

	public int getLaufzeitInMilisekunden(String laufzeit) {
		Date date;
		try {
			date = df.parse(laufzeit);
		} catch (ParseException e) {
			return 0;
		}
		System.out.println("Time: " + (int) date.getTime());
		return (int) date.getTime();
	}

	public String getLaufzeitInFormat(int milisekunden) {
		Date date = new Date(milisekunden);
		return (df.format(date));
	}

	public void zeitErfassen(Fahrer fahrer, FahrerResultat resultat) {
		this.dbController.speichereFahrer(fahrer, resultat);
	}

	public List<FahrerResultat> ladeAngemeldeteFahrerMitKategorie(Integer rennenID, Integer kategorieID) {
		return this.dbController.ladeStartliste(rennenID, kategorieID);
	}

	public FahrerResultat ladeAngemeldetenFahrer(Integer fahrerID, Integer kategorieID, Integer rennenID) {
		return this.dbController.ladeFahrerresultatByKategorie(fahrerID, kategorieID, rennenID);
	}

	public List<FahrerResultat> ladeAngemeldeteFahrerMitKategorieMitSuche(Integer rennenID, Integer kategorieID,
			String suche) {
		return this.dbController.ladeStartlisteMitSuche(rennenID, kategorieID, suche);
	}

	public List<AltersKategorie> ladeAlleKategorien() {
		return this.dbController.ladeKategorien();
	}

	public List<Rennen> ladeAlleRennen() {
		return this.dbController.ladeRennen();
	}
}
