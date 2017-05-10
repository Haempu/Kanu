package ch.bfh.project1.kanu.controller;

import com.vaadin.server.VaadinSession;

public class SessionController {

	public static final String SESSION_BENUTZER_ID = "benutzerid";
	public static final String SESSION_EMAIL = "benutzeremail";
	public static final String SESSION_BENUTZER_ROLLE = "benutzerrolle";

	public static void setBenutzerID(Integer benutzerID) {
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_ID, benutzerID);
	}

	public static void setBenuterEmail(String benutzeremail) {
		VaadinSession.getCurrent().setAttribute(SESSION_EMAIL, benutzeremail);
	}

	public static void setBenutzerRolle(String benutzerrolle) {
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_ROLLE, benutzerrolle);
	}

	public static Integer getBenutzerID() {
		return (Integer) VaadinSession.getCurrent().getAttribute(SESSION_BENUTZER_ID);
	}

	public static String getBenutzerEmail() {
		return (String) VaadinSession.getCurrent().getAttribute(SESSION_EMAIL);
	}

	public static String getBenutzerRolle() {
		return (String) VaadinSession.getCurrent().getAttribute(SESSION_BENUTZER_ROLLE);
	}

	public static void benutzerAbmelden() {
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_ID, null);
		VaadinSession.getCurrent().setAttribute(SESSION_EMAIL, null);
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_ROLLE, null);
	}
}
