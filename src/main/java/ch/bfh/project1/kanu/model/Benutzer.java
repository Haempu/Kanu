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
	public enum BenutzerRolle {
		ROLLE_STANDARD, ROLLE_TURNIERORGANISATOR, ROLLE_TORRICHTER, ROLLE_ZEITNEHMER, ROLLE_RECHNUNG, ROLLE_CLUBVERANTWORTLICHER
	}
	
	private Integer benutzerID;
	private String emailAdresse;
	private String passwort;
	private BenutzerRolle benutzerRolle;
	
	//TODO: nur für Test?
	public Benutzer(){
		
	}
	
	public Benutzer(Integer id, String email, String pw, BenutzerRolle br){
		this.benutzerID = id;
		this.emailAdresse = email;
		this.passwort = pw;
		this.benutzerRolle = br;
	}
	
	public Integer getBenutzerID() {
		return benutzerID;
	}

	public void setBenutzerID(Integer benutzerID) {
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