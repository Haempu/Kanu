package ch.bfh.project1.kanu.model;

public class Klub {
	/**
	 * Fahrer können nur über den Klub, bzw den Klubverantwortlichen, angemeldet werden.
	 *
	 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
	 * @date 28.03.2017
	 * @version 1.0
	 *
	 */
	
	private int klubID;
	private String kennung;
	private String name;
	
	public int getKlubID() {
		return klubID;
	}
	public void setKlubID(int klubID) {
		this.klubID = klubID;
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
}
