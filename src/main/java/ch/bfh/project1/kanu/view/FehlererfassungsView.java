package ch.bfh.project1.kanu.view;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Container;
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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ch.bfh.project1.kanu.controller.FehlererfassungsController;
import ch.bfh.project1.kanu.model.AltersKategorie;
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
	private boolean init = false;
	private UI ui; // Haupt GUI
	private FehlererfassungsController fehlererfassungsController = new FehlererfassungsController();

	// UI Komponenten
	private Label titel = new Label("Fehlererfassung");
	// private Table tabelleAuswahl = new Table();
	private HorizontalLayout hlAuswahl = new HorizontalLayout();
	private Table tabelleFahrer = new Table();
	private HorizontalLayout tabelleLayout = new HorizontalLayout();
	private FormLayout fehlererfassungslayout = new FormLayout();
	private Window popup;

	// Membervariablen
	private List<Rennen> rennliste;
	private List<AltersKategorie> altersKategorieliste;
	private NativeSelect dropDownRennen = new NativeSelect(NAME_RENNEN);
	private NativeSelect dropDownAlterskategorie = new NativeSelect(NAME_KATEGORIE);
	private List<Strafzeit> strafzeitenLauf1 = new ArrayList<>();
	private List<Strafzeit> strafzeitenLauf2 = new ArrayList<>();

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
	private static final int ROW_HEIGHT_TORE = 10;

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
		this.titel.setValue("Fehlererfassung");
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
		this.popup.setWidth("580px");

		this.tabelleLayout.addComponent(new Label("Keine angemeldeten Fahrer für das ausgewählte Rennen."));

		this.fehlererfassungslayout.addComponent(this.titel);
		this.fehlererfassungslayout.addComponent(this.hlAuswahl);
		this.fehlererfassungslayout.addComponent(this.tabelleLayout);
		this.init = true;

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
	private void auswahlErzeugen() {

		for (Rennen rennen : fehlererfassungsController.ladeRennen()) {
			this.dropDownRennen.addItem(rennen);
		}

		dropDownRennen.addValueChangeListener(event -> {
			Rennen aktRennen = (Rennen) dropDownRennen.getValue();
			this.dropDownAlterskategorie.removeAllItems();
			for (AltersKategorie ak : aktRennen.getKategorien()) {
				dropDownAlterskategorie.addItem(ak);

			}
		});

		dropDownAlterskategorie.addValueChangeListener(event -> {
			Rennen aktRennen = (Rennen) dropDownRennen.getValue();
			AltersKategorie aktKategorie = (AltersKategorie) dropDownAlterskategorie.getValue();
			if (aktKategorie != null) {
				generiereTabelleTeilnehmer(aktRennen, aktKategorie.getAltersKategorieID());
			} else {
				this.tabelleLayout.removeAllComponents();
				this.tabelleLayout.addComponent(new Label("Keine Angemeldeten Fahrer für dieses Rennen."));
			}
		});

		this.hlAuswahl.addComponent(dropDownRennen);
		this.hlAuswahl.addComponent(dropDownAlterskategorie);
	}

	/**
	 * Funktion füllt die Tabelle mit allen angemeldeten Fahrer des Clubs ab.
	 */
	private void generiereTabelleTeilnehmer(Rennen rennen, int aktKategorieID) {
		this.tabelleLayout.removeAllComponents();
		this.tabelleFahrer.removeAllItems();
		final int anzTore = rennen.getAnzTore();
		List<FahrerResultat> angemeldeteFahrer = this.fehlererfassungsController.ladeFahrerliste(rennen.getRennenID(),
				aktKategorieID);

		if (angemeldeteFahrer.size() != 0) {
			for (FahrerResultat fr : angemeldeteFahrer) {
				Object newItemId = this.tabelleFahrer.addItem();
				Item row = this.tabelleFahrer.getItem(newItemId);
				final Integer fahrerID = fr.getFahrer().getFahrerID();

				Button bearbeiten = new Button("Bearbeiten");
				// Popup-Fenster
				bearbeiten.addClickListener(event -> {
					FormLayout popupLayoutForm = new FormLayout();
					VerticalLayout lauf1 = new VerticalLayout();
					lauf1.addComponent(new Label("Lauf 1"));
					Table lauf1Table = generiereTabelleFehlererfassung(fahrerID, 1);
					lauf1Table.setPageLength(ROW_HEIGHT_TORE);
					lauf1Table.setStyleName("tortable");
					Table lauf2Table = generiereTabelleFehlererfassung(fahrerID, 2);
					lauf2Table.setPageLength(ROW_HEIGHT_TORE);

					lauf1.addComponent(lauf1Table);
					VerticalLayout lauf2 = new VerticalLayout();
					lauf2.addComponent(new Label("Lauf 2"));
					lauf2.addComponent(lauf2Table);
					HorizontalLayout hl = new HorizontalLayout();
					hl.addComponent(lauf1);
					hl.addComponent(lauf2);
					hl.setMargin(true);
					Button btnSpeichern = new Button("Speichern");
					btnSpeichern.addClickListener(speichernEvent -> {
						Container container1 = lauf1Table.getContainerDataSource();
						for (Object itemId : container1.getItemIds()) {
							Item item = container1.getItem(itemId);
							Integer tor = (Integer) item.getItemProperty(COLUMN_TOR).getValue();
							CheckBox beruehrt = (CheckBox) item.getItemProperty(COLUMN_BERUEHRT).getValue();
							CheckBox verfehlt = (CheckBox) item.getItemProperty(COLUMN_VERFEHLT).getValue();

							this.fehlererfassungsController.fehlerErfassen(fr.getFahrer().getFahrerID(),
									fr.getKategorie().getAltersKategorieID(), rennen.getRennenID(), tor, 1,
									beruehrt.getValue(), verfehlt.getValue());
						}

						Container container2 = lauf2Table.getContainerDataSource();
						for (Object itemId : container2.getItemIds()) {
							Item item = container2.getItem(itemId);
							Integer tor = (Integer) item.getItemProperty(COLUMN_TOR).getValue();
							CheckBox beruehrt = (CheckBox) item.getItemProperty(COLUMN_BERUEHRT).getValue();
							CheckBox verfehlt = (CheckBox) item.getItemProperty(COLUMN_VERFEHLT).getValue();

							this.fehlererfassungsController.fehlerErfassen(fr.getFahrer().getFahrerID(),
									fr.getKategorie().getAltersKategorieID(), rennen.getRennenID(), tor, 2,
									beruehrt.getValue(), verfehlt.getValue());
						}

						this.popup.close();
					});

					popupLayoutForm.addComponent(hl);
					popupLayoutForm.addComponent(btnSpeichern);
					popupLayoutForm.addStyleName("popup");
					this.popup.setContent(popupLayoutForm);
					this.ui.addWindow(this.popup);
				});

				row.getItemProperty(COLUMN_NR).setValue(Integer.toString(fr.getStartnummer()));
				row.getItemProperty(COLUMN_NAME).setValue(fr.getFahrer().getName() + " " + fr.getFahrer().getVorname());
				row.getItemProperty(COLUMN_CLUB).setValue(fr.getFahrer().getClub().getName());
				row.getItemProperty(COLUMN_BEARBEITEN).setValue(bearbeiten);
			}

			this.tabelleFahrer.setPageLength(angemeldeteFahrer.size());
			this.tabelleLayout.addComponent(this.tabelleFahrer);

		} else {
			this.tabelleLayout.addComponent(new Label("Keine Angemeldeten Fahrer für diese Kategorie"));
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
		AltersKategorie aktKategorie = (AltersKategorie) this.dropDownAlterskategorie.getValue();
		strafzeitliste = this.fehlererfassungsController.ladeStrafzeitliste(fahrerID, aktRennen.getRennenID(),
				aktKategorie.getAltersKategorieID(), lauf);
		int j = 0;
		for (int i = 0; i < anzTore; i++) {
			boolean beruehrt = false;
			boolean verpasst = false;
			Object newItemId = tabelle.addItem();
			Item row = tabelle.getItem(newItemId);

			if (strafzeitliste.size() > j && strafzeitliste.get(j).getTorNummer() == i + 1) {
				beruehrt = strafzeitliste.get(j).isBeruehrt();
				verpasst = strafzeitliste.get(j).isVerpasst();
				j++;
			}

			// CheckBox cBeruehrt = new CheckBox("", beruehrt);
			// CheckBox cVerpasst = new CheckBox("", verpasst);
			// cBeruehrt.setImmediate(true);
			// cVerpasst.setImmediate(true);

			row.getItemProperty(COLUMN_TOR).setValue((Integer) i + 1);

			row.getItemProperty(COLUMN_BERUEHRT).setValue(new CheckBox("", beruehrt));
			row.getItemProperty(COLUMN_VERFEHLT).setValue(new CheckBox("", verpasst));
		}
		return tabelle;
	}

	@Override
	public boolean istInitialisiert() {
		return this.init;
	}
}
