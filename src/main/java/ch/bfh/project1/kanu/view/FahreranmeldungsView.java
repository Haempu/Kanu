package ch.bfh.project1.kanu.view;

import java.util.List;

import ch.bfh.project1.kanu.controller.FahreranmeldungsController;
import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Fahrer;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rennen;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Hier kann man alle Fahrer an- und auch wieder abmelden. Der Benutzer hat
 * vorerst eine Übersicht aller Fahrer/Innen.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */
public class FahreranmeldungsView implements ViewTemplate {

	private boolean init = false; // Ist die View initialisiert

	// UI Komponenten
	private Label titel = new Label("Fahreranmeldung");
	private VerticalLayout layoutFahrer = new VerticalLayout();
	private VerticalLayout layoutAngemeldeteFahrer = new VerticalLayout();
	private TextField fahrerSuche = new TextField();
	private Table fahrerTable = new Table();
	private Table angemeldeteFahrerTable = new Table();
	private FormLayout fahreranmeldungsLayout = new FormLayout();
	private UI ui;
	private NativeSelect aktuellesRennen = new NativeSelect("Rennen");
	private NativeSelect altersKategorien = new NativeSelect("Kategorie");

	// Membervariablen
	private List<AltersKategorie> kategorie;
	private List<Rennen> rennen;
	private boolean firstCall = false;

	// Kontroller
	private FahreranmeldungsController fController = new FahreranmeldungsController();

	// member variabel: Popup fenster
	private Window popup;
	private TextField vornameText = new TextField("Vorname");
	private TextField nachnameText = new TextField("Nachname");
	private TextField clubText = new TextField("Club");

	// Statische Variablen
	private static final String COLUMN_VORNAME = "Vorname";
	private static final String COLUMN_NACHNAME = "Nachname";
	private static final String COLUMN_JAHRGANG = "Jahrgang";
	private static final String COLUMN_ALTERSKATEGORIE = "Kategorie";
	private static final String COLUMN_ANMELDEN = "Anmelden";
	private static final String COLUMN_ABMELDEN = "Abmelden";
	private static final int ROW_HEIGHT = 5;

	/**
	 * Konstruktor: FahreranmeldungsView
	 * 
	 * @param ui
	 */
	public FahreranmeldungsView(UI ui) {
		this.ui = ui;
	}

	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		this.fahreranmeldungsLayout.setSpacing(true);
		this.titel.setValue("Fahreranmeldung");
		this.titel.setStyleName("h2");

		this.fahrerSuche.setInputPrompt("Suchen");
		this.fahrerSuche.setStyleName("search");

		Label label1 = new Label("Nicht angemeldete Fahrer");
		label1.setStyleName("h3");
		Label label2 = new Label("Angemeldete Fahrer");
		label2.setStyleName("h3");

		this.kategorie = this.fController.ladeAlleKategorien();
		this.rennen = this.fController.ladeAlleRennen();

		this.aktuellesRennen.setNullSelectionAllowed(false);

		this.fahrerTable.addContainerProperty(COLUMN_VORNAME, String.class, null);
		this.fahrerTable.addContainerProperty(COLUMN_NACHNAME, String.class, null);
		this.fahrerTable.addContainerProperty(COLUMN_JAHRGANG, Integer.class, null);
		this.fahrerTable.addContainerProperty(COLUMN_ANMELDEN, Button.class, null);

		this.angemeldeteFahrerTable.addContainerProperty(COLUMN_VORNAME, String.class, null);
		this.angemeldeteFahrerTable.addContainerProperty(COLUMN_NACHNAME, String.class, null);
		this.angemeldeteFahrerTable.addContainerProperty(COLUMN_JAHRGANG, Integer.class, null);
		this.angemeldeteFahrerTable.addContainerProperty(COLUMN_ALTERSKATEGORIE, NativeSelect.class, null);
		this.angemeldeteFahrerTable.addContainerProperty(COLUMN_ABMELDEN, Button.class, null);

		this.fahrerTable.setWidth("100%");
		this.angemeldeteFahrerTable.setWidth("100%");

		this.altersKategorien.setRequired(true);
		this.altersKategorien.setNullSelectionAllowed(false);
		this.vornameText.setEnabled(false);
		this.nachnameText.setEnabled(false);
		this.clubText.setEnabled(false);

		this.vornameText.setWidth("250px");
		this.nachnameText.setWidth("250px");
		this.clubText.setWidth("250px");

		this.popup = new Window("Fahrer anmelden");
		this.popup.center();
		this.popup.setModal(true);
		this.popup.setWidth("400px");

