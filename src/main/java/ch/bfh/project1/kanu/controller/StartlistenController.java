package ch.bfh.project1.kanu.controller;

import ch.bfh.project1.kanu.model.Rennen;
import ch.bfh.project1.kanu.view.StartlistenView;

/**
 * Die Klasse StartlistenController beinhaltet die Logik der Klasse
 * StartlistenView.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class StartlistenController {
	private DBController db;
	private StartlistenView startlistenView;

	public StartlistenController() {
		db = DBController.getInstance();
	}
	
	public void startlistenErstellen() {
		
	}
	
	public Rennen ladeRennen(Integer rennenID)
	{
		return db.ladeRennen().get(0); //TODO
	}
}
