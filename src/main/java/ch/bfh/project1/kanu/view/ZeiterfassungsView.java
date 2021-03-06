package ch.bfh.project1.kanu.view;

import java.util.List;

import ch.bfh.project1.kanu.controller.ValidierungsController;
import ch.bfh.project1.kanu.controller.ZeiterfassungsController;
import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Fahrer;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rennen;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Hier kann der Zeitnehmer die Laufzeit für den Fahrer eingeben und bearbeiten.
 * Als Übersicht wird ihm eine Tabelle, aller startenden Fahrern, angezeigt. Mit
 * dem Suchfeld kann er den aktuellen Fahrer suchen und die Laufzeit erfassen
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */
public class ZeiterfassungsView implements ViewTemplate {

	private boolean init = false; // Ist die View initialisiert

	// Kontroller
	private ZeiterfassungsController zController = new ZeiterfassungsController();

	// UI Komponenten
	private Label titel = new Label("Zeit erfassen");
	private TextField fahrerSuche = new TextField();
	private Table table = new Table();
	private HorizontalLayout tableLayout = new HorizontalLayout();
	private FormLayout zeiterfassungsLayout = new FormLayout();
	private List<Rennen> rennen;
	private List<AltersKategorie> kategorien;
	private NativeSelect rennenSelect = new NativeSelect("Rennen");
	private NativeSelect kategorienSelect = new NativeSelect("Kategorie");
	private HorizontalLayout filterLayout = new HorizontalLayout();
	private UI ui; // Haupt GUI

	// member variabel: Popup fenster
	private Window popup;
	private TextField startnummerText = new TextField("Startnummer");
	private TextField vornameText = new TextField("Vorname");
	private TextField nachnameText = new TextField("Nachname");
	private TextField clubText = new TextField("Club");
	private Label laufzeiten = new Label("Laufzeiten");
	private TextField laufzeitEins = new TextField("1. Laufzeit");
	private TextField laufzeitZwei = new TextField("2. Laufzeit");

	// Konstanten
	private static final String COLUMN_STARTNUMMER = "Startnummer";
	private static final String COLUMN_VORNAME = "Vorname";
	private static final String COLUMN_NACHNAME = "Nachname";
	private static final String COLUMN_JAHRGANG = "Jahrgang";
	private static final String COLUMN_ORT = "Ort";
	private static final String COLUMN_BUTTON = "Zeit erfassen";

	/**
	 * Konstruktor: ZeiterfassungsView
	 * 
	 * @param ui
	 *            - Haupt GUI
	 */
	public ZeiterfassungsView(UI ui) {
		this.ui = ui;
	}

	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		this.zeiterfassungsLayout.setSpacing(true);
		this.titel.setStyleName("h2");
		this.laufzeiten.setStyleName("h3");

		this.fahrerSuche.setInputPrompt("Suchen");
		this.fahrerSuche.setStyleName("search");

		// Tabellenstruktur
		this.table.addContainerProperty(COLUMN_STARTNUMMER, Integer.class, null);
		this.table.addContainerProperty(COLUMN_VORNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_NACHNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_JAHRGANG, Integer.class, null);
		this.table.addContainerProperty(COLUMN_ORT, String.class, null);
		this.table.addContainerProperty(COLUMN_BUTTON, Button.class, null);

		this.table.setWidth("100%");

		// Die folgenden Felder können nicht bearbeitet werden
		this.startnummerText.setEnabled(false);
		this.vornameText.setEnabled(false);
		this.nachnameText.setEnabled(false);
		this.clubText.setEnabled(false);

		// Hinweis wie das Zeitformat aussieht
		this.laufzeitEins.setInputPrompt("Minuten:Sekunden.Milisekunden");
		this.laufzeitZwei.setInputPrompt("Minuten:Sekunden.Milisekunden");

		// Validierung zu den Laufzeitfelder hinzufügen
		ValidierungsController.laufzeitValidation(this.laufzeitEins);
		ValidierungsController.laufzeitValidation(this.laufzeitZwei);

		// Popup Window
		this.popup = new Window("Zeit erfassen");
		this.popup.center();
		this.popup.setModal(true);
		this.popup.setWidth("400px");

		this.rennen = this.zController.ladeAlleRennen();
		this.kategorien = this.zController.ladeAlleKategorien();

		// Rennen Dropdown mit Werten abfüllen
		for (Rennen r : this.rennen) {
			this.rennenSelect.addItem(r);
			if (this.rennenSelect.getValue() == null) {
				this.rennenSelect.setValue(r);
			}
		}

		// Event wenn die Kategorie geändert hat
		this.kategorienSelect.addValueChangeListener(event -> {
			AltersKategorie kat = (AltersKategorie) this.kategorienSelect.getValue();
			Rennen rennen = (Rennen) this.rennenSelect.getValue();
			tabelleAbfuellen(rennen.getRennenID(), kat.getAltersKategorieID());
		});

		// Event wenn das Rennen geändert hat
		this.rennenSelect.addValueChangeListener(event -> {
			AltersKategorie kat = (AltersKategorie) this.kategorienSelect.getValue();
			Rennen rennen = (Rennen) this.rennenSelect.getValue();
			tabelleAbfuellen(rennen.getRennenID(), kat.getAltersKategorieID());
		});

		// Kategorien in Dropdown füllen
		for (AltersKategorie kat : this.kategorien) {
			this.kategorienSelect.addItem(kat);
			if (this.kategorienSelect.getValue() == null) {
				this.kategorienSelect.setValue(kat);
			}
		}

