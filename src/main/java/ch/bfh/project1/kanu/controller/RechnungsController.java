package ch.bfh.project1.kanu.controller;

import java.util.List;

import ch.bfh.project1.kanu.model.Club;
import ch.bfh.project1.kanu.view.RechnungsView;

/**
 * Die Klasse RechnungsController beinhaltet die Logik der Klasse RecnungsView.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class RechnungsController {
	private DBController dbController;
	private RechnungsView rechnungsView;

	/**
	 * Lädt alle Klubs, die mindestens einen Fahrer an das Rennen angemeldet
	 * haben.
	 */
	public List<String> ladeAngemeldeteClubs() {
		return dbController.ladeAngemeldeteClubs();
	}

	public void rechnungErstellen(Club club) {

	}

}