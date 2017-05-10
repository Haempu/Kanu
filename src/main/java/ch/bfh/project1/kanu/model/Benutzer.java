package ch.bfh.project1.kanu.model;

/**
 * Die Klasse Benutzer ist ein Benutzer des Systems. Ein Benutzer hat folgende
 * Attribute: - Eindeutige Identifikationsnummer - Email-Adresse - Passwort -
 * Benutzerrolle
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 28.03.2017
 * @version 1.0
 *
 */

public class Benutzer {
	private Integer benutzerID;
	private String emailAdresse;
	private String passwort;
	private Integer benutzerRechte;
	private Integer clubID;
	
	public Benutzer(Integer benutzerID, Integer clubID, String emailAdresse, String passwort, Integer benutzerRechte)
	{
		this.benutzerID = benutzerID;
		this.emailAdresse = emailAdresse;
		this.passwort = passwort;
		this.benutzerRechte = benutzerRechte;
		this.clubID = clubID;
	}
	
	public Benutzer()
	{
		
	}

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
}