		// Wenn ein neues Rennen ausgewählt wird, wird die Tabelle neu abgefüllt
		this.aktuellesRennen.addValueChangeListener(event -> {
			if (this.aktuellesRennen.getValue() != null) {
				Rennen rennen = (Rennen) this.aktuellesRennen.getValue();

				if (!firstCall) {
					firstCall = true;
					this.layoutFahrer.removeAllComponents();

					if (fahrerTabelleAbfuellen(rennen.getRennenID())) {
						this.layoutFahrer.addComponent(this.fahrerSuche);
						this.layoutFahrer.addComponent(this.fahrerTable);
					} else {
						this.layoutFahrer.addComponent(new Label("Keine Fahrer vorhanden"));
					}
				}

				this.layoutAngemeldeteFahrer.removeAllComponents();
				zeigeAngemeldeteFahrer(rennen.getRennenID());

			} else {
				this.layoutFahrer.removeAllComponents();
				this.layoutAngemeldeteFahrer.removeAllComponents();
				this.layoutFahrer.addComponent(new Label("Kein Rennen ausgewählt"));
				this.layoutAngemeldeteFahrer.addComponent(new Label("Kein Rennen ausgewählt"));
			}
		});

		// Dropdown für alle Rennen abfüllen
		for (Rennen rn : this.rennen) {
			this.aktuellesRennen.addItem(rn);

			if (this.aktuellesRennen.getValue() == null) {
				this.aktuellesRennen.setValue(rn);
			}
		}

		// Event für die Suche
		this.fahrerSuche.addValueChangeListener(event -> {
			Rennen rennen = (Rennen) this.aktuellesRennen.getValue();
			fahrerTabelleAbfuellen(rennen.getRennenID());
		});

		this.layoutFahrer.setImmediate(true);
		this.layoutAngemeldeteFahrer.setImmediate(true);

		// Layout mit Komponenten abfüllen
		this.fahreranmeldungsLayout.addComponent(this.titel);
		this.fahreranmeldungsLayout.addComponent(this.aktuellesRennen);
		this.fahreranmeldungsLayout.addComponent(label1);
		this.fahreranmeldungsLayout.addComponent(this.layoutFahrer);
		this.fahreranmeldungsLayout.addComponent(label2);
		this.fahreranmeldungsLayout.addComponent(this.layoutAngemeldeteFahrer);
		this.fahreranmeldungsLayout.setImmediate(true);

		this.init = true; // Die View ist nun initlialisiert
	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@Override
	public void viewAnzeigen(Component inhalt) {
		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(this.fahreranmeldungsLayout);
	}

	/**
	 * Funktion füllt die Tabelle mit allen Fahrer des Clubs ab.
	 * 
	 * @param rennenID
	 *            - ausgewähltes Rennen
	 * @return - Wenn keine Werte in die Tabelle geschrieben werden, wird false
	 *         zurückgegeben
	 */
	@SuppressWarnings("unchecked") //Cast checked "von Hand" --> gelbe Markierung nicht schön!
	private boolean fahrerTabelleAbfuellen(Integer rennenID) {
		this.fahrerTable.removeAllItems();
		List<Fahrer> fahrer = null;

		if (this.fahrerSuche.getValue() == null || this.fahrerSuche.getValue().equals("")) {
			// Lade Fahrer ohne Suche
			fahrer = this.fController.ladeAlleFahrer();
		} else {
			// Lade Fahrer mit Suche
			fahrer = this.fController.ladeFahrerMitSuche(this.fahrerSuche.getValue());
		}

		if (!fahrer.isEmpty()) {

			// Alle Fahrer in die Tabelle abfüllen
			for (Fahrer f : fahrer) {
				Object newItemId = this.fahrerTable.addItem();
				Item row = this.fahrerTable.getItem(newItemId);

				// Alterskategorien hinzufügen
				for (AltersKategorie ak : this.kategorie) {
					this.altersKategorien.addItem(ak);
					if (this.altersKategorien.getValue() == null) {
						this.altersKategorien.setValue(ak);
					}
				}
				Button speichern = new Button("Speichern");

				// Speichern Event
				speichern.addClickListener(event -> {
					AltersKategorie ak = (AltersKategorie) altersKategorien.getValue();
					this.fController.fahrerAnmelden(f.getFahrerID(), rennenID, ak.getAltersKategorieID());
					zeigeAngemeldeteFahrer(rennenID);
					this.popup.close();
				});

				Button anmelden = new Button("Anmelden");

				// Anmelden Event (in der Tabelle)
				anmelden.addClickListener(event -> {

					FormLayout popupLayoutForm = new FormLayout();

					this.vornameText.setValue(f.getVorname());
					this.nachnameText.setValue(f.getName());
					this.clubText.setValue(f.getClub().getName());
					popupLayoutForm.addComponent(this.vornameText);
					popupLayoutForm.addComponent(this.nachnameText);
					popupLayoutForm.addComponent(this.clubText);

					popupLayoutForm.addComponent(altersKategorien);
					popupLayoutForm.addComponent(speichern);
					popupLayoutForm.addStyleName("popup");

					this.popup.setContent(popupLayoutForm);

					this.ui.addWindow(this.popup);
				});

				// Tabelle mit Werten abfüllen
				row.getItemProperty(COLUMN_VORNAME).setValue(f.getVorname());
				row.getItemProperty(COLUMN_NACHNAME).setValue(f.getName());
				row.getItemProperty(COLUMN_JAHRGANG).setValue(f.getJahrgang());
				row.getItemProperty(COLUMN_ANMELDEN).setValue(anmelden);
			}

			if (fahrer.size() < ROW_HEIGHT) {
				this.fahrerTable.setPageLength(fahrer.size());
			} else {
				this.fahrerTable.setPageLength(ROW_HEIGHT);
			}
		} else {
			return false; // keine Fahrer vorhanden
		}

		return true;
	}

