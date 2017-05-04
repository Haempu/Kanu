package ch.bfh.project1.kanu.controller;

import com.vaadin.server.VaadinSession;

public class SessionController {

	public static final String SESSION_BENUTZER_ID = "benutzerid";
	public static final String SESSION_BENUTZERNAME = "benutzername";
	public static final String SESSION_BENUTZER_ROLLE = "benutzerrolle";

	public static void setBenutzerID(Integer benutzerID) {
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_ID, benutzerID);
	}

	public static void setBenutzername(String benutzername) {
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZERNAME, benutzername);
	}

	public static void setBenutzerRolle(String benutzerrolle) {
		VaadinSession.getCurrent().setAttribute(SESSION_BENUTZER_ROLLE, benutzerrolle);
	}

	public static Integer getBenutzerID() {
		return (Integer) VaadinSession.getCurrent().getAttribute(SESSION_BENUTZER_ID);
	}

	public static String getBenutzername() {
		return (String) VaadinSession.getCurrent().getAttribute(SESSION_BENUTZERNAME);
	}

	public static String getBenutzerRolle() {
		return (String) VaadinSession.getCurrent().getAttribute(SESSION_BENUTZER_ROLLE);
	}
}
