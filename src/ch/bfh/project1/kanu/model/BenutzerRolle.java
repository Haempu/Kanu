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
	private static Rolle BENUTZER_ROLLE;

	public enum Rolle {
		ROLLE_STANDARD, ROLLE_TURNIERORGANISATOR, ROLLE_TORRICHTER, ROLLE_ZEITNEHMER, ROLLE_RECHNUNG, ROLLE_CLUBVERANTWORTLICHER
	}
}
