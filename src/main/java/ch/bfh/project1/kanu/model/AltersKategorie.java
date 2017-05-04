package ch.bfh.project1.kanu.model;

/**
 * Die Klasse AltersKategorie beinhaltet eine Kategorie mit ihrem Namen
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 28.03.2017
 * @version 1.0
 *
 */

public class AltersKategorie {

	private Integer altersKategorieID;
	private String name;

	public AltersKategorie(Integer altersKategorieID, String name) {
		this.altersKategorieID = altersKategorieID;
		this.name = name;
	}

	public int getAltersKategorieID() {
		return altersKategorieID;
	}

	public void setAltersKategorieID(Integer altersKategorieID) {
		this.altersKategorieID = altersKategorieID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
