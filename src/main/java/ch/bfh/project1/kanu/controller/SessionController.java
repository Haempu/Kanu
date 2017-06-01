package ch.bfh.project1.kanu.controller;

import com.vaadin.server.VaadinSession;

import ch.bfh.project1.kanu.model.Benutzer;

/**
 * Der SessionController ist für das Schreiben und Lesen auf die Session
 * zuständig.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */
public class SessionController {

	// Konstanten
	public static final String SESSION_BENUTZER_ID = "benutzerid";
	public static final String SESSION_EMAIL = "benutzeremail";
	public static final String SESSION_BENUTZER_OBJECT = "benutzerobjekt";

	/**
	 * Funktion setzt BenutzerID auf die Session
	 * 
	 * @param benutzerID
	 */
	public static void setBenutzerID(Integer benutzerID) {
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_ID, benutzerID);
	}

	/**
	 * Funktion setzt die Email Adresse des benutzers auf die Session
	 * 
	 * @param benutzeremail
	 */
	public static void setBenuterEmail(String benutzeremail) {
		VaadinSession.getCurrent().setAttribute(SESSION_EMAIL, benutzeremail);
	}

	/**
	 * Funktion setzt einen Benutzer auf die Session
	 * 
	 * @param benutzer
	 */
	public static void setBenutzer(Benutzer benutzer) {
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_OBJECT, benutzer);
	}

	/**
	 * Funktion gibt den angemeldeten benutzer als Objekt zurück.
	 * 
	 * @return
	 */
	public static Benutzer getBenutzer() {
		return (Benutzer) VaadinSession.getCurrent().getAttribute(SESSION_BENUTZER_OBJECT);
	}

	/**
	 * Funktion gibt die Benutzer ID des angemeldeten Benutzers zurück.
	 * 
	 * @return
	 */
	public static Integer getBenutzerID() {
		return (Integer) VaadinSession.getCurrent().getAttribute(SESSION_BENUTZER_ID);
	}

	/**
	 * Funktion gibt die Email Adresse des angemeldeten Benutzers zurück.
	 *
	 * @return
	 */
	public static String getBenutzerEmail() {
		return (String) VaadinSession.getCurrent().getAttribute(SESSION_EMAIL);
	}

	/**
	 * Funktion meldet ein Benutzer von der Session ab.
	 */
	public static void benutzerAbmelden() {
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_ID, null);
		VaadinSession.getCurrent().setAttribute(SESSION_EMAIL, null);
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_OBJECT, null);
	}
}
