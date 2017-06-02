package ch.bfh.project1.kanu.view;

import java.util.ArrayList;
import java.util.List;

import ch.bfh.project1.kanu.controller.FehlererfassungsController;
import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rennen;
import ch.bfh.project1.kanu.model.Strafzeit;

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
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Hier kann der Torrichter die Fehler für die einzelnen Tore und Fahrer
 * eingeben und bearbeiten. Für die Übersicht wird ihm eine Tabelle, aller
 * startenden Fahrern, angezeigt. Mit dem Suchfeld kann er den aktuellen Fahrer
 * suchen und die Fehler pro Tor erfassen.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */
public class FehlererfassungsView implements ViewTemplate {
	private boolean init = false; // wurde die View initialisiert oder nicht

	// Controller
	private FehlererfassungsController fehlererfassungsController = new FehlererfassungsController();

	// UI Komponenten
	private UI ui; // Haupt GUI
	private Label titel = new Label("Fehlererfassung");
	private HorizontalLayout hlAuswahl = new HorizontalLayout();
	private TextField fahrerSuche = new TextField();
	private Table tabelleFahrer = new Table();
	private HorizontalLayout tabelleLayout = new HorizontalLayout();
	private FormLayout fehlererfassungslayout = new FormLayout();
	private Window popup;

	// Membervariablen
	//private List<Rennen> rennliste;
	//private List<AltersKategorie> altersKategorieliste;
	private NativeSelect dropDownRennen = new NativeSelect(NAME_RENNEN);
	private NativeSelect dropDownAlterskategorie = new NativeSelect(NAME_KATEGORIE);
	private List<Integer> bearbeiteteToreLauf1 = new ArrayList<>();
	private List<Integer> bearbeiteteToreLauf2 = new ArrayList<>();

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

		this.fahrerSuche.setInputPrompt("Suchen");
		this.fahrerSuche.setStyleName("search");

		auswahlErzeugen(); // DropDowns Rennen und Alterskategorie abfüllen

		// Tabellen initliasieren
		this.tabelleFahrer.setImmediate(true);
		this.tabelleFahrer.addContainerProperty(COLUMN_NR, String.class, null);
		this.tabelleFahrer.addContainerProperty(COLUMN_NAME, String.class, null);
		this.tabelleFahrer.addContainerProperty(COLUMN_CLUB, String.class, null);
		this.tabelleFahrer.addContainerProperty(COLUMN_BEARBEITEN, Button.class, null);
		this.tabelleFahrer.setWidth("100%");

		this.popup = new Window("Fehler erfassen");
		this.popup.center();
		this.popup.setModal(true);
		this.popup.setImmediate(true);
		this.popup.setWidth("580px");

		this.tabelleLayout.addComponent(new Label("Keine angemeldeten Fahrer für das ausgewählte Rennen."));

		// Event für die Suche
		this.fahrerSuche.addValueChangeListener(event -> {
			Rennen rennen = (Rennen) this.dropDownRennen.getValue();
			AltersKategorie kat = (AltersKategorie) this.dropDownAlterskategorie.getValue();

			if (this.dropDownRennen != null && kat != null) {
				generiereTabelleTeilnehmer(rennen, kat.getAltersKategorieID());
			}
		});

		// Komponenten zum Hauptlayout hinzufügen
		this.fehlererfassungslayout.addComponent(this.titel);
		this.fehlererfassungslayout.addComponent(this.hlAuswahl);
		this.fehlererfassungslayout.addComponent(this.fahrerSuche);
		this.fehlererfassungslayout.addComponent(this.tabelleLayout);

		this.init = true; // View ist initialisiert

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
	 * Funktion erzeugt die Auswahl für die Dropdownfelder Rennen und Kategorie
	 */
	private void auswahlErzeugen() {

		// Rennen Dropdown abfüllen
		for (Rennen rennen : fehlererfassungsController.ladeRennen()) {
			this.dropDownRennen.addItem(rennen);
		}

		// Event für Rennen Dropdown
		this.dropDownRennen.addValueChangeListener(event -> {
			Rennen aktRennen = (Rennen) this.dropDownRennen.getValue();
			this.dropDownAlterskategorie.removeAllItems();
			for (AltersKategorie ak : aktRennen.getKategorien()) {
				this.dropDownAlterskategorie.addItem(ak);

			}
		});

		// Event für Kategorie Dropdown
		this.dropDownAlterskategorie.addValueChangeListener(event -> {
			Rennen aktRennen = (Rennen) dropDownRennen.getValue();
			AltersKategorie aktKategorie = (AltersKategorie) this.dropDownAlterskategorie.getValue();
			if (aktKategorie != null) {
				// Tabelle abfüllen wenn kategorie ausgewählt wurde
				generiereTabelleTeilnehmer(aktRennen, aktKategorie.getAltersKategorieID());
			} else {
				// Wenn keine Kategorie ausgewählt wurde
				this.tabelleLayout.removeAllComponents();
				this.tabelleLayout.addComponent(new Label("Keine Angemeldeten Fahrer für dieses Rennen."));
			}
		});

		this.hlAuswahl.addComponent(this.dropDownRennen);
		this.hlAuswahl.addComponent(this.dropDownAlterskategorie);
	}

