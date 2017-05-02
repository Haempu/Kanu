package ch.bfh.project1.kanu.controller;

import ch.bfh.project1.kanu.model.Benutzer;
import ch.bfh.project1.kanu.view.BenutzerprofilView;

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
	private DBController dbController;
	private BenutzerprofilView benutzerProfilView;

	/**
	 * Hier kann die E-Mailadresse und oder das Passwort geändert werden
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
	 * @return
	 */
	public boolean benutzerprofilAendern(int benutzerID, String mail, String aktPW, String neuesPW,
			String wiederholtesPW) {
		Benutzer benutzer = dbController.ladeBenutzer(benutzerID);
		if (benutzer.getPasswort().equals(aktPW)) {
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
			dbController.speichereBenutzer(benutzer);
			return true;
		}
		return false;
	}
}