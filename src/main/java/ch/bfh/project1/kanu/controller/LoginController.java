package ch.bfh.project1.kanu.controller;

import com.vaadin.server.VaadinSession;

import ch.bfh.project1.kanu.model.Benutzer;
import ch.bfh.project1.kanu.view.LoginView;

/**
 * Die Klasse BenutzerprofilController beinhaltet die Logik der Klasse
 * BenutzerprofilView.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class LoginController {

	// Membervariablen
	private LoginView loginView;
	private DBController dbController;
	private Benutzer aktuellerBenutzer;

	/**
	 * Konstruktor: LoginController
	 */
	public LoginController() {
		this.dbController = DBController.getInstance();
	}

	/**
	 * Funktion loggt einen Benutzer ein. Dieser wird auf der Session abgelegt.
	 * 
	 * @param email
	 * @param passwort
	 * @return
	 */
	public boolean loginMitBenutzer(String email, String passwort) {
		if (existiertBenutzer(email, passwort)) {
			SessionController.setBenutzerID(this.aktuellerBenutzer.getBenutzerID());
			SessionController.setBenuterEmail(this.aktuellerBenutzer.getEmailAdresse());
			SessionController.setBenutzer(aktuellerBenutzer);
			return true;
		}
		return false;
	}

	/**
	 * Funktion überprüft ob der Benutzer existiert und ob das Passwort
	 * übereinstimmt.
	 * 
	 * @param email
	 * @param passwort
	 * @return
	 */
	public boolean existiertBenutzer(String email, String passwort) {

		aktuellerBenutzer = dbController.ladeBenutzerMitEmail(email);

		if (aktuellerBenutzer != null) {
			return aktuellerBenutzer.passwortVergleichen(passwort);
		}
		return false;
	}

	/**
	 * Funktion gibt zurück ob ein Benutzer auf der Session eingeloggt ist oder
	 * nicht.
	 * 
	 * @return
	 */
	public boolean loginAufSession() {
		if (VaadinSession.getCurrent() != null && SessionController.getBenutzerID() != null) {
			return true;
		}
		return false;
	}

	/**
	 * Funktion meldet einen Benutzer ab.
	 */
	public void abmelden() {
		SessionController.benutzerAbmelden();
	}
}
