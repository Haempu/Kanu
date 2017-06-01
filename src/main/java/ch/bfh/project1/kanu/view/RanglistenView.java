package ch.bfh.project1.kanu.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import ch.bfh.project1.kanu.controller.RanglistenController;
import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rangliste;
import ch.bfh.project1.kanu.model.Rennen;
import ch.bfh.project1.kanu.util.ResultatComparator;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class RanglistenView implements ViewTemplate {

	private boolean init = false;
	private VerticalLayout layout;
	private Rennen rennen;
	private RanglistenController rController;

	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		this.init = true;
		layout = new VerticalLayout();
		rController = new RanglistenController();
	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void viewAnzeigen(Component inhalt) {
		if (rennen == null)
			throw new IllegalArgumentException("Kein Rennen angegeben");
		layout.removeAllComponents();
		
		NativeSelect ns = new NativeSelect();
		List<Rennen> lrennen = rController.ladeRennen();
		for(Rennen r : lrennen)
		{
			ns.addItem(r.getRennenID());
			ns.setItemCaption(r.getRennenID(), r.getName());
		}
		ns.select(rennen.getRennenID());
		ns.addValueChangeListener(event -> {
			setRennen(rController.ladeRennen((Integer) ns.getValue()));
			viewAnzeigen(inhalt);
		});
		layout.addComponent(ns);
		Label titel = new Label("Rangliste");
		titel.setStyleName("h2");
		layout.addComponent(titel);
		Rangliste rangliste = rController.ladeRanglisteRennen(rennen);
		Button pdf = new Button("PDF generieren");
		pdf.addClickListener(event -> {
			try {
				rController.generierePDF(rangliste);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		layout.addComponent(pdf);
		int altKat = -1;
		List<FahrerResultat> res = new ArrayList<FahrerResultat>();
		rangliste.getResultate().add(new FahrerResultat(new AltersKategorie(-2, ""))); //Damit auch die letzte Kategorie angezeigt wird
		for(FahrerResultat f : rangliste.getResultate()) 
		{
			if(altKat != f.getKategorie().getAltersKategorieID()) 
			{
				altKat = f.getKategorie().getAltersKategorieID();
				if(res.size() > 0) 
				{
					// Tabelle anzeigen
					Collections.sort(res, new ResultatComparator());
					Table trangliste = new Table();
					trangliste.setWidth("100%");
					trangliste.addContainerProperty(Tabelle.RANG, String.class, null);
					trangliste.addContainerProperty(Tabelle.NAME, String.class, null);
					trangliste.addContainerProperty(Tabelle.CLUB, String.class, null);
					trangliste.addContainerProperty(Tabelle.ZEIT1, String.class, null);
					trangliste.addContainerProperty(Tabelle.FEHLER1, String.class, null);
					trangliste.addContainerProperty(Tabelle.TOTAL1, String.class, null);
					trangliste.addContainerProperty(Tabelle.ZEIT2, String.class, null);
					trangliste.addContainerProperty(Tabelle.FEHLER2, String.class, null);
					trangliste.addContainerProperty(Tabelle.TOTAL2, String.class, null);
					trangliste.addContainerProperty(Tabelle.TOTAL, String.class, null);
					trangliste.addContainerProperty(Tabelle.DIFF, String.class, null);
					int i = 1;
					for(FahrerResultat r : res) 
					{
						Object id = trangliste.addItem();
						Item row = trangliste.getItem(id);
						SimpleDateFormat df = new SimpleDateFormat("mm:ss.S");
						row.getItemProperty(Tabelle.RANG).setValue(i + "");
						row.getItemProperty(Tabelle.NAME).setValue(r.getFahrer().getVorname() + " " + r.getFahrer().getName());
						row.getItemProperty(Tabelle.CLUB).setValue(r.getFahrer().getClub().getName());
						Date d = new Date(r.getZeitErsterLauf());
						row.getItemProperty(Tabelle.ZEIT1).setValue(df.format(d.getTime()) + "");
						row.getItemProperty(Tabelle.FEHLER1).setValue(r.getStrafzeit1() + "");
						row.getItemProperty(Tabelle.TOTAL1).setValue(renderMilli(r.getGesamtzeit1()) + "");
						d = new Date(r.getZeitZweiterLauf());
						row.getItemProperty(Tabelle.ZEIT2).setValue(df.format(d.getTime()) + "");
						row.getItemProperty(Tabelle.FEHLER2).setValue(r.getStrafzeit2() + "");
						row.getItemProperty(Tabelle.TOTAL2).setValue(renderMilli(r.getGesamtzeit2()) + "");
						row.getItemProperty(Tabelle.TOTAL).setValue(renderMilli(r.getZeitTotal()));
						i++;
					}
					trangliste.setPageLength(res.size());
					layout.addComponent(new Label("Kategorie " + res.get(0).getKategorie().getName()));
					layout.addComponent(trangliste);
					res.clear();
				}
			}
			res.add(f);
		}

		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(layout);
	}

	@Override
	public boolean istInitialisiert() {
		return this.init;
	}

	public void setRennen(Rennen rennen) {
		this.rennen = rennen;
	}
	
	/**
	 * Wandelt die Millisekunden in ein von Menschen lesbares Format um.
	 * @param zahl Die Zahl in ms
	 * @return Formatierter String im Format mm.ss.S
	 */
	private String renderMilli(Integer zahl)
	{
		Integer hs = (zahl % 1000) / 100;
		Integer s = (zahl / 1000) % 60;
		Integer m = zahl / 60000;
		String string = String.format("%02d:%02d.%d", m, s, hs);
		return string;
	}
	
	public enum Tabelle {
		RANG("#"), NAME("Name"), CLUB("Club"), ZEIT1("Zeit 1. Lauf"), FEHLER1("Fehler"), 
		TOTAL1("Total"), ZEIT2("Zeit 2. Lauf"), FEHLER2("Fehler"), TOTAL2("Total"), 
		TOTAL("Total"), DIFF("Diff");

		private final String column;

		Tabelle(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
		
		@Override
		public String toString() {
			return column;
		}
	}

}