	/**
	 * Funktion füllt die Tabelle mit allen angemeldeten Fahrer des Clubs ab.
	 *
	 * @param rennenID
	 *            - ausgewähltes Rennen
	 * @return - Wenn keine Werte in die Tabelle geschrieben werden, wird false
	 *         zurückgegeben
	 */
	@SuppressWarnings("unchecked") //Cast checked "von Hand" --> gelbe Markierung nicht schön
	private boolean angemeldeteFahrerTabelleAbfuellen(Integer rennenID) {
		this.angemeldeteFahrerTable.removeAllItems();

		// Angemeldete Fahrer aus der Datenbank lesen
		List<FahrerResultat> angemeldeteFahrer = this.fController.ladeAngemeldeteFahrer(rennenID);

		if (!angemeldeteFahrer.isEmpty()) {
			// Angemeldete Fahrer iterieren
			for (FahrerResultat fr : angemeldeteFahrer) {

				Fahrer f = fr.getFahrer();
				AltersKategorie kat = fr.getKategorie();

				Object newItemId = this.angemeldeteFahrerTable.addItem();
				Item row = this.angemeldeteFahrerTable.getItem(newItemId);

				NativeSelect altersKategorien = new NativeSelect("Kategorie");
				altersKategorien.setRequired(true);
				altersKategorien.setNullSelectionAllowed(false);

				NativeSelect rennen = new NativeSelect("Rennen");
				rennen.setRequired(true);

				for (AltersKategorie ak : this.kategorie) {
					altersKategorien.addItem(ak);

					if (ak.getAltersKategorieID() == kat.getAltersKategorieID()) {
						altersKategorien.setValue(ak);
					}
				}

				for (Rennen rn : this.rennen) {
					rennen.addItem(rn);
				}

				// Wenn die Kategorie geändert wurde soll dies in der Datenbank
				// gespeichert werden
				altersKategorien.addValueChangeListener(event -> {
					AltersKategorie neu = (AltersKategorie) altersKategorien.getValue();
					this.fController.fahrerFuerNeueKategorieAnmelden(f.getFahrerID(), kat.getAltersKategorieID(),
							neu.getAltersKategorieID(), rennenID);
				});

				Button abmelden = new Button("Abmelden");

				// Abmelden Event
				abmelden.addClickListener(event -> {
					this.fController.fahrerAbmelden(f.getFahrerID(), rennenID, kat.getAltersKategorieID());
					zeigeAngemeldeteFahrer(rennenID);
				});

				// Tabelle mit Werten abfüllen
				row.getItemProperty(COLUMN_VORNAME).setValue(f.getVorname());
				row.getItemProperty(COLUMN_NACHNAME).setValue(f.getName());
				row.getItemProperty(COLUMN_JAHRGANG).setValue(f.getJahrgang());
				row.getItemProperty(COLUMN_ALTERSKATEGORIE).setValue(altersKategorien);
				row.getItemProperty(COLUMN_ABMELDEN).setValue(abmelden);
			}
			if (angemeldeteFahrer.size() < ROW_HEIGHT) {
				this.angemeldeteFahrerTable.setPageLength(angemeldeteFahrer.size());
			} else {
				this.angemeldeteFahrerTable.setPageLength(ROW_HEIGHT);
			}
		} else {
			return false; // Keine angemeldeten Fahrer vorhanden
		}

		return true;
	}

	/**
	 * Die Funktion zeigt das entsprechende Layout für die angemeldeten Fahrer
	 * an.
	 * 
	 * @param rennenID
	 *            - ausgewähltes Rennen
	 */
	private void zeigeAngemeldeteFahrer(Integer rennenID) {
		this.layoutAngemeldeteFahrer.removeAllComponents();

		if (angemeldeteFahrerTabelleAbfuellen(rennenID)) {
			this.layoutAngemeldeteFahrer.addComponent(this.angemeldeteFahrerTable);
		} else {
			this.layoutAngemeldeteFahrer.addComponent(new Label("Keine Fahrer angemeldet"));
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
