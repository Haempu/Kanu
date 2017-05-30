package ch.bfh.project1.kanu.controller;

import java.util.List;

import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Fahrer;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rennen;
import ch.bfh.project1.kanu.model.Strafzeit;
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

	public ZeiterfassungsController() {
		this.dbController = DBController.getInstance();
	}

	public void zeitErfassen(Fahrer fahrer, long zeit, int lauf, Strafzeit strafzeit) {

	}

	public List<FahrerResultat> ladeAngemeldeteFahrerMitKategorie(Integer rennenID, Integer kategorieID) {
		return this.dbController.ladeStartliste(rennenID, kategorieID);
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
