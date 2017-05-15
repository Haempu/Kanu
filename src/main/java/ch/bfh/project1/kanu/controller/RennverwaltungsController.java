package ch.bfh.project1.kanu.controller;

import java.util.List;

import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Rennen;

public class RennverwaltungsController {
	
	private DBController db;
	
	public RennverwaltungsController() {
		db = DBController.getInstance();
	}

	/**
	 * Gibt die Kategorien zurück
	 * @return
	 */
	public List<AltersKategorie> ladeKategorien()
	{
		return db.ladeKategorien();
	}
	
	/**
	 * Speichert das Rennen in der Datenbank
	 * @param rennen Das Rennen Objekt
	 * @return true wenn erfolgreich, false sonst
	 */
	public boolean speichereRennen(Rennen rennen)
	{
		return false;
	}
}
