package ch.bfh.project1.kanu.view;

import java.text.SimpleDateFormat;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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

	private boolean init = false;

	//private UI ui; // Haupt GUI
	private Label titel = new Label("Startlistenverwaltung");
	private VerticalLayout vLayout = new VerticalLayout();
	//private Button gesamteStartlisteAlsPDF = new Button("Gesamte Startliste als PDF herunterladen");
	private StartlistenController sController = new StartlistenController();
	private Rennen rennen;
	private List<Rennen> lrennen;

	//private GridLayout layout = new GridLayout(3, 3);

	private List<ListSelect> block = new ArrayList<ListSelect>();
	private ListSelect kategorien = new ListSelect();
	
	private int anzBloecke = 1;

	private ClickListener cladd, clrem;

	public StartlistenView(UI ui) {
		//this.ui = ui;
	}

	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {

		titel.setStyleName("h2");
		kategorien.setMultiSelect(true);

		cladd = new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ListSelect tmp = (ListSelect) event.getButton().getData();
				@SuppressWarnings("unchecked")
				Set<Integer> temp = (Set<Integer>) kategorien.getValue();
				for (int id : temp) {
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
				for (int id : temp) {
					kategorien.addItem(id);
					kategorien.setItemCaption(id, tmp.getItemCaption(id));
					tmp.removeItem(id);
				}
			}
		};

		this.init = true;
	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@Override
	public void viewAnzeigen(Component inhalt) {
		vLayout.removeAllComponents();
		vLayout.addComponent(titel);
		if (rennen == null) 
		{
			lrennen = sController.ladeAlleRennen(); //Wenn kein Rennen ausgewählt, eine Liste mit Rennen ausgeben
			zeigeRennen(inhalt);
		} 
		else if(rennen.getRennenID() == null)
			throw new IllegalArgumentException("Kein gültiges Rennen angegeben");
		else
		{
			block.clear();
			List<Integer> kats = new ArrayList<Integer>();
			List<ListSelect> bloecke = sController.ladeBloecke(rennen.getRennenID(), kats);
			kategorien.clear();
			for (AltersKategorie kat : rennen.getKategorien()) //Kategorien ausgeben
			{
				kategorien.addItem(kat.getAltersKategorieID());
				kategorien.setItemCaption(kat.getAltersKategorieID(), kat.getName());
			}
			for(Integer k : kats) 
			{
				kategorien.removeItem(k); //Bereits gesetzte Kategorien wieder entfernen
			}
			HorizontalLayout hLayout = new HorizontalLayout();
			for(ListSelect ls : bloecke) //Blöcke aus der Datenbank anzeigen
			{
				GridLayout layout = new GridLayout(2, 3);
				Button tmp = new Button("<<");
				tmp.setData(ls);
				tmp.addClickListener(cladd);
				Button tmp2 = new Button(">>");
				tmp2.setData(ls);
				tmp2.addClickListener(clrem);
				block.add(ls);
				layout.addComponent(ls, 0, 0, 0, 1);
				layout.addComponent(tmp, 1, 0);
				layout.addComponent(tmp2, 1, 1);
				layout.setComponentAlignment(tmp, Alignment.BOTTOM_CENTER);
				layout.setComponentAlignment(tmp2, Alignment.TOP_CENTER);
				layout.setSpacing(true);
				Label lblock = new Label("Block " + anzBloecke);
				lblock.setStyleName("h2");
				vLayout.addComponent(lblock);
				if(anzBloecke == 1) //Beim ersten Block nebenan noch die verbleibenden Kategorien anzeigen
				{
					hLayout.addComponent(layout);
					hLayout.addComponent(kategorien);
					hLayout.setSpacing(true);
					vLayout.addComponent(hLayout);
				}
				else
					vLayout.addComponent(layout);
				anzBloecke++;
			}
			if(bloecke.size() == 0) //Wenn keine Blöcke in der db gefunden wurden, einen neuen anzeigen
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
				GridLayout layout = new GridLayout(2, 3);
				layout.addComponent(tmp1, 0, 0, 0, 1);
				layout.addComponent(tmp, 1, 0);
				layout.addComponent(tmp2, 1, 1);
				layout.setComponentAlignment(tmp, Alignment.BOTTOM_CENTER);
				layout.setComponentAlignment(tmp2, Alignment.TOP_CENTER);
				layout.setSpacing(true);
				Label lblock = new Label("Block 1");
				lblock.setStyleName("h2");
				vLayout.addComponent(lblock);
				hLayout.addComponent(layout);
				hLayout.addComponent(kategorien);
				hLayout.setSpacing(true);
				vLayout.addComponent(hLayout);
			}

			// kategorien.setRows(kategorien.size());
			HorizontalLayout hLayout2 = new HorizontalLayout();
			Button bneu = new Button("Block hinzufügen");

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
			hLayout2.addComponent(speichern);
			hLayout2.addComponent(bneu);
			vLayout.addComponent(hLayout2);

			//layout.setSpacing(true);
		}
		

		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(vLayout);
	}

	/**
	 * Zeigt die verschiedenen Rennen an, zu denen dann die Blöcke und Startlisten konfiguriert werden können
	 * @param inhalt Das Inhalts Panel
	 */
	@SuppressWarnings("unchecked")
	private void zeigeRennen(Component inhalt)
	{
		Table trennen = new Table();
		trennen.addContainerProperty("Name", String.class, null);
		trennen.addContainerProperty("Ort", String.class, null);
		trennen.addContainerProperty("Datum", String.class, null);
		trennen.addContainerProperty("Blöcke", Button.class, null);
		trennen.addContainerProperty("Startliste", Button.class, null);
		for (Rennen r : lrennen) 
		{
			Object id = trennen.addItem();
			Item row = trennen.getItem(id);
			row.getItemProperty("Name").setValue(r.getName());
			row.getItemProperty("Ort").setValue(r.getOrt());
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			row.getItemProperty("Datum").setValue(sdf.format(r.getDatumVon()));
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
		vLayout.addComponent(trennen);
	}

	/**
	 * Zeigt die Startliste an, sofern vorhanden
	 */
	@SuppressWarnings("unchecked")
	private void zeigeStartliste() 
	{
		vLayout.removeAllComponents();
		List<FahrerResultat> fahrer = sController.ladeStartliste(rennen.getRennenID());
		fahrer.add(new FahrerResultat(new AltersKategorie(-2, "")));
		int altKat = -1;
		List<FahrerResultat> res = new ArrayList<FahrerResultat>();
		for(FahrerResultat f : fahrer)
		{
			if(altKat != f.getKategorie().getAltersKategorieID()) 
			{
				altKat = f.getKategorie().getAltersKategorieID();
				if(res.size() > 0)
				{
					Table sl = new Table();
					sl.addContainerProperty("#", String.class, null);
					sl.addContainerProperty("Zeit 1. Lauf", String.class, null);
					sl.addContainerProperty("Zeit 2. Lauf", String.class, null);
					sl.addContainerProperty("Name", String.class, null);
					for(FahrerResultat r : res)
					{
						Object id = sl.addItem();
						Item row = sl.getItem(id);
						row.getItemProperty("#").setValue(r.getStartnummer() + "");
						row.getItemProperty("Zeit 1. Lauf").setValue(r.getStartzeitEins());
						row.getItemProperty("Zeit 2. Lauf").setValue(r.getStartzeitZwei());
						row.getItemProperty("Name").setValue(r.getFahrer().getVorname() + " " + r.getFahrer().getName());
					}
					sl.setPageLength(res.size());
					sl.setWidth("500px");
					vLayout.addComponent(new Label("Kategorie " + res.get(0).getKategorie().getName()));
					vLayout.addComponent(sl);
					res.clear();
				}
			}
			res.add(f);
		}
		if(fahrer.size() == 1)
			vLayout.addComponent(new Label("Noch keine Startliste vorhanden"));
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
		GridLayout layout = new GridLayout(3, 3);
		layout.addComponent(tmp, 0, 0, 0, 1);
		layout.addComponent(badd, 1, 0);
		layout.addComponent(brem, 1, 1);
		layout.setComponentAlignment(badd, Alignment.BOTTOM_CENTER);
		layout.setComponentAlignment(brem, Alignment.TOP_CENTER);
		layout.setSpacing(true);
		Label lblock = new Label("Block " + anzBloecke);
		lblock.setStyleName("h2");
		HorizontalLayout htemp = (HorizontalLayout) vLayout.getComponent(vLayout.getComponentCount() - 1);
		vLayout.removeComponent(htemp);
		vLayout.addComponent(lblock);
		anzBloecke++;
		vLayout.addComponent(layout);
		vLayout.addComponent(htemp);
	}

	public Rennen getRennen() {
		return rennen;
	}

	/**
	 * Setzt das Rennen, für welches die Startliste generiert werden soll
	 * 
	 * @param rennen
	 */
	public void setRennen(Rennen rennen) {
		this.rennen = rennen;
	}

	@Override
	public boolean istInitialisiert() {
		return this.init;
	}
}
