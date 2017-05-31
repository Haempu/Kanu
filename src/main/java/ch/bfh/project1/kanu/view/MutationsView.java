package ch.bfh.project1.kanu.view;

import java.util.ArrayList;
import java.util.List;

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
import com.vaadin.ui.Window;

import ch.bfh.project1.kanu.controller.MutationsController;
import ch.bfh.project1.kanu.controller.ValidierungsController;
import ch.bfh.project1.kanu.model.Club;
import ch.bfh.project1.kanu.model.Fahrer;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class MutationsView implements ViewTemplate {

	private boolean init = false;

	private UI ui; // Haupt GUI

	// member variabeln: Übersichtstabelle
	private Label titel = new Label("Fahrerverwaltung");
	private TextField fahrerSuche = new TextField();
	private Table table = new Table();
	private FormLayout fahrerVerwaltungsLayout = new FormLayout();
	private Button neuerFahrer = new Button("Neuer Fahrer");

	// member variabel: Popup fenster
	private Window popup;
	private TextField vornameText = new TextField("Vorname");
	private TextField nachnameText = new TextField("Nachname");
	private TextField jahrgangText = new TextField("Jahrgang");
	private TextField strasseText = new TextField("Strasse");
	private TextField plzText = new TextField("Postleitzahl");
	private TextField ortText = new TextField("Ort");
	private TextField telefonNrText = new TextField("Telefonnummer");
	private NativeSelect clubs = new NativeSelect("Klub");

	// Controller
	private MutationsController mController = new MutationsController();

	// Konstanten
	private static final String COLUMN_VORNAME = "Vorname";
	private static final String COLUMN_NACHNAME = "Nachname";
	private static final String COLUMN_JAHRGANG = "Jahrgang";
	private static final String COLUMN_ORT = "Ort";
	private static final String COLUMN_BUTTON = "Bearbeiten";
	private static final String COLUMN_LOESCHEN = "Löschen";

	/**
	 * Konstruktor: MutationsView
	 * 
	 * @param ui
	 */
	public MutationsView(UI ui) {
		this.ui = ui;
	}

	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		this.fahrerVerwaltungsLayout.setSpacing(true);
		this.titel.setStyleName("h2");

		this.fahrerSuche.setInputPrompt("Suchen");
		this.fahrerSuche.setStyleName("search");

		this.table.addContainerProperty(COLUMN_VORNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_NACHNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_JAHRGANG, Integer.class, null);
		this.table.addContainerProperty(COLUMN_ORT, String.class, null);
		this.table.addContainerProperty(COLUMN_BUTTON, Button.class, null);
		this.table.addContainerProperty(COLUMN_LOESCHEN, Button.class, null);

		this.table.setWidth(100L, Component.UNITS_PERCENTAGE);
		this.table.setImmediate(true);
		this.fahrerVerwaltungsLayout.setImmediate(true);

		this.clubs.setNullSelectionAllowed(false);
		List<Club> cl = this.mController.ladeAlleClubs();

		for (Club c : cl) {
			this.clubs.addItem(c);

			if (this.clubs.getValue() == null) {
				this.clubs.setValue(c);
			}
		}

		tabelleAbfuellen();

		Button speichern = new Button("Speichern");
		popupEvent(this.neuerFahrer, speichern, null);
		fahrerSpeichernEvent(speichern, null, null, true);

		ValidierungsController.setTextFeldRequired(this.nachnameText);
		ValidierungsController.setTextFeldRequired(this.vornameText);
		ValidierungsController.setTextFeldRequired(this.ortText);
		ValidierungsController.setTextFeldRequired(this.plzText);
		ValidierungsController.setTextFeldRequired(this.strasseText);
		ValidierungsController.setTextFeldRequired(this.telefonNrText);
		ValidierungsController.setTextFeldRequired(this.jahrgangText);
		ValidierungsController.checkIfInteger(this.jahrgangText);

		// TODO: add benutzerrolle

		// if
		// (SessionController.getBenutzerRolle().equals(BenutzerRolle.ROLLE_RECHNUNG))
		// {
		// this.popupLayoutMaster.addComponent(this.clubs);
		// }

		this.fahrerSuche.addValueChangeListener(event -> {
			// Rennen rennen = (Rennen) this.aktuellesRennen.getValue();
			// fahrerTabelleAbfuellen(rennen.getRennenID());
			tabelleAbfuellen();
		});

		this.popup = new Window("Fahrer verwalten");
		this.popup.center();
		this.popup.setModal(true);
		this.popup.setWidth("400px");

		this.fahrerVerwaltungsLayout.addComponent(this.titel);
		this.fahrerVerwaltungsLayout.addComponent(this.neuerFahrer);
		this.fahrerVerwaltungsLayout.addComponent(this.fahrerSuche);
		this.fahrerVerwaltungsLayout.addComponent(this.table);

		this.init = true;
	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@Override
	public void viewAnzeigen(Component inhalt) {
		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(this.fahrerVerwaltungsLayout);
	}

	/**
	 * Funktion füllt die Tabelle mit allen Clubs ab.
	 */
	private void tabelleAbfuellen() {
		ArrayList<Fahrer> fahrer;
		this.table.removeAllItems();

		// if
		// (SessionController.getBenutzerRolle().equals(BenutzerRolle.ROLLE_RECHNUNG))
		// {

		if (this.fahrerSuche.getValue() == null || this.fahrerSuche.getValue().equals("")) {
			fahrer = this.mController.ladeFahrermutationslisteAlle();
		} else {
			fahrer = this.mController.ladeFahrermutationslisteAlleMitSuche(this.fahrerSuche.getValue());
		}

		// } else {
		// TODO: change club id
		// fahrer = this.mController.ladeFahrermutationslisteClub(1);
		// }

		for (Fahrer f : fahrer) {
			Object newItemId = this.table.addItem();
			Item row = this.table.getItem(newItemId);

			Button bearbeiten = new Button("Bearbeiten");
			Button loeschen = new Button("Loeschen");
			Button speichern = new Button("Speichern");

			fahrerSpeichernEvent(speichern, row, f, false);
			popupEvent(bearbeiten, speichern, f);
			loeschenEvent(loeschen, f, row);

			row.getItemProperty(COLUMN_VORNAME).setValue(f.getVorname());
			row.getItemProperty(COLUMN_NACHNAME).setValue(f.getName());
			row.getItemProperty(COLUMN_JAHRGANG).setValue(f.getJahrgang());
			row.getItemProperty(COLUMN_ORT).setValue(f.getOrt());
			row.getItemProperty(COLUMN_BUTTON).setValue(bearbeiten);
			row.getItemProperty(COLUMN_LOESCHEN).setValue(loeschen);
		}

		this.table.setPageLength(fahrer.size());
	}

	/**
	 * Fahrer Speichern Event
	 * 
	 * @param speichern
	 * @param row
	 * @param f
	 * @param neuerFahrer
	 * @param bearbeiten
	 */
	private void fahrerSpeichernEvent(Button speichern, Item row, Fahrer f, boolean neuerFahrer) {
		speichern.addClickListener(event -> {
			if (textfelderValidieren()) {

				Club club = (Club) this.clubs.getValue();
				Fahrer newFahrer = new Fahrer(this.nachnameText.getValue(), this.vornameText.getValue(),
						Integer.parseInt(this.jahrgangText.getValue()), this.telefonNrText.getValue(),
						this.strasseText.getValue(), Integer.parseInt(this.plzText.getValue()), this.ortText.getValue(),
						club);

				if (!neuerFahrer) {
					newFahrer.setFahrerID(f.getFahrerID());
					this.mController.speichereFahrerBearbeitenAlle(newFahrer);

					row.getItemProperty(COLUMN_VORNAME).setValue(newFahrer.getVorname());
					row.getItemProperty(COLUMN_NACHNAME).setValue(newFahrer.getName());
					row.getItemProperty(COLUMN_JAHRGANG).setValue(newFahrer.getJahrgang());
					row.getItemProperty(COLUMN_ORT).setValue(newFahrer.getOrt());

				} else {
					Object newItemId = this.table.addItem();
					Item newRow = this.table.getItem(newItemId);

					Integer fahrerID = this.mController.speichereNeuenFahrer(newFahrer);
					newFahrer.setFahrerID(fahrerID);

					Button newBearbeiten = new Button("Bearbeiten");
					popupEvent(newBearbeiten, speichern, newFahrer);

					Button newLoeschen = new Button("Löschen");
					loeschenEvent(newLoeschen, newFahrer, newRow);

					newRow.getItemProperty(COLUMN_VORNAME).setValue(newFahrer.getVorname());
					newRow.getItemProperty(COLUMN_NACHNAME).setValue(newFahrer.getName());
					newRow.getItemProperty(COLUMN_JAHRGANG).setValue(newFahrer.getJahrgang());
					newRow.getItemProperty(COLUMN_ORT).setValue(newFahrer.getOrt());
					newRow.getItemProperty(COLUMN_BUTTON).setValue(newBearbeiten);
					newRow.getItemProperty(COLUMN_LOESCHEN).setValue(newLoeschen);

				}

				this.popup.close();
			}

			// TODO: add this
			/*
			 * if (SessionController.getBenutzerRolle().equals(BenutzerRolle.
			 * ROLLE_RECHNUNG)) { fahrer.setClub((Club) this.clubs.getValue());
			 * this.mController.speichereFahrerBearbeitenAlle(fahrer); } else {
			 * this.mController.speichereFahrerBearbeitenClub(fahrer); }
			 */
		});

	}

	/**
	 * Event für das Popup Fenster
	 * 
	 * @param popupButton
	 * @param speichern
	 * @param f
	 */
	private void popupEvent(Button popupButton, Button speichern, Fahrer f) {
		popupButton.addClickListener(event -> {

			if (f != null) {
				Fahrer bFahrer = this.mController.ladeFahrer(f.getFahrerID());
				fahrerFelderAbfuellen(bFahrer);
			} else {
				this.vornameText.setValue("");
				this.nachnameText.setValue("");
				this.jahrgangText.setValue("");
				this.strasseText.setValue("");
				this.plzText.setValue("");
				this.ortText.setValue("");
				this.telefonNrText.setValue("");
			}

			FormLayout popupLayoutMaster = new FormLayout();
			popupLayoutMaster.addComponent(this.vornameText);
			popupLayoutMaster.addComponent(this.nachnameText);
			popupLayoutMaster.addComponent(this.jahrgangText);
			popupLayoutMaster.addComponent(this.strasseText);
			popupLayoutMaster.addComponent(this.plzText);
			popupLayoutMaster.addComponent(this.ortText);
			popupLayoutMaster.addComponent(this.telefonNrText);
			popupLayoutMaster.addComponent(this.clubs);
			popupLayoutMaster.addComponent(speichern);

			popupLayoutMaster.addStyleName("popup");
			this.popup.setContent(popupLayoutMaster);

			// TODO: add benutzerrolle
			/*
			 * if (SessionController.getBenutzerRolle().equals(BenutzerRolle.
			 * ROLLE_RECHNUNG)) { this.clubs.setValue(f.getClub()); }
			 */

			this.ui.addWindow(this.popup);
		});
	}

	/**
	 * Setzt das Geschehen eines Lösch Events.
	 * 
	 * @param loeschen
	 * @param f
	 * @param itemId
	 */
	public void loeschenEvent(Button loeschen, Fahrer f, Object itemId) {
		loeschen.addClickListener(event -> {
			this.mController.fahrerLoeschen(f.getFahrerID());
			tabelleAbfuellen();
			// TODO: performance
			// this.table.removeItem(itemId);
			// this.table.setImmediate(true);
		});
	}

	/**
	 * Füllt das Fahrer Popup Formular ab.
	 * 
	 * @param f
	 */
	private void fahrerFelderAbfuellen(Fahrer f) {
		this.vornameText.setValue(f.getVorname());
		this.nachnameText.setValue(f.getName());
		this.jahrgangText.setValue(Integer.toString(f.getJahrgang()));
		this.strasseText.setValue(f.getStrasse());
		this.plzText.setValue(Integer.toString(f.getPlz()));
		this.ortText.setValue(f.getOrt());
		this.telefonNrText.setValue(f.getTelNr());
		this.clubs.setValue(f.getClub());
	}

	/**
	 * Validiert die Textfelder
	 * 
	 * @return
	 */
	private boolean textfelderValidieren() {
		if (this.vornameText.isValid() && this.nachnameText.isValid() && this.jahrgangText.isValid()
				&& this.strasseText.isValid() && this.plzText.isValid() && this.ortText.isValid()
				&& this.telefonNrText.isValid() && this.clubs.isValid()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean istInitialisiert() {
		return this.init;
	}

}
