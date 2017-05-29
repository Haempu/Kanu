package ch.bfh.project1.kanu.controller;

import java.util.ArrayList;
import java.util.List;

import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.FahrerRennen;
import ch.bfh.project1.kanu.model.Rennen;
import ch.bfh.project1.kanu.view.StartlistenView;

import com.vaadin.ui.ListSelect;

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
	
	public Rennen ladeRennen(Integer rennenID)
	{
		return db.ladeRennen(rennenID);
	}
	
	/**
	 * Speichert die Blöcke in der Datenbank ab. Allfällig bereits gespeicherte Blöcke werden zuerst gelöscht und
	 * danach durch die aktuelle Anordnung angepasst.
	 * @param block Eine Liste mit den Blöcken (ListSelects)
	 * @param rennen Das Rennen
	 */
	public void speichereBloecke(List<ListSelect> block, Rennen rennen)
	{
		db.loescheBloecke(rennen.getRennenID());
		int blockNr = 1;
		for(ListSelect l : block)
		{
			@SuppressWarnings("unchecked")
			List<Integer> ids = (List<Integer>) l.getItemIds();
			db.speichereBlock(rennen.getRennenID(), blockNr, ids);
			blockNr++;
		}
	}
	
	/**
	 * Liest zum gegebenen Rennn die Blöcke aus der db, sofern vorhanden
	 * @param rennenID
	 * @return
	 */
	public List<ListSelect> ladeBloecke(Integer rennenID)
	{
		List<ListSelect> ls = new ArrayList<ListSelect>();
		for(AltersKategorie k : db.ladeBloecke(rennenID))
		{
			if(ls.size() < k.getBlock())
			{
				ListSelect tmp = new ListSelect();
				tmp.setMultiSelect(true);
				tmp.setRows(10);
				ls.add(tmp);
			}
			ls.get(k.getBlock() - 1).addItem(k.getAltersKategorieID());
			ls.get(k.getBlock() - 1).setItemCaption(k.getAltersKategorieID(), k.getName());
		}
		System.out.println(ls.size());
		return ls;
	}
	
	/**
	 * Hier wird die Startliste generiert. Es werden für jeden Fahrer zwei Läufe erstellt, wobei die Blöcke
	 * berücksichtigt werden.
	 * @param block Eine Liste mit den Blöcken (ListSelect)
	 * @param rennen Das Rennen
	 * @return true wenn alles erfolgreich (inkl. Speichern in der db), false sonst
	 */
	public boolean generiereStartliste(List<ListSelect> block, Rennen rennen)
	{
		int zeit = rennen.getDatumVon().getHours() * 60 + rennen.getDatumVon().getMinutes();
		int i = 0;
		boolean success = true;
		for(ListSelect ls : block)
		{
			@SuppressWarnings("unchecked")
			List<Integer> ids = (List<Integer>) ls.getItemIds();
			int startBlock = zeit;
			for(int x = 0;x < 2;x++)
			{
				if(x == 1)
				{
					if(zeit - startBlock < 45) //Wenn keine 45min zwischen erstem und zweitem Lauf vergangen sind, die Pause erzwingen
						zeit += startBlock + 45;
				}
				for(Integer id : ids)
				{
					List<FahrerRennen> fahrer = db.ladeFehlererfassung(rennen.getRennenID(), id, 0);
					for(FahrerRennen f : fahrer)
					{
						if(x == 0) //Beim ersten Lauf wird der Startplatz (Nr.) vergeben, die ist beim zweiten Lauf gleich
						{
							i++;
							f.setStartplatz(i);
							f.setStartzeit1(zeit / 60 + ":" + zeit % 60 + ":00");
						}
						else
							f.setStartzeit2(zeit / 60 + ":" + zeit % 60 + ":00");
						zeit++;
						if(!db.speichereStartplatz(f))
							success = false;
					}
				}
			}
		}
		return success;
	}
	
	/**
	 * Entfernt alle Blöcke mit 0 Kategorien aus der Liste
	 * @param block Die Liste mit den Blöcken
	 * @return Die bereinigte Liste mit Blöcken
	 */
	public List<ListSelect> bloeckeVorbereiten(List<ListSelect> block)
	{
		for(int i = 0;i < block.size();i++)
		{
			if(block.get(i).size() == 0)
			{
				block.remove(i);
				i--;
			}
		}
		return block;
	}
}
