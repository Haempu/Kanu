package ch.bfh.project1.kanu.controller;

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
	private DBController dbController;
	private LoginView loginView;

	public boolean loginMitBenutzer(String email, String passwort) {
		return false;
	}

	public boolean existiertBenutzer(String email) {
		return false;
	}

	public boolean loginAufSession() {
		return false;
	}

	public void abmelden() {

	}
}
