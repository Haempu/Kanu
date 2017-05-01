package ch.bfh.project1.kanu.controller;

import java.util.ArrayList;
import java.util.List;

import ch.bfh.project1.kanu.model.Benutzer;
import ch.bfh.project1.kanu.model.Fahrer;

/**
 * Die Klasse DBController ist zuständig, die Daten aus der Datenbank zur
 * Verfügung zu stellen und auf die Datenbank zu schreiben.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class DBController {
	public List<String> fahrerlisteClub(int clubID){
		return new ArrayList();
	}
	
	public void fahrerAnmelden(int fahrerID){
		
	}
	
	public void fahrerAbmelden(int fahrerID){
		
	}
	
	public Fahrer ladeFahrer(int fahrerID){
		return new Fahrer();
	}
	
	public void speichereFahrer(Fahrer fahrer){
		
	}
	
	public Benutzer ladeBenutzer(int benutzerID){
		return new Benutzer();
	}
	
	public void speichereBenutzer(Benutzer benutzerID){
		
	}
	
	public List<String> ladeAngemeldeteClubs(){
		return new ArrayList();
	}
}