	/**
	 * Funktion füllt die Tabelle mit allen angemeldeten Fahrer ab.
	 * 
	 * @param rennen
	 *            - aktuell ausgewähltes Rennen
	 * @param aktKategorieID
	 *            - aktuell ausgewählte Kategorie
	 */
	@SuppressWarnings("unchecked") //Cast checked "von Hand"
	private void generiereTabelleTeilnehmer(Rennen rennen, Integer aktKategorieID) {
		// Tabelle und Layout zuerst leeren
		this.tabelleLayout.removeAllComponents();
		this.tabelleFahrer.removeAllItems();

		List<FahrerResultat> angemeldeteFahrer;

		if (this.fahrerSuche.getValue() == null || this.fahrerSuche.getValue().equals("")) {
			// Lade fahrer ohne Suche
			angemeldeteFahrer = this.fehlererfassungsController.ladeFahrerliste(rennen.getRennenID(), aktKategorieID);
		} else {
			// Lade fahrer mit Suche
			angemeldeteFahrer = this.fehlererfassungsController.ladeFahrerlistMitSuche(rennen.getRennenID(),
					aktKategorieID, this.fahrerSuche.getValue());
		}

		if (angemeldeteFahrer.size() != 0) {
			for (FahrerResultat fr : angemeldeteFahrer) {
				Object newItemId = this.tabelleFahrer.addItem();
				Item row = this.tabelleFahrer.getItem(newItemId);
				final Integer fahrerID = fr.getFahrer().getFahrerID();

				Button bearbeiten = new Button("Bearbeiten");
				// Popup-Fenster
				bearbeiten.addClickListener(event -> {
					FormLayout popupLayoutForm = new FormLayout();
					popupLayoutForm.setImmediate(true);
					VerticalLayout lauf1 = new VerticalLayout();

					lauf1.setImmediate(true);
					lauf1.addComponent(new Label("Lauf 1"));
					Table lauf1Table = generiereTabelleFehlererfassung(fahrerID, 1);
					lauf1Table.setImmediate(true);

					lauf1Table.setPageLength(ROW_HEIGHT_TORE);
					lauf1Table.setStyleName("tortable");
					Table lauf2Table = generiereTabelleFehlererfassung(fahrerID, 2);
					lauf2Table.setPageLength(ROW_HEIGHT_TORE);

					lauf1.addComponent(lauf1Table);
					VerticalLayout lauf2 = new VerticalLayout();
					lauf2.setImmediate(true);
					lauf2Table.setImmediate(true);

					lauf2.addComponent(new Label("Lauf 2"));
					lauf2.addComponent(lauf2Table);
					HorizontalLayout hl = new HorizontalLayout();
					hl.addComponent(lauf1);
					hl.addComponent(lauf2);
					hl.setMargin(true);
					Button btnSpeichern = new Button("Speichern");

					// Speichern der Fehlererfassung
					btnSpeichern.addClickListener(speichernEvent -> {

						// Fehlererfassung für ersten Lauf speichern
						Container container1 = lauf1Table.getContainerDataSource();
						for (Object itemId : container1.getItemIds()) {
							Item item = container1.getItem(itemId);
							Integer tor = (Integer) item.getItemProperty(COLUMN_TOR).getValue();
							for (Integer t : this.bearbeiteteToreLauf1) {
								if (t.intValue() == tor) {
									CheckBox beruehrt = (CheckBox) item.getItemProperty(COLUMN_BERUEHRT).getValue();
									CheckBox verfehlt = (CheckBox) item.getItemProperty(COLUMN_VERFEHLT).getValue();
									this.fehlererfassungsController.fehlerErfassen(fr.getFahrer().getFahrerID(),
											fr.getKategorie().getAltersKategorieID(), rennen.getRennenID(), tor, 1,
											beruehrt.getValue(), verfehlt.getValue());
								}
							}
						}

						// Fehlererfassung für zweiten Lauf speichern
						Container container2 = lauf2Table.getContainerDataSource();
						for (Object itemId : container2.getItemIds()) {
							Item item = container2.getItem(itemId);
							Integer tor = (Integer) item.getItemProperty(COLUMN_TOR).getValue();
							for (Integer t : this.bearbeiteteToreLauf2) {
								if (t.intValue() == tor) {
									CheckBox beruehrt = (CheckBox) item.getItemProperty(COLUMN_BERUEHRT).getValue();
									CheckBox verfehlt = (CheckBox) item.getItemProperty(COLUMN_VERFEHLT).getValue();

									this.fehlererfassungsController.fehlerErfassen(fr.getFahrer().getFahrerID(),
											fr.getKategorie().getAltersKategorieID(), rennen.getRennenID(), tor, 2,
											beruehrt.getValue(), verfehlt.getValue());
								}
							}
						}

						// Nach dem Speichern werden die bearbeiteten Tore
						// zurückgesetzt
						this.bearbeiteteToreLauf1.clear();
						this.bearbeiteteToreLauf2.clear();

						this.popup.close();
					});

					popupLayoutForm.addComponent(hl);
					popupLayoutForm.addComponent(btnSpeichern);
					popupLayoutForm.addStyleName("popup");
					this.popup.setContent(popupLayoutForm);
					this.ui.addWindow(this.popup);
				});

				// Tabelle mit ihren Werten abfüllen
				row.getItemProperty(COLUMN_NR).setValue(Integer.toString(fr.getStartnummer()));
				row.getItemProperty(COLUMN_NAME).setValue(fr.getFahrer().getName() + " " + fr.getFahrer().getVorname());
				row.getItemProperty(COLUMN_CLUB).setValue(fr.getFahrer().getClub().getName());
				row.getItemProperty(COLUMN_BEARBEITEN).setValue(bearbeiten);
			}

			this.tabelleFahrer.setPageLength(angemeldeteFahrer.size());
			this.tabelleLayout.addComponent(this.tabelleFahrer);

		} else {
			// Wenn keine Fahrer geladen wurden
			this.tabelleLayout.addComponent(new Label("Keine Angemeldeten Fahrer für diese Kategorie"));
		}
	}

