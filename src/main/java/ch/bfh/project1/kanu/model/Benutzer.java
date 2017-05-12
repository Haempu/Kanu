package ch.bfh.project1.kanu.model;

import ch.bfh.project1.kanu.util.PasswordAuthentication;

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
	
	public enum BenutzerRolle {
		ROLLE_STANDARD(0),
		ROLLE_TURNIERORGANISATOR(1),
		ROLLE_TORRICHTER(2),
		ROLLE_ZEITNEHMER(4),
		ROLLE_RECHNUNG(8),
		ROLLE_CLUBVERANTWORTLICHER(16);
		
		private final Integer value;
		
		BenutzerRolle(Integer value) {
			this.value = value;
		}
		
		public Integer value() {
			return value;
		}
	}
	
	public Benutzer(Integer benutzerID, Integer clubID, String emailAdresse, String passwort, Integer benutzerRechte)
	{
		this.benutzerID = benutzerID;
		this.emailAdresse = emailAdresse;
		this.passwort = passwort;
		this.benutzerRechte = benutzerRechte;
		this.setClubID(clubID);
	}

	public int getBenutzerID() {
		return benutzerID;
	}

	public void setBenutzerID(int benutzerID) {
		this.benutzerID = benutzerID;
	}

	public String getEmailAdresse() {
		return emailAdresse;
	}

	public void setEmailAdresse(String emailAdresse) {
		this.emailAdresse = emailAdresse;
	}

	/**
	 * Gibt das gehashte Passwort zurück
	 * @return
	 */
	public String getPasswort() {
		return passwort;
	}

	/**
	 * <strong>Plaintext</strong> Passwort, das Passwort wird hier gehasht! Um den Hash beim Benutzer zu speichern den Konstruktor benutzen!
	 * @param passwort <strong>Plaintext</strong> Passwort
	 */
	public void setPasswort(String passwort) {
		PasswordAuthentication pa = new PasswordAuthentication();
		this.passwort = pa.hash(passwort.toCharArray());
	}
	
	public Integer getClubID() {
		return clubID;
	}

	public void setClubID(Integer clubID) {
		this.clubID = clubID;
	}
	
	/**
	 * Vergleicht das Passwort mit dem Hash
	 * @param hash Der Hash des Passworts (von der db)
	 * @return true wenn richtiges Passwort, false sonst
	 */
	public boolean passwortVergleichen(String passwort)
	{
		PasswordAuthentication pa = new PasswordAuthentication();
		return pa.authenticate(passwort.toCharArray(), this.passwort);
	}

	/**
	 * Überprüft den Benutzer auf das angegebene Recht.
	 * @param br Das zu überprüfende Recht
	 * @return true wenn der Benutzer das Recht hat, false sonst
	 */
	public boolean hatRechte(BenutzerRolle br)
	{
		return (benutzerRechte & br.value()) == br.value();
	}
	
	/**
	 * Gibt die Rechte als Zahl zurück (zB. zum Speichern in die db)
	 * @return Die Rechte als Integer Zahl
	 */
	public Integer getRechte()
	{
		return benutzerRechte;
	}
	
	/**
	 * Fügt ein Recht zu den bestehenden Rechten hinzu
	 * @param br Das hinzuzufügende Recht
	 */
	public void setzeRechte(BenutzerRolle br)
	{
		this.benutzerRechte |= br.value();
	}
	
	/**
	 * Löscht ein angegebenes Recht des Benutzers
	 * @param br Das Recht, welches zu löschen ist
	 */
	public void loescheRechte(BenutzerRolle br)
	{
		this.benutzerRechte &= ~br.value();
	}
	
	/**
	 * Löscht alle Rechte des Benutzers
	 */
	public void loescheRechte()
	{
		this.benutzerRechte = 0;
	}
}