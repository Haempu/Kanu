package ch.bfh.project1.kanu.view;

import java.awt.Checkbox;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ch.bfh.project1.kanu.controller.FehlererfassungsController;
import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Fahrer;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rennen;
import ch.bfh.project1.kanu.model.Strafzeit;




/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class FehlererfassungsView implements ViewTemplate {
	private UI ui; // Haupt GUI
	private FehlererfassungsController fehlererfassungsController = new FehlererfassungsController();

	// UI Komponenten
	private Label titel = new Label("Fehlererfassung");
	//private Table tabelleAuswahl = new Table();
	private HorizontalLayout hlAuswahl = new HorizontalLayout();
	private Table tabelleFahrer = new Table();
	private FormLayout fehlererfassungslayout = new FormLayout();

	// Membervariablen
	private List<Rennen> rennliste;
	private List<AltersKategorie> altersKategorieliste;
	private NativeSelect dropDownRennen = new NativeSelect(NAME_RENNEN);
	private NativeSelect dropDownAlterskategorie = new NativeSelect(NAME_KATEGORIE);
	private Button btnVor = new Button("Vor");
	private Button btnZurueck = new Button("Zurück");
	private Button btnSpeichern = new Button("Speichern");
	
	
	//Popup Komponenten
	private Window popup;
	
	// Statische Variablen
	private static final String NAME_RENNEN = "Rennen";
	private static final String NAME_KATEGORIE = "Kategorie";
	private static final String COLUMN_NR = "Nr";
	private static final String COLUMN_NAME = "Name";
	private static final String COLUMN_CLUB = "Club";
	private static final String COLUMN_BEARBEITEN = "Bearbeiten";
	private static final String COLUMN_TOR = "Tor";
	private static final String COLUMN_BERUEHRT = "Berührt";
	private static final String COLUMN_VERFEHLT = "Verfehlt";
	/*private static final String COLUMN_ANGEMELDET = "Angemeldet";
	private static final String COLUMN_VORNAME = "Vorname";
	private static final String COLUMN_NACHNAME = "Nachname";
	private static final String COLUMN_JAHRGANG = "Jahrgang";
	private static final String COLUMN_BOOTSKLASSE = "Bootsklasse";
	private static final String COLUMN_ALTERSKATEGORIE = "Alterskategorie";*/

	/**
	 * Konstruktor: FehlererfassungsView
	 * 
	 * @param ui
	 */
	public FehlererfassungsView(UI ui) {
		this.ui = ui;
	}
	
	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		this.fehlererfassungslayout.setSpacing(true);
		this.titel.setValue("Fehlererfassung: Kanu Club Grenchen");
		this.titel.setStyleName("h2");

		this.rennliste = new ArrayList<Rennen>();
		this.altersKategorieliste = new ArrayList<AltersKategorie>();
		
		auswahlErzeugen();

		this.tabelleFahrer.setImmediate(true);
		this.tabelleFahrer.addContainerProperty(COLUMN_NR, String.class, null);
		this.tabelleFahrer.addContainerProperty(COLUMN_NAME, String.class, null);
		this.tabelleFahrer.addContainerProperty(COLUMN_CLUB, String.class, null);
		this.tabelleFahrer.addContainerProperty(COLUMN_BEARBEITEN, Button.class, null);
		this.tabelleFahrer.setWidth(100L, Component.UNITS_PERCENTAGE);
		
		this.popup = new Window("Fehler erfassen");
		this.popup.center();
		this.popup.setModal(true);
		this.popup.setWidth("600px");

		this.fehlererfassungslayout.addComponent(this.titel);
		this.fehlererfassungslayout.addComponent(this.hlAuswahl);
		this.fehlererfassungslayout.addComponent(this.tabelleFahrer);
	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@Override
	public void viewAnzeigen(Component inhalt) {
		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(this.fehlererfassungslayout);
	}

	/**
	 * Funktion erzeugt die Auswahl.
	 */
	private void auswahlErzeugen(){
		dropDownRennen.setRequired(true);
		dropDownAlterskategorie.setRequired(true);
		
		for (Rennen rennen : fehlererfassungsController.ladeRennen()) {
			dropDownRennen.addItem(rennen);
		}
		
		dropDownRennen.addValueChangeListener(event -> {
			Rennen aktRennen = (Rennen) dropDownRennen.getValue();
			for (AltersKategorie ak : aktRennen.getKategorien()) {
				dropDownAlterskategorie.addItem(ak);
			}
		});
		
		dropDownAlterskategorie.addValueChangeListener(event -> {
			Rennen aktRennen = (Rennen) dropDownRennen.getValue();
			System.out.println(aktRennen.getRennenID());
			AltersKategorie aktKategorie = (AltersKategorie) dropDownAlterskategorie.getValue();
			generiereTabelleTeilnehmer(aktRennen, aktKategorie.getAltersKategorieID());
		});
		
		
		this.hlAuswahl.addComponent(dropDownRennen);
		this.hlAuswahl.addComponent(dropDownAlterskategorie);
	}
	
	/**
	 * Funktion füllt die Tabelle mit allen angemeldeten Fahrer des Clubs ab.
	 */
	private void generiereTabelleTeilnehmer(Rennen rennen, int aktKategorieID) {
		this.tabelleFahrer.removeAllItems();
		final int anzTore = rennen.getAnzTore();
		List<FahrerResultat> angemeldeteFahrer = this.fehlererfassungsController.ladeFahrerliste(rennen.getRennenID(), aktKategorieID);
			for (FahrerResultat fr : angemeldeteFahrer) {
				Object newItemId = this.tabelleFahrer.addItem();
				Item row = this.tabelleFahrer.getItem(newItemId);
				final Integer fahrerID = fr.getFahrer().getFahrerID();

				Button bearbeiten = new Button("Bearbeiten");
				bearbeiten.addClickListener(event -> {
					FormLayout popupLayoutForm = new FormLayout();
					VerticalLayout lauf1 = new VerticalLayout();
					lauf1.addComponent(new Label("Lauf 1"));
					lauf1.addComponent(generiereTabelleFehlererfassung(fahrerID, 1));
					VerticalLayout lauf2 = new VerticalLayout();
					lauf2.addComponent(new Label("Lauf 2"));
					lauf2.addComponent(generiereTabelleFehlererfassung(fahrerID, 2));
					HorizontalLayout hl = new HorizontalLayout();
					hl.addComponent(lauf1);
					hl.addComponent(lauf2);
					hl.setMargin(true);
					HorizontalLayout buttons = new HorizontalLayout();
					buttons.addComponent(btnZurueck);
					buttons.addComponent(btnVor);
					buttons.addComponent(btnSpeichern);
					buttons.setMargin(true);
					popupLayoutForm.addComponent(hl);
					popupLayoutForm.addComponent(buttons);
					popupLayoutForm.addStyleName("popup");
					this.popup.setContent(popupLayoutForm);
					this.ui.addWindow(this.popup);
				});
				
				row.getItemProperty(COLUMN_NR).setValue("1");
				row.getItemProperty(COLUMN_NAME).setValue(fr.getFahrer().getName() + " " + fr.getFahrer().getVorname());
				row.getItemProperty(COLUMN_CLUB).setValue("Testclub");
				row.getItemProperty(COLUMN_BEARBEITEN).setValue(bearbeiten);
			}
	}

	private Table generiereTabelleFehlererfassung(Integer fahrerID, int lauf) {
		Table tabelle = new Table();
		tabelle.setImmediate(true);
		tabelle.addContainerProperty(COLUMN_TOR, Integer.class, null);
		tabelle.addContainerProperty(COLUMN_BERUEHRT, CheckBox.class, null);
		tabelle.addContainerProperty(COLUMN_VERFEHLT, CheckBox.class, null);
		tabelle.setWidth(100L, Component.UNITS_PERCENTAGE);
		List<Strafzeit> strafzeitliste = new ArrayList<Strafzeit>();
		Rennen aktRennen = (Rennen) dropDownRennen.getValue();
		int anzTore = aktRennen.getAnzTore();
		AltersKategorie aktKategorie = (AltersKategorie) dropDownAlterskategorie.getValue();
		strafzeitliste = this.fehlererfassungsController.ladeStrafzeitliste(fahrerID, aktRennen.getRennenID(), aktKategorie.getAltersKategorieID(), lauf);
		int j = 0;
		for (int i = 0; i < anzTore; i++) {
			boolean beruehrt = false;
			boolean verpasst = false;
			Object newItemId = tabelle.addItem();
			Item row = tabelle.getItem(newItemId);
			if (strafzeitliste.size() > j && strafzeitliste.get(j).getTorNummer() == i) {
				beruehrt = strafzeitliste.get(j).getTorBeruehrt();
				verpasst = strafzeitliste.get(j).getTorVerpasst();
				j++;
			}
			row.getItemProperty(COLUMN_TOR).setValue((Integer) i+1);
			row.getItemProperty(COLUMN_BERUEHRT).setValue(new CheckBox("", beruehrt));
			row.getItemProperty(COLUMN_VERFEHLT).setValue(new CheckBox("", verpasst));
		}
		return tabelle;
	}
	
	/**
	 * Die Funktion zeigt das entsprechende Layout für die angemeldeten Fahrer
	 * an.
	 * 
	 * @param rennenID
	 */
//	private void zeigeAngemeldeteFahrer(Integer rennenID) {
//		this.layoutAngemeldeteFahrer.removeAllComponents();
//
//		if (angemeldeteFahrerTabelleAbfuellen(rennenID)) {
//			this.layoutAngemeldeteFahrer.addComponent(this.angemeldeteFahrerTable);
//		} else {
//			this.layoutAngemeldeteFahrer.addComponent(new Label("Keine Fahrer angemeldet"));
//		}
//	}
}
	
	
	
//	private void generiereFahrertabelle(List<FahrerResultat> listeFahrer) {
//		this.tabelleFahrer.clear();
//		System.out.println(listeFahrer.size());
//		listeFahrer.add(new FahrerResultat());
//		listeFahrer.get(0).setFahrer(new Fahrer(1, "Hans", "Müller", 1993, true));
//		for (FahrerResultat fr : listeFahrer) {
//			Object newItemId = this.tabelleFahrer.addItem();
//			Item row = this.tabelleFahrer.getItem(newItemId);
//			row.getItemProperty(COLUMN_NR).setValue("1");
//			row.getItemProperty(COLUMN_NAME).setValue(fr.getFahrer().getName() + " " + fr.getFahrer().getVorname());
//			row.getItemProperty(COLUMN_CLUB).setValue(fr.getFahrer().getClub().getName());
//			row.getItemProperty(COLUMN_BEARBEITEN).setValue(newValue);
//		}
//		this.tabelleFahrer.setPageLength(listeFahrer.size());
//	}

