package ch.bfh.project1.kanu.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

import ch.bfh.project1.kanu.controller.StartlistenController;
import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rennen;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class StartlistenView implements ViewTemplate {

	private UI ui; // Haupt GUI
	private Button gesamteStartlisteAlsPDF = new Button("Gesamte Startliste als PDF herunterladen");
	private StartlistenController sController = new StartlistenController();
	private Rennen rennen;
	private List<Rennen> lrennen;
	
	private GridLayout layout = new GridLayout(3, 3);
	
	private List<ListSelect> block = new ArrayList<ListSelect>();
	private ListSelect kategorien = new ListSelect();
	
	private ClickListener cladd, clrem;

	public StartlistenView(UI ui) {
		this.ui = ui;
	}
	
	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		
		kategorien.setMultiSelect(true);
		
		cladd = new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ListSelect tmp = (ListSelect) event.getButton().getData();
				@SuppressWarnings("unchecked")
				Set<Integer> temp = (Set<Integer>) kategorien.getValue();
				for(int id : temp)
				{
					tmp.addItem(id);
					tmp.setItemCaption(id, kategorien.getItemCaption(id));
					kategorien.removeItem(id);
				}
			}
		};
		
		clrem = new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ListSelect tmp = (ListSelect) event.getButton().getData();
				@SuppressWarnings("unchecked")
				Set<Integer> temp = (Set<Integer>) tmp.getValue();
				for(int id : temp)
				{
					kategorien.addItem(id);
					kategorien.setItemCaption(id, tmp.getItemCaption(id));
					tmp.removeItem(id);
				}
			}
		};
	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@Override
	public void viewAnzeigen(Component inhalt) {
		layout.removeAllComponents();
		if(rennen == null)
		{
			lrennen = sController.ladeAlleRennen();
			zeigeRennen(inhalt);
		}
		else if(rennen.getRennenID() == null)
			throw new IllegalArgumentException("Kein gültiges Rennen angegeben"); //TODO Fehlermeldung ausgeben
		else
		{
			int i = 0;
			block.clear();
			layout.setRows(3);
			List<Integer> kats = new ArrayList<Integer>();
			List<ListSelect> bloecke = sController.ladeBloecke(rennen.getRennenID(), kats);
			kategorien.clear();
			for(AltersKategorie kat : rennen.getKategorien())
			{
				kategorien.addItem(kat.getAltersKategorieID());
				kategorien.setItemCaption(kat.getAltersKategorieID(), kat.getName());
			}
			for(Integer k : kats)
			{
				kategorien.removeItem(k);
			}
			for(ListSelect ls : bloecke)
			{
				if(i > 0)
				{
					int rows = layout.getRows();
					layout.setRows(rows + 2);
				}
				Button tmp = new Button("<<");
				tmp.setData(ls);
				tmp.addClickListener(cladd);
				Button tmp2 = new Button(">>");
				tmp2.setData(ls);
				tmp2.addClickListener(clrem);
				block.add(ls);
				layout.addComponent(ls, 0, i * 2, 0, i * 2 + 1);
				layout.addComponent(tmp, 1, i * 2);
				layout.addComponent(tmp2, 1, i * 2 + 1);
				layout.setComponentAlignment(tmp, Alignment.BOTTOM_CENTER);
				layout.setComponentAlignment(tmp2, Alignment.TOP_CENTER);
				i++;
			}
			if(i == 0)
			{
				ListSelect tmp1 = new ListSelect();
				tmp1.setMultiSelect(true);
				Button tmp = new Button("<<");
				tmp.setData(tmp1);
				tmp.addClickListener(cladd);
				Button tmp2 = new Button(">>");
				tmp2.setData(tmp1);
				tmp2.addClickListener(clrem);
				block.add(tmp1);
				layout.addComponent(tmp1, 0, 0, 0, 1);
				layout.addComponent(tmp, 1, 0);
				layout.addComponent(tmp2, 1, 1);
				layout.setComponentAlignment(tmp, Alignment.BOTTOM_CENTER);
				layout.setComponentAlignment(tmp2, Alignment.TOP_CENTER);
			}
			if(i > 0)
				i--;
			
			//kategorien.setRows(kategorien.size());
			layout.addComponent(kategorien, 2, 0, 2, layout.getRows() - 1);
			layout.setComponentAlignment(kategorien, Alignment.MIDDLE_CENTER);
			
			Button bneu = new Button("Block hinzufügen");
			layout.addComponent(bneu, 1, i * 2 + 2);
			
			bneu.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;
	
				@Override
				public void buttonClick(ClickEvent event) {
					addBox();
				}
			});
			
			Button speichern = new Button("Speichern");
			speichern.addClickListener(event -> {
				List<ListSelect> tmpBlock = sController.bloeckeVorbereiten(block);
				sController.speichereBloecke(tmpBlock, rennen);
				sController.generiereStartliste(tmpBlock, rennen);
			});
			layout.addComponent(speichern, 0, i * 2 + 2);
			
			layout.setSpacing(true);
		}
		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(layout);
	}
	
	private void zeigeRennen(Component inhalt)
	{
		Table trennen = new Table();
		trennen.addContainerProperty("Name", String.class, null);
		trennen.addContainerProperty("Ort", String.class, null);
		trennen.addContainerProperty("Datum", String.class, null);
		trennen.addContainerProperty("Blöcke", Button.class, null);
		trennen.addContainerProperty("Startliste", Button.class, null);
		for(Rennen r : lrennen)
		{
			Object id = trennen.addItem();
			Item row = trennen.getItem(id);
			row.getItemProperty("Name").setValue(r.getName());
			row.getItemProperty("Ort").setValue(r.getOrt());
			row.getItemProperty("Datum").setValue(r.getDatum().toGMTString()); //TODO
			Button bbearbeiten = new Button("Blöcke");
			bbearbeiten.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					setRennen(r);
					viewAnzeigen(inhalt);
				}
			});
			row.getItemProperty("Blöcke").setValue(bbearbeiten);
			Button bsl = new Button("Startliste");
			bsl.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					setRennen(r);
					zeigeStartliste();
				}
			});
			row.getItemProperty("Startliste").setValue(bsl);
		}
		trennen.setPageLength(lrennen.size());
		layout.addComponent(trennen);
	}
	
	@SuppressWarnings("unchecked")
	private void zeigeStartliste()
	{
		layout.removeAllComponents();
		List<FahrerResultat> fahrer = sController.ladeStartliste(rennen.getRennenID());
		Table sl = new Table();
		sl.addContainerProperty("#", String.class, null);
		sl.addContainerProperty("Zeit 1. Lauf", String.class, null);
		sl.addContainerProperty("Zeit 2. Lauf", String.class, null);
		sl.addContainerProperty("Name", String.class, null);
		int altKat = -1;
		int kats = 0;
		for(FahrerResultat f : fahrer)
		{
			if(altKat != f.getKategorie().getAltersKategorieID())
			{
				altKat = f.getKategorie().getAltersKategorieID();
				Object id = sl.addItem();
				Item row = sl.getItem(id);
				row.getItemProperty("#").setValue("");
				row.getItemProperty("Zeit 1. Lauf").setValue(f.getKategorie().getName());
				row.getItemProperty("Zeit 2. Lauf").setValue("");
				row.getItemProperty("Name").setValue("");
				kats++;
			}
			Object id = sl.addItem();
			Item row = sl.getItem(id);
			row.getItemProperty("#").setValue(f.getStartnummer() + "");
			row.getItemProperty("Zeit 1. Lauf").setValue(f.getStartzeitEins());
			row.getItemProperty("Zeit 2. Lauf").setValue(f.getStartzeitZwei());
			row.getItemProperty("Name").setValue(f.getFahrer().getVorname() + " " + f.getFahrer().getName()); //TODO
		}
		sl.setPageLength(fahrer.size() + kats);
		if(fahrer.size() == 0)
			layout.addComponent(new Label("Noch keine Startliste vorhanden"));
		else
			layout.addComponent(sl);
	}
	
	private void addBox()
	{
		ListSelect tmp = new ListSelect();
		tmp.setMultiSelect(true);
		Button badd = new Button("<<");
		badd.setData(tmp);
		badd.addClickListener(cladd);
		Button brem = new Button(">>");
		brem.addClickListener(clrem);
		brem.setData(tmp);
		block.add(tmp);
		Button tmp2 = (Button) layout.getComponent(0, layout.getRows() - 1);
		layout.removeComponent(tmp2);
		Button tmp4 = (Button) layout.getComponent(1, layout.getRows() - 1);
		layout.removeComponent(tmp4);
		int rows = layout.getRows();
		layout.setRows(rows + 2);
		layout.addComponent(tmp, 0, rows - 1, 0, rows);
		layout.addComponent(badd, 1, rows - 1);
		layout.addComponent(brem, 1, rows);
		layout.addComponent(tmp2, 0, rows + 1);
		layout.addComponent(tmp4, 1, rows + 1);
		ListSelect tmp3 = (ListSelect) layout.getComponent(2, 0);
		layout.removeComponent(tmp3);
		layout.addComponent(tmp3, 2, 0, 2, layout.getRows() - 1);
		layout.setComponentAlignment(tmp3, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(badd, Alignment.BOTTOM_CENTER);
		layout.setComponentAlignment(brem, Alignment.TOP_CENTER);
	}

	public Rennen getRennen() {
		return rennen;
	}

	/**
	 * Setzt das Rennen, für welches die Startliste generiert werden soll
	 * @param rennen
	 */
	public void setRennen(Rennen rennen) {
		this.rennen = rennen;
	}

}
