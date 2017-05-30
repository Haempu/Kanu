package ch.bfh.project1.kanu.controller;

import java.util.List;

import ch.bfh.project1.kanu.model.FahrerResultat;
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
	 * Wird immer aufgerufen, wenn die erfassten Fehler bestätigt wurden.
	 * 
	 * @param fahrerID
	 *            - ID des aktuellen Fahrers
	 * @param rennenID
	 *            - ID des aktuellen Rennens
	 * @param tornummer
	 *            - Tornummer, bei welchem der Fehler gemacht wurde.
	 */
	public void fehlerErfassen(Integer fahrerID, Integer rennenID, int tornummer) {
		this.dbController.fehlerErfassen(fahrerID, rennenID, tornummer, tornummer, tornummer, tornummer); //TODO
	}
}
