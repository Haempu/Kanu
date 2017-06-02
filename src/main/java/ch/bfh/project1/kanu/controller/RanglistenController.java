package ch.bfh.project1.kanu.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.DocumentException;

import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rangliste;
import ch.bfh.project1.kanu.model.Rennen;
import ch.bfh.project1.kanu.view.RanglistenView;

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
	private static String DOKUMENTTITEL_STARTLISTE = "Startliste";
	private static String[] HEADER = { "Rang", "Startnr", "Name", "Club", "1. Lauf", "2. Lauf", "Gesamt" };
	private DBController dbController;
	private RanglistenView ranglistenView;
	
	public RanglistenController()
	{
		dbController = DBController.getInstance();
	}

	/**
	 * Gibt die Gesamtrangliste des Rennens zurück.
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
			
			Date d = new Date(f.getZeitErsterLauf());
			if(d.getTime() == 0)
				zuloeschen.add(f);
			else
				System.out.println(d.getTime());
		}
		rl.getResultate().removeAll(zuloeschen);
		return rl;
	}

	/**
	 * Gibt die Rangliste der Alterskategorie zurück.
	 * 
	 * @param altersKategorieID
	 *            - ID der Alterskategorie.
	 * @return Rangliste
	 */
	public Rangliste ladeRanglisteAltersKategorie(Integer altersKategorieID) {
		return this.dbController.ladeRanglisteAltersKategorie(null, altersKategorieID); //TODO
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
	public void generierePDF(String pfad, Rangliste rangliste) {
		try {
			PDFController.generierePdfRangliste(pfad, rangliste);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}