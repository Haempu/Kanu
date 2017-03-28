package ch.bfh.project1.kanu.model;

public class Fahrer {
	/**
	 * Fahrer sind Teilnehmer am Rennen. Jeder Fahrer muss registirert sein um am Rennen Teilnehmen zu können.
	 *
	 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
	 * @date 28.03.2017
	 * @version 1.0
	 *
	 */
	
	private int fahrerID;
	private Klub klub;
	private String name;
	private String vorname;
	private int jahrgang;
	private String telNr;
	private int plz;
	private String ort;
	
	
	public int getFahrerID() {
		return fahrerID;
	}
	public void setFahrerID(int fahrerID) {
		this.fahrerID = fahrerID;
	}
	public Klub getKlub() {
		return klub;
	}
	public void setKlub(Klub klub) {
		this.klub = klub;
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
