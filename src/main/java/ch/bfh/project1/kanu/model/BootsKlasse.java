package ch.bfh.project1.kanu.model;

/**
 * Die Klasse AltersKategorie beinhaltet eine Kategorie mit ihrem Namen
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 28.03.2017
 * @version 1.0
 *
 */

public class BootsKlasse {

	private int bootsKlasseID;
	private String name;

	public BootsKlasse(int bootsKlasseID, String name) {
		super();
		this.bootsKlasseID = bootsKlasseID;
		this.name = name;
	}

	public int getBootsKlasseID() {
		return bootsKlasseID;
	}

	public void setBootsKlasseID(int bootsKlasseID) {
		this.bootsKlasseID = bootsKlasseID;
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
