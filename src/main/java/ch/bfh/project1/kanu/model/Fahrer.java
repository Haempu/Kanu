package ch.bfh.project1.kanu.model;

/**
 * Die Klasse Fahrer beinhaltet alle Informationen eines Fahrers.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class Fahrer {

	private Integer fahrerID;
	private Club club;
	private String name;
	private String vorname;
	private int jahrgang;
	private String telNr;
	private String strasse;
	private int plz;
	private String ort;

	public Integer getFahrerID() {
		return fahrerID;
	}

	public void setFahrerID(Integer fahrerID) {
		this.fahrerID = fahrerID;
	}

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public int getJahrgang() {
		return jahrgang;
	}

	public void setJahrgang(int jahrgang) {
		this.jahrgang = jahrgang;
	}

	public String getTelNr() {
		return telNr;
	}

	public void setTelNr(String telNr) {
		this.telNr = telNr;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public int getPlz() {
		return plz;
	}

	public void setPlz(int plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

}
