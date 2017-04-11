package ch.bfh.project1.kanu.model;

/**
 * Ein „Club“ ist ein Kanu-Club, mit all seinen Fahrern. Ein Club beinhaltet eine Kennung, einen Namen, einen Clubverantwortlichen und eine Adresse.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 28.03.2017
 * @version 1.0
 *
 */

public class Club {
	
	private int clubID;
	private String kennung;
	private String name;
	private Clubverantwortlicher clubverantwortlicher;
	private String strasse;
	private int plz;
	private String ort;
	
	public int getClubID() {
		return clubID;
	}
	public void setClubID(int clubID) {
		this.clubID = clubID;
	}
	public String getKennung() {
		return kennung;
	}
	public void setKennung(String kennung) {
		this.kennung = kennung;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Clubverantwortlicher getClubverantwortlicher() {
		return clubverantwortlicher;
	}
	public void setClubverantwortlicher(Clubverantwortlicher clubverantwortlicher) {
		this.clubverantwortlicher = clubverantwortlicher;
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
