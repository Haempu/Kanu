package ch.bfh.project1.kanu.controller;

import java.util.ArrayList;

import ch.bfh.project1.kanu.model.Fahrer;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.view.MutationsView;

/**
 * Die Klasse MutationsController beinhaltet die Logik der Klasse MutationsView.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class MutationsController {
	private DBController dbController;
	private MutationsView mutationsView;

	public MutationsController() {
		this.dbController = DBController.getInstance();
	}

	/**
	 * Lädt die Liste für die Fahrermutationstabelle. Lädt Fahrer von allen
	 * Clubs.
	 * 
	 * @return Liste von allen Fahrern die am Rennen teilgenommen haben.
	 */
	public ArrayList<Fahrer> ladeFahrermutationslisteAlle() {
		return this.dbController.ladeFahrermutationslisteAlle();
	}

	/**
	 * Lädt die Liste für die Fahrermutationstabelle. Lädt alle Fahrer des Clubs
	 * vom angemeldeten Benutzer.
	 * 
	 * @param clubID
	 *            - ID des Clubs vom angemeldeten Benutzer.
	 * @return Liste von allen Fahreren des Clubs, die am Rennen teilgenommen
	 *         haben.
	 */
	public ArrayList<Fahrer> ladeFahrermutationslisteClub(Integer clubID) {
		return this.dbController.ladeFahrermutationslisteClub(clubID);
	}

	/**
	 * Lädt die Resultate des ausgewählten Fahrers.
	 * 
	 * @param fahrerID
	 *            - ID des ausgewählten Fahrers.
	 * @return Resultate des ausgewählten Fahrers.
	 */
	public FahrerResultat ladeFahrerresultat(Integer fahrerID) {
		return this.dbController.ladeFahrerresultat(fahrerID);
	}

	/**
	 * Speichert die bearbeiteten Daten sowie die bearbeiteten Resultate des
	 * ausgewählten Fahrers.
	 * 
	 * @param fahrer
	 *            - Ausgewählter Fahrer.
	 * @param fahrerResultat
	 *            - Resultate des ausgewählten Fahrers.
	 */
	public void speichereFahrerBearbeitenAlle(Fahrer fahrer) {
		this.dbController.speichereFahrerBearbeitenAlle(fahrer);
	}

	/**
	 * Speichert die bearbeiteten Daten des ausgwählten Fahrers.
	 * 
	 * @param fahrer
	 *            - Ausgewählter Fahrer.
	 */
	public void speichereFahrerBearbeitenClub(Fahrer fahrer) {
		this.dbController.speichereFahrerBearbeitenClub(fahrer);
	}
}
