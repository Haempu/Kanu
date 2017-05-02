package ch.bfh.project1.kanu.model;

/**
 * Die Rolle des Benutzers wird in der Klasse BenutzerRolle definiert. Die
 * Applikation erlaubt die folgenden Benutzerrollen: - ROLLE_STANDARD -
 * ROLLE_TURNIERORGANISATOR - ROLLE_TORRICHTER - ROLLE_ZEITNEHMER -
 * ROLLE_RECHNUNG - ROLLE_CLUBVERANTWORTLICHER
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 28.03.2017
 * @version 1.0
 *
 */

public class BenutzerRolle {
	
	public enum Rolle {
		ROLLE_STANDARD, ROLLE_TURNIERORGANISATOR, ROLLE_TORRICHTER, ROLLE_ZEITNEHMER, ROLLE_RECHNUNG, ROLLE_CLUBVERANTWORTLICHER
	}
	
	private int rolleID;
	private String name;
	
	public int getRolleID() {
		return rolleID;
	}
	public void setRolleID(int rolleID) {
		this.rolleID = rolleID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
