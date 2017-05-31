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
	 * Lädt alle Fahrer eines Rennens.
	 * 
	 * @param rennenID
	 *            - ID des Rennens
	 * @return Liste von allen Fahrern eines Rennens.
	 */
	public List<FahrerResultat> ladeFehlererfassung(Integer rennenID) {
		return this.dbController.ladeStartliste(rennenID);
	}
	
	public FehlererfassungsController()
	{
		dbController = DBController.getInstance();
	}

	/**
	 * Lädt alle Rennen.
	 * 
	 */
	public void fehlerErfassen(Integer fahrerID, Integer rennenID, int tornummer) {
		this.dbController.fehlerErfassen(fahrerID, rennenID, tornummer, tornummer, tornummer, tornummer); //TODO
	}
	public List<Rennen> ladeRennen() {
		return dbController.ladeRennen();
	}
	
	public List<FahrerResultat> ladeFahrerliste(Integer rennID, Integer kategorieID) {
		return dbController.ladeStartliste(rennID, kategorieID);
	}
	
	public List<Strafzeit> ladeStrafzeitliste(Integer fahrerID, Integer rennenID, Integer kategorieID, int lauf) {
		return dbController.ladeStrafzeit(fahrerID, rennenID, kategorieID, lauf);
	}

}
