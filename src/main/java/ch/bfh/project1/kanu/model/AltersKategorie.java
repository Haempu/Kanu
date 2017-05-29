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
	private Integer gebuehr;
	private Integer block, nr;

	public AltersKategorie(Integer altersKategorieID, String name) {
		this.altersKategorieID = altersKategorieID;
		this.name = name;
	}
	
	public AltersKategorie(Integer altersKategorieID, String name, Integer gebuehr) {
		this.altersKategorieID = altersKategorieID;
		this.name = name;
		this.gebuehr = gebuehr;
	}
	
	public AltersKategorie(Integer altersKategorieID, String name, Integer block, Integer nr) {
		this.altersKategorieID = altersKategorieID;
		this.name = name;
		this.block = block;
		this.nr = nr;
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

	public Integer getGebuehr() {
		return gebuehr;
	}

	public void setGebuehr(Integer gebuehr) {
		this.gebuehr = gebuehr;
	}

	public Integer getBlock()
	{
		return block;
	}

	public void setBlock(Integer block)
	{
		this.block = block;
	}

	public Integer getNr()
	{
		return nr;
	}

	public void setNr(Integer nr)
	{
		this.nr = nr;
	}
}
