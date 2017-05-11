package ch.bfh.project1.kanu.controller;

import com.vaadin.server.VaadinSession;

import ch.bfh.project1.kanu.model.Benutzer;

public class SessionController {

	public static final String SESSION_BENUTZER_ID = "benutzerid";
	public static final String SESSION_EMAIL = "benutzeremail";
	public static final String SESSION_BENUTZER_OBJECT = "benutzerobjekt";

	public static void setBenutzerID(Integer benutzerID) {
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_ID, benutzerID);
	}

	/**
	 * gestl: Brauchen wir nicht mehr, oder?
	 * Ich würde hier einfach das Benutzer Objekt speichern, geht im gleichen Zug und man hat direkt alle Daten, die man braucht.
	 * //TODO
	 * @param benutzeremail
	 */
	@Deprecated
	public static void setBenuterEmail(String benutzeremail) {
		VaadinSession.getCurrent().setAttribute(SESSION_EMAIL, benutzeremail);
	}
	
	public static void setBenutzer(Benutzer benutzer)
	{
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_OBJECT, benutzer);
	}
	
	public static Benutzer getBenutzer()
	{
		return (Benutzer) VaadinSession.getCurrent().getAttribute(SESSION_BENUTZER_OBJECT);
	}

	public static Integer getBenutzerID() {
		return (Integer) VaadinSession.getCurrent().getAttribute(SESSION_BENUTZER_ID);
	}

	public static String getBenutzerEmail() {
		return (String) VaadinSession.getCurrent().getAttribute(SESSION_EMAIL);
	}

	public static void benutzerAbmelden() {
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_ID, null);
		VaadinSession.getCurrent().setAttribute(SESSION_EMAIL, null);
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_OBJECT, null);
	}
}
