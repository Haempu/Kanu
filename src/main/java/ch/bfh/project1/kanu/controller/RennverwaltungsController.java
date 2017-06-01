package ch.bfh.project1.kanu.controller;

import java.util.List;

import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Club;
import ch.bfh.project1.kanu.model.Rennen;

/**
 * Die Klasse RennverwaltungsController beinhaltet die Logik der Klasse
 * RennverwaltungsView.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */
public class RennverwaltungsController {

	// Membervariablen
	private DBController db;

	/**
	 * Konstruktor: RennverwaltungsController
	 */
	public RennverwaltungsController() {
		db = DBController.getInstance();
	}

	/**
	 * Gibt die Kategorien zurück.
	 * 
	 * @return
	 */
	public List<AltersKategorie> ladeKategorien() {
		return db.ladeKategorien();
	}

	/**
	 * Lädt die Rennen, welche schon erfasst worden sind
	 * 
	 * @return Eine Liste mit Rennen
	 */
	public List<Rennen> ladeRennen() {
		return db.ladeRennen();
	}

	/**
	 * Lädt alle Clubs.
	 * 
	 * @return
	 */
	public List<Club> ladeClubs() {
		return db.ladeClubs();
	}

	/**
	 * Speichert das Rennen in der Datenbank
	 * 
	 * @param rennen
	 *            Das Rennen Objekt
	 * @return true wenn erfolgreich, false sonst
	 */
	public boolean speichereRennen(Rennen rennen) {
		return db.speichereRennen(rennen);
	}
}
