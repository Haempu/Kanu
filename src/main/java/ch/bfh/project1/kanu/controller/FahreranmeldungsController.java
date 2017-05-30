
package ch.bfh.project1.kanu.controller;

import java.util.List;

import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Fahrer;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rennen;
import ch.bfh.project1.kanu.view.FahreranmeldungsView;

/**
 * Die Klasse FahreranmeldungsController beinhaltet die Logik der Klasse
 * FahreranmeldungssView.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class FahreranmeldungsController {
	private DBController dbController;
	private FahreranmeldungsView fahreranmeldungsView;

	public FahreranmeldungsController() {
		this.dbController = DBController.getInstance();
	}

	/**
	 * Gibt eine Liste zurück, welche alle Fahrer eines Clubs mit den Attributen
	 * "Angemeldet", "Vorname", "Nachnahme" und "Jahrgang" enthält, sowie die
	 * Auswahlmöglichkeiten zur Bootsklasse und der Alterskategorie.
	 * 
	 * @param clubID
	 *            - ID eines Clubs
	 * @return
	 */
	public List<Fahrer> ladeFahrerlisteClub(int clubID) {
		return this.dbController.fahrerlisteClub(clubID);
	}

	/**
	 * Meldet einen Fahrer zum Rennen an.
	 * 
	 * @param fahrerID
	 *            - ID eines Fahrers.
	 */
	public void fahrerAnmelden(Integer fahrerID, Integer rennenID, Integer alterskategorieID) {
		this.dbController.fahrerAnmelden(fahrerID, rennenID, alterskategorieID);
	}

	/**
	 * Meldet einen Fahrer vom Rennen ab.
	 * 
	 * @param fahrerID
	 *            - ID eines Fahrers.
	 */
	public void fahrerAbmelden(Integer fahrerID, Integer rennenID, Integer alterskategorieID) {
		this.dbController.fahrerAbmelden(fahrerID, rennenID, alterskategorieID);
	}

	/**
	 * Lädt den zu bearbeitenden Fahrer.
	 * 
	 * @param fahrerID
	 *            - ID eines Fahrers.
	 * @return
	 */
	public Fahrer ladeFahrerVerwalten(Integer fahrerID) {
		// TODO: Was wenn Fahrer nicht vorhanden? Exception von DBController?
		return this.dbController.ladeFahrer(fahrerID);
	}

	public List<Fahrer> ladeAlleFahrer() {
		return this.dbController.ladeFahrermutationslisteAlle();

	}

	public List<Fahrer> ladeFahrerMitSuche(String suche) {
		return this.dbController.ladeFahrermutationslisteAlleMitSuche(suche);
	}

	public List<AltersKategorie> ladeAlleKategorien() {
		return this.dbController.ladeKategorien();
	}

	public List<Rennen> ladeAlleRennen() {
		return this.dbController.ladeRennen();
	}

	public List<FahrerResultat> ladeAngemeldeteFahrer(Integer rennenID) {
		return this.dbController.ladeStartliste(rennenID);
	}

	public void neueKategorie(Integer fahrerID, Integer alteKategorieID, Integer neueKategorieID, Integer rennenID) {
		this.dbController.setzeNeueKategorie(fahrerID, alteKategorieID, neueKategorieID, rennenID);
	}

	/**
	 * Speichert den bearbeiteten Fahrer.
	 * 
	 * @param fahrer
	 *            - ID eines Fahrers.
	 */
	public void speichereFahrer(Fahrer fahrer) {
		this.dbController.speichereFahrer(fahrer);
	}
}