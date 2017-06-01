package ch.bfh.project1.kanu.controller;

import java.util.List;

import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rennen;
import ch.bfh.project1.kanu.model.Strafzeit;
import ch.bfh.project1.kanu.view.FehlererfassungsView;

/**
 * Die Klasse FehlererfassungsController beinhaltet die Logik der Klasse
 * FehlererfassungsView.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class FehlererfassungsController {
	private DBController dbController;
	private FehlererfassungsView fehlererfassungsview;

	/**
	 * Konstruktor: FehlererfassungsController
	 */
	public FehlererfassungsController() {
		this.dbController = DBController.getInstance();
	}

	/**
	 * Lädt alle Fahrer eines Rennens.
	 * 
	 * @param rennenID
	 *            - ID des Rennens
	 * @return Liste von allen Fahrern eines Rennens.
	 */
	public List<FahrerResultat> ladeFehlererfassung(Integer rennenID) {
		return this.dbController.ladeStartliste(rennenID);
	}

	/**
	 * Erfasst einen Fehler und berechnet die Strafzeit.
	 * 
	 * @param fahrerID
	 * @param kategorieID
	 * @param rennenID
	 * @param tornummer
	 * @param lauf
	 * @param beruehrt
	 * @param verfehlt
	 */
	public void fehlerErfassen(Integer fahrerID, Integer kategorieID, Integer rennenID, int tornummer, int lauf,
			boolean beruehrt, boolean verfehlt) {
		Integer strafzeit = 0;
		if (beruehrt) {
			strafzeit += Strafzeit.STRAFZEIT_BERUEHRT;
		}
		if (verfehlt) {
			strafzeit += Strafzeit.STRAFZEIT_VERPASST;
		}

		if (strafzeit == 0) {
			strafzeit = null;
		}

		this.dbController.fehlerErfassen(fahrerID, rennenID, kategorieID, lauf, tornummer, strafzeit);
	}

	/**
	 * Funktion lädt alle Rennen.
	 * 
	 * @return
	 */
	public List<Rennen> ladeRennen() {
		return dbController.ladeRennen();
	}

	/**
	 * Funktion ladet alle angemeldeten Fahrer.
	 * 
	 * @param rennID
	 * @param kategorieID
	 * @return
	 */
	public List<FahrerResultat> ladeFahrerliste(Integer rennID, Integer kategorieID) {
		return dbController.ladeStartliste(rennID, kategorieID);
	}

	/**
	 * Funktion ladet alle angemeldeten Fahrer mit einem Such-String.
	 * 
	 * @param rennID
	 * @param kategorieID
	 * @param suche
	 * @return
	 */
	public List<FahrerResultat> ladeFahrerlistMitSuche(Integer rennID, Integer kategorieID, String suche) {
		return dbController.ladeStartlisteMitSuche(rennID, kategorieID, suche);
	}

	/**
	 * Funktion ladet alle erfassten Strafzeiten.
	 * 
	 * @param fahrerID
	 * @param rennenID
	 * @param kategorieID
	 * @param lauf
	 * @return
	 */
	public List<Strafzeit> ladeStrafzeitliste(Integer fahrerID, Integer rennenID, Integer kategorieID, int lauf) {
		return dbController.ladeStrafzeit(fahrerID, rennenID, kategorieID, lauf);
	}

}
