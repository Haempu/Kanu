package ch.bfh.project1.kanu.controller;

import java.util.ArrayList;
import java.util.List;

import ch.bfh.project1.kanu.model.Club;
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

	// Membervariablen
	private DBController dbController;
	private MutationsView mutationsView;

	/**
	 * Konstruktor: MutationsController
	 */
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
		return (ArrayList<Fahrer>) this.dbController.ladeFahrermutationslisteAlle();
	}

	/**
	 * Lädt die Liste für die Fahrermutationstabelle. Lädt Fahrer von allen
	 * Clubs.
	 * 
	 * @return Liste von allen Fahrern die am Rennen teilgenommen haben.
	 */
	public ArrayList<Fahrer> ladeFahrermutationslisteAlleMitSuche(String suche) {
		return (ArrayList<Fahrer>) this.dbController.ladeFahrermutationslisteAlleMitSuche(suche);
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
	public List<Fahrer> ladeFahrermutationslisteClub(Integer clubID) {
		return this.dbController.ladeFahrermutationslisteClub(clubID);
	}

	/**
	 * Lädt die Resultate des ausgewählten Fahrers.
	 * 
	 * @param fahrerID
	 *            - ID des ausgewählten Fahrers.
	 * @return Resultate des ausgewählten Fahrers.
	 */
	public List<FahrerResultat> ladeFahrerresultat(Integer fahrerID) {
		return this.dbController.ladeFahrerresultat(fahrerID);
	}

	/**
	 * Lädt alle Clubs
	 * 
	 * @return
	 */
	public List<Club> ladeAlleClubs() {
		return this.dbController.ladeClubs();
	}

	/**
	 * Lädt einen Fahrer mit der FahrerID
	 * 
	 * @param fahrerID
	 * @return
	 */
	public Fahrer ladeFahrer(Integer fahrerID) {
		return this.dbController.ladeFahrer(fahrerID);
	}

	/**
	 * Speichert die bearbeiteten Daten sowie die bearbeiteten Resultate des
	 * ausgewählten Fahrers.
	 * 
	 * @param fahrer
	 *            - Ausgewählter Fahrer.
	 * @param fahrerResultat
	 *            - Resultate des ausgewählten Fahrers.
	 * @return fahrerID
	 */
	public void speichereFahrerBearbeitenAlle(Fahrer fahrer) {
		this.dbController.speichereFahrer(fahrer);
	}

	/**
	 * Speichert einen neuen Fahrer
	 * 
	 * @param fahrer
	 * @return
	 */
	public Integer speichereNeuenFahrer(Fahrer fahrer) {
		return this.dbController.speichereNeuenFahrer(fahrer);
	}

	/**
	 * Löscht einen Fahrer
	 * 
	 * @param fahrerID
	 */
	public void fahrerLoeschen(Integer fahrerID) {
		this.dbController.fahrerLoeschen(fahrerID);
	}

	/**
	 * Speichert die bearbeiteten Daten des ausgwählten Fahrers.
	 * 
	 * @param fahrer
	 *            - Ausgewählter Fahrer.
	 */
	public void speichereFahrerBearbeitenClub(Fahrer fahrer) {
		this.dbController.speichereFahrer(fahrer);
	}
}
