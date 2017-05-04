
package ch.bfh.project1.kanu.controller;

import java.util.List;

import ch.bfh.project1.kanu.model.Fahrer;
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

	/**
	 * Gibt eine Liste zurück, welche alle Fahrer eines Clubs mit den Attributen
	 * "Angemeldet", "Vorname", "Nachnahme" und "Jahrgang" enthält, sowie die
	 * Auswahlmöglichkeiten zur Bootsklasse und der Alterskategorie.
	 * 
	 * @param clubID
	 *            - ID eines Clubs
	 * @return
	 */
	public List<String> ladeFahreranmeldungslisteClub(Integer clubID) {
		return dbController.ladeFahreranmeldungslisteClub(clubID);
	}

	/**
	 * Meldet einen Fahrer zum Rennen an.
	 * 
	 * @param fahrerID
	 *            - ID eines Fahrers.
	 */
	public void fahrerAnmelden(Integer fahrerID, Integer rennenID, Integer bootsKlasseID, Integer alterskategorieID) {
		dbController.fahrerAnmelden(fahrerID, rennenID, bootsKlasseID, alterskategorieID);
	}

	/**
	 * Meldet einen Fahrer vom Rennen ab.
	 * 
	 * @param fahrerID
	 *            - ID eines Fahrers.
	 */
	public void fahrerAbmelden(Integer fahrerID, Integer rennenID, Integer bootsKlasseID, Integer alterskategorieID) {
		dbController.fahrerAbmelden(fahrerID, rennenID, bootsKlasseID, alterskategorieID);
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
		return dbController.ladeFahrer(fahrerID);
	}

	/**
	 * Speichert den bearbeiteten Fahrer.
	 * 
	 * @param fahrer
	 *            - ID eines Fahrers.
	 */
	public void speichereFahrer(Fahrer fahrer) {
		dbController.speichereFahrer(fahrer);
	}
}