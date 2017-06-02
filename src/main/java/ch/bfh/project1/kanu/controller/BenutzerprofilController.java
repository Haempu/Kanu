package ch.bfh.project1.kanu.controller;

import ch.bfh.project1.kanu.model.Benutzer;

/**
 * Die Klasse BenutzerprofilController beinhaltet die Logik der Klasse
 * BenutzerprofilView.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class BenutzerprofilController {

	// Membervariablen
	private DBController dbController;

	/**
	 * Konstruktor: BenutzerprofilController
	 */
	public BenutzerprofilController() {
		this.dbController = DBController.getInstance();
	}

	/**
	 * Ändert die E-Mailadresse und/oder das Passwort des aktuell angemeldeten
	 * Benutzers
	 * 
	 * @param benutzerID
	 *            - ID des aktuell angemeldeten Benutzers
	 * @param mail
	 *            - E-Mail des aktuell angemeldeten Benutzers
	 * @param aktPW
	 *            - Aktuelles Passwort des aktuell angemeldeten Benutzers
	 * @param neuesPW
	 *            - Neues Passwort des aktuell angemeldeten Benutzers
	 * @param wiederholtesPW
	 *            - Wiederhohlung des neuen Passworts
	 * @return True wenn erfolgreich, False sonst.
	 */
	public boolean benutzerprofilAendern(Integer benutzerID, String mail, String aktPW, String neuesPW,
			String wiederholtesPW) {
		Benutzer benutzer = this.dbController.ladeBenutzer(benutzerID);
		if (benutzer.passwortVergleichen(aktPW)) {
			// Nur E-Mail soll geändert werden
			if (neuesPW.isEmpty() && wiederholtesPW.isEmpty()) {
				benutzer.setEmailAdresse(mail);
			}
			// PW soll (auch) geändert werden
			else if (neuesPW.equals(wiederholtesPW) && !neuesPW.isEmpty()) {
				benutzer.setEmailAdresse(mail);
				benutzer.setPasswort(neuesPW);
			}
			// Wenn neues PW nicht leer ist oder nicht bestätigt wurde ändert
			// nichts
			else {
				return false;
			}
			this.dbController.speichereBenutzer(benutzer);
			return true;
		}
		return false;
	}

	/**
	 * Funktion ändert die E-Mail Adresse.
	 * 
	 * @param benutzerID
	 * @param mail
	 */
	public void emailAendern(Integer benutzerID, String mail) {
		Benutzer benutzer = this.dbController.ladeBenutzer(benutzerID);
		benutzer.setEmailAdresse(mail);

		this.dbController.speichereBenutzer(benutzer);
	}
}