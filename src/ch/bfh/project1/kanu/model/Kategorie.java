package ch.bfh.project1.kanu.model;

/**
 * Die Klasse Kategorie beinhaltet eine Kategorie mit ihrem Namen
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 28.03.2017
 * @version 1.0
 *
 */

public class Kategorie {
	private int kategorieID;
	private String name;

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

}