	/**
	 * 
	 * 
	 * @param fahrerID
	 *            - Fahrer Identifikationsnummer
	 * @param lauf
	 *            - 1 oder 2
	 * @return - Tabelle mit allen Toren für die Fehlererfassung
	 */
	@SuppressWarnings("unchecked") //Cast cecked "von Hand"
	private Table generiereTabelleFehlererfassung(Integer fahrerID, int lauf) {
		Table tabelle = new Table();
		tabelle.setImmediate(true);
		tabelle.setWidth("100%");

		tabelle.addContainerProperty(COLUMN_TOR, Integer.class, null);
		tabelle.addContainerProperty(COLUMN_BERUEHRT, CheckBox.class, null);
		tabelle.addContainerProperty(COLUMN_VERFEHLT, CheckBox.class, null);

		List<Strafzeit> strafzeitliste = new ArrayList<Strafzeit>();
		Rennen aktRennen = (Rennen) this.dropDownRennen.getValue();
		int anzTore = aktRennen.getAnzTore();

		AltersKategorie aktKategorie = (AltersKategorie) this.dropDownAlterskategorie.getValue();
		// Strafzeiten aus der Datenbank laden
		strafzeitliste = this.fehlererfassungsController.ladeStrafzeitliste(fahrerID, aktRennen.getRennenID(),
				aktKategorie.getAltersKategorieID(), lauf);

		// Iteration für alle Tore
		int j = 0;
		for (int i = 0; i < anzTore; i++) {
			boolean beruehrt = false;
			boolean verpasst = false;
			Object newItemId = tabelle.addItem();
			Item row = tabelle.getItem(newItemId);
			Integer tor = (Integer) i + 1;

			if (strafzeitliste.size() > j && strafzeitliste.get(j).getTorNummer() == i + 1) {
				beruehrt = strafzeitliste.get(j).isBeruehrt();
				verpasst = strafzeitliste.get(j).isVerpasst();
				j++;
			}

			CheckBox cBeruehrt = new CheckBox("", beruehrt);
			CheckBox cVerpasst = new CheckBox("", verpasst);

			// Event damit man weiss welche Tore bearbeitet wurden
			cBeruehrt.addValueChangeListener(evt -> {
				checkBoxEvent(lauf, tor);
			});

			// Event damit man weiss welche Tore bearbeitet wurden
			cVerpasst.addValueChangeListener(evte -> {
				checkBoxEvent(lauf, tor);
			});

			cBeruehrt.setImmediate(true);
			cVerpasst.setImmediate(true);

			// Tabelle mit ihren Werten abfüllen
			row.getItemProperty(COLUMN_TOR).setValue(tor);
			row.getItemProperty(COLUMN_BERUEHRT).setValue(cBeruehrt);
			row.getItemProperty(COLUMN_VERFEHLT).setValue(cVerpasst);
		}
		return tabelle;
	}

	/**
	 * Setzt den CheckBox Event. Wenn eine Checkbox für eine Fehlererfassung
	 * angeklickt wurde, muss das Tor vermerkt werden. So werden später nur die
	 * Strafzeiten in die Datenbank geschrieben, die auch bearbeitet wurden.
	 * 
	 * @param lauf
	 *            - 1 oder 2
	 * @param tor
	 *            - jeweiliges Tor
	 */
	private void checkBoxEvent(Integer lauf, Integer tor) {
		if (lauf.intValue() == 1) {
			if (!this.bearbeiteteToreLauf1.contains(tor)) {
				this.bearbeiteteToreLauf1.add(tor);
			}
		} else {
			if (!this.bearbeiteteToreLauf2.contains(tor)) {
				this.bearbeiteteToreLauf2.add(tor);
			}
		}
	}

	/**
	 * Funktion gibt zurück ob die View bereits initialisiert wurde.
	 * 
	 * @return
	 */
	@Override
	public boolean istInitialisiert() {
		return this.init;
	}
}