		// Event für die Suche
		this.fahrerSuche.addValueChangeListener(event -> {
			AltersKategorie kat = (AltersKategorie) this.kategorienSelect.getValue();
			Rennen rennen = (Rennen) this.rennenSelect.getValue();
			tabelleAbfuellen(rennen.getRennenID(), kat.getAltersKategorieID());
		});

		this.kategorienSelect.setNullSelectionAllowed(false);
		this.rennenSelect.setNullSelectionAllowed(false);

		this.filterLayout.addComponent(this.rennenSelect);
		this.filterLayout.addComponent(this.kategorienSelect);

		// Komponenten zum Layout hinzufügen
		this.zeiterfassungsLayout.addComponent(this.titel);
		this.zeiterfassungsLayout.addComponent(this.filterLayout);
		this.zeiterfassungsLayout.addComponent(this.fahrerSuche);
		this.zeiterfassungsLayout.addComponent(this.tableLayout);

		this.init = true; // View ist initialisiert
	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@Override
	public void viewAnzeigen(Component inhalt) {
		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(this.zeiterfassungsLayout);
	}

	/**
	 * Funktion füllt die Tabelle mit allen Fahrer des Clubs ab.
	 */
	@SuppressWarnings("unchecked") //Cast ckecked "von Hand"
	private void tabelleAbfuellen(Integer rennenID, Integer kategorieID) {
		// Alle Tabellen einträge löschen
		this.tableLayout.removeAllComponents();
		this.table.removeAllItems();
		List<FahrerResultat> fahrer = null;

		if (this.fahrerSuche.getValue() == null || this.fahrerSuche.getValue().equals("")) {
			// Angemeldete Fahrer mit Kategorie laden (ohne Suche)
			fahrer = this.zController.ladeAngemeldeteFahrerMitKategorie(rennenID, kategorieID);
		} else {
			// Angemeldete Fahrer mit Suche
			fahrer = this.zController.ladeAngemeldeteFahrerMitKategorieMitSuche(rennenID, kategorieID,
					this.fahrerSuche.getValue());
		}

		if (!fahrer.isEmpty()) {

			// Angemeldete Fahrer iterieren
			for (FahrerResultat fr : fahrer) {
				Fahrer f = fr.getFahrer();

				Object newItemId = this.table.addItem();
				Item row = this.table.getItem(newItemId);

				Button speichern = new Button("Speichern");

				// Speichern Event
				speichern.addClickListener(event -> {
					if (this.laufzeitEins.isValid() && this.laufzeitZwei.isValid()) {
						Integer zeit1 = null;
						Integer zeit2 = null;

						if (!this.laufzeitEins.getValue().equals("")) {
							zeit1 = this.zController.getLaufzeitInMilisekunden(this.laufzeitEins.getValue());
						}
						if (!this.laufzeitZwei.getValue().equals("")) {
							zeit2 = this.zController.getLaufzeitInMilisekunden(this.laufzeitZwei.getValue());
						}

						fr.setZeitErsterLauf(zeit1);
						fr.setZeitZweiterLauf(zeit2);

						// Zeit erfassen und in die Datenbank schreiben
						this.zController.zeitErfassen(f, fr);
						this.popup.close();

					}
				});

				Button zeitErfassen = new Button("Zeit erfassen");

				// Event für Popup
				zeitErfassen.addClickListener(event -> {

					// Angemeldete Fahrer aus der Datenbank lesen
					FahrerResultat newFahrerResultat = this.zController.ladeAngemeldetenFahrer(f.getFahrerID(),
							kategorieID, rennenID);

					FormLayout popupLayoutForm = new FormLayout();
					this.startnummerText.setValue(Integer.toString(fr.getStartnummer()));
					this.vornameText.setValue(f.getVorname());
					this.nachnameText.setValue(f.getName());
					this.clubText.setValue(f.getClub().getName());
					if (fr.getZeitErsterLauf() != null) {
						this.laufzeitEins
								.setValue(this.zController.getLaufzeitInFormat(newFahrerResultat.getZeitErsterLauf()));
					} else {
						this.laufzeitEins.setValue("");
					}
					if (fr.getZeitErsterLauf() != null) {
						this.laufzeitZwei
								.setValue(this.zController.getLaufzeitInFormat(newFahrerResultat.getZeitZweiterLauf()));
					} else {
						this.laufzeitZwei.setValue("");
					}

					// Popup mit Komponenten abfüllen
					popupLayoutForm.addComponent(this.startnummerText);
					popupLayoutForm.addComponent(this.vornameText);
					popupLayoutForm.addComponent(this.nachnameText);
					popupLayoutForm.addComponent(this.clubText);
					popupLayoutForm.addComponent(this.laufzeiten);
					popupLayoutForm.addComponent(this.laufzeitEins);
					popupLayoutForm.addComponent(this.laufzeitZwei);
					popupLayoutForm.addComponent(speichern);
					popupLayoutForm.addStyleName("popup");

					this.popup.setContent(popupLayoutForm);

					this.ui.addWindow(this.popup);
				});

				// Tabelle mit Werten abfüllen
				row.getItemProperty(COLUMN_STARTNUMMER).setValue(fr.getStartnummer());
				row.getItemProperty(COLUMN_VORNAME).setValue(f.getVorname());
				row.getItemProperty(COLUMN_NACHNAME).setValue(f.getName());
				row.getItemProperty(COLUMN_JAHRGANG).setValue(f.getJahrgang());
				row.getItemProperty(COLUMN_ORT).setValue(f.getOrt());
				row.getItemProperty(COLUMN_BUTTON).setValue(zeitErfassen);

				this.table.setPageLength(fahrer.size());
				this.table.setWidth("100%");
				this.tableLayout.addComponent(this.table);
			}
		} else {
			this.tableLayout.addComponent(new Label("Keine angemeldeten Fahrer für diese Kategorie"));
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
