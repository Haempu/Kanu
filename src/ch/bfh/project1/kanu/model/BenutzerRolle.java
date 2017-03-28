package ch.bfh.project1.kanu.model;

public class BenutzerRolle {
	/**
	 * Die BenutzerRolle definiert auf was der Benuter alles Zugriff hat.
	 * Turnierorganisator:	           Kann das/die Rennen verwalten
	 * Torrichter:                     Kann die Strafzeiten für die Tore erfassen
	 * Zeitnehmer:                     Kann die Zeit für einen Lauf erfassen
	 * Chef Rechnungsbüro:             Kann Rechnungen stellen und die Posten für die Tore zuweisen
	 * Clubverantwortlicher:           Kann Spieler bearbeiten, anmelden, etc
	 * Standard Benutzer / Speaker:    Kann Rangliste / Startliste anschauen (keine Mutationsrechte)
	 *
	 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
	 * @date 28.03.2017
	 * @version 1.0
	 *
	 */
	private static Rolle BENUTZER_ROLLE;
	
	public enum Rolle {
		TURNIERORGANISATOR, TORRICHTER, ZEITNEHMER, CHEF_RECHNUNGSBUERO, CLUBVERANTWORTLICHER, STANDARDBENUTER
	}	
}
