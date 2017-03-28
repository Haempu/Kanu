package ch.bfh.project1.kanu.model;

/**
 * Ein Benutzer ist ein registrierter Benuter des Systems. Er registriert sich mit folgenden Attributen:
 * - Emailadresse - Passwort
 * TODO: Wer bestimmt die Benutzerrolle? Turnierorganisator?
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 28.03.2017
 * @version 1.0
 *
 */

public class Benutzer {
	private int benutzerID;
	private String emailAdresse;
	private String passwort;
	private BenutzerRolle benutzerRolle;
	
	public int getBenutzerID() {
		return benutzerID;
	}
	public void setBenutzerID(int benutzerID) {
		benutzerID = benutzerID;
	}
	public String getEmailAdresse() {
		return emailAdresse;
	}
	public void setEmailAdresse(String emailAdresse) {
		this.emailAdresse = emailAdresse;
	}
	public String getPasswort() {
		return passwort;
	}
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
	public BenutzerRolle getBenutzerRolle() {
		return benutzerRolle;
	}
	public void setBenutzerRolle(BenutzerRolle benutzerRolle) {
		this.benutzerRolle = benutzerRolle;
	}
}