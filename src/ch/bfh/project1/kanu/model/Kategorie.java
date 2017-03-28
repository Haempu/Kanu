package ch.bfh.project1.kanu.model;

public class Kategorie {
	/**
	 * Es gibt verschiedene Kategorien von Rennen.
	 *
	 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
	 * @date 28.03.2017
	 * @version 1.0
	 *
	 */
	
	private int kategorieID;
	private String name;
	private int attribut2; // TODO: was ist das?
	
	public int getKategorieID() {
		return kategorieID;
	}
	public void setKategorieID(int kategorieID) {
		this.kategorieID = kategorieID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAttribut2() {
		return attribut2;
	}
	public void setAttribut2(int attribut2) {
		this.attribut2 = attribut2;
	}
	
}
