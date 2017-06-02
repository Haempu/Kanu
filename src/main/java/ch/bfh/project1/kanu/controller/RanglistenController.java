package ch.bfh.project1.kanu.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.itextpdf.text.DocumentException;

import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rangliste;
import ch.bfh.project1.kanu.model.Rennen;
import ch.bfh.project1.kanu.util.ResultatComparator;

/**
 * Die Klasse RanglistenController beinhaltet die Logik der Klasse
 * RanglistenView.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class RanglistenController {
	private DBController dbController;
	
	public RanglistenController()
	{
		dbController = DBController.getInstance();
	}

	/**
	 * Gibt die Gesamtrangliste des Rennens zurück.
	 * Löscht aber alle Fahrer wieder, die noch keinen Lauf absolviert haben!
	 * 
	 * @param rennen
	 *            - ID des Rennens.
	 * @return Rangliste
	 */
	public Rangliste ladeRanglisteRennen(Rennen rennen) {
		Rangliste rl = dbController.ladeRanglisteRennen(rennen);
		rl.setRennen(dbController.ladeRennen(rl.getRennen().getRennenID()));
		List<FahrerResultat> zuloeschen = new ArrayList<FahrerResultat>();
		for(FahrerResultat f : rl.getResultate())
		{
			if(f.getZeitTotal() == 0) //Wenn kein Lauf absolviert wurde, Fahrer wieder löschen!
				zuloeschen.add(f);
		}
		rl.getResultate().removeAll(zuloeschen);
		return rl;
	}
	
	/**
	 * Lädt alle Rennen aus der db
	 * @return
	 */
	public List<Rennen> ladeRennen()
	{
		return dbController.ladeRennen();
	}
	
	/**
	 * Lädt ein Rennen aus der db
	 * @param rennenID
	 * @return
	 */
	public Rennen ladeRennen(Integer rennenID)
	{
		return dbController.ladeRennen(rennenID);
	}
	
	/**
	 * Diese Methode sortiert die Rangliste für das PDF Dokument nach Kategorien und innerhalb der Kategorien nach Zeit. 
	 * @param rangliste Die Rangliste
	 * @return Eine sortierte Rangliste (neues Objekt, nicht das alte)
	 */
	public Rangliste sortiereRangliste(Rangliste rangliste)
	{
		int altKat = -1;
		List<FahrerResultat> res = new ArrayList<FahrerResultat>();
		List<FahrerResultat> restot = new ArrayList<FahrerResultat>();
		Rangliste resultat = new Rangliste();
		resultat.setRennen(rangliste.getRennen());
		System.out.println(rangliste.getResultate().size());
		for(FahrerResultat f : rangliste.getResultate())
		{
			//Wenn neue Kategorie, die alte Kategorie sortieren und speichern
			if(altKat != f.getKategorie().getAltersKategorieID()) 
			{
				System.out.println(res.size());
				altKat = f.getKategorie().getAltersKategorieID();
				//Nur nehmen, wenn auch Fahrer vorhanden sind
				if(res.size() > 0) 
				{
					Collections.sort(res, new ResultatComparator());
					restot.addAll(res);
					res.clear();
				}
			}
			res.add(f);
		}
		resultat.setResultate(restot);
		return resultat;
	}

	/**
	 * Generiert ein PDF-Dokument mit einer oder mehrerern Tabellen vom Typ
	 * "Rangliste".
	 * 
	 * @param pfad
	 *            - Speicherort des Dokuments.
	 * @param rangliste
	 *            - Liste vom Typ "Rangliste".
	 * @throws IOException
	 * @throws DocumentException
	 */
	public String generierePDF(Rangliste rangliste) throws IOException, DocumentException {
		return PDFController.generierePdfRangliste("D:\\Downloads\\", sortiereRangliste(rangliste));
	}

}