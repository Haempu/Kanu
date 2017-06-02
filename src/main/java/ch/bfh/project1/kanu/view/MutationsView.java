package ch.bfh.project1.kanu.view;

import java.util.ArrayList;
import java.util.List;

import ch.bfh.project1.kanu.controller.MutationsController;
import ch.bfh.project1.kanu.controller.ValidierungsController;
import ch.bfh.project1.kanu.model.Club;
import ch.bfh.project1.kanu.model.Fahrer;

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

/**
 * Der Benutzer kann hier die Daten der Fahrer bearbeiten.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */
public class MutationsView implements ViewTemplate {

	private boolean init = false; // Ist die view initialisiert

	// UI Komponenten
	private Label titel = new Label("Fahrerverwaltung");
	private TextField fahrerSuche = new TextField();
	private Table table = new Table();
	private FormLayout fahrerVerwaltungsLayout = new FormLayout();
	private Button neuerFahrer = new Button("Neuer Fahrer");
	private UI ui; // Haupt GUI

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

	// Kontroller
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
	 *            - wird aufgrund des Popup Windows verwendet
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

		// Tabellenstruktur
		this.table.addContainerProperty(COLUMN_VORNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_NACHNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_JAHRGANG, Integer.class, null);
		this.table.addContainerProperty(COLUMN_ORT, String.class, null);
		this.table.addContainerProperty(COLUMN_BUTTON, Button.class, null);
		this.table.addContainerProperty(COLUMN_LOESCHEN, Button.class, null);

		this.table.setWidth("100%");
		this.table.setImmediate(true);
		this.fahrerVerwaltungsLayout.setImmediate(true);

		// Alle Klubs aus der Datenbank lesen
		List<Club> cl = this.mController.ladeAlleClubs();
		this.clubs.setNullSelectionAllowed(false);

		// Alle Klubs dem Dropdownfeld hinzufügen
		for (Club c : cl) {
			this.clubs.addItem(c);

			if (this.clubs.getValue() == null) {
				this.clubs.setValue(c);
			}
		}

		tabelleAbfuellen(); // Tabelle mit Fahrern abfüllen

		Button speichern = new Button("Speichern");
		popupEvent(this.neuerFahrer, speichern, null);
		fahrerSpeichernEvent(speichern, null, null, true);

		// Vaidierungen zu den Textfeldern hinzufügen
		ValidierungsController.setTextFeldRequired(this.nachnameText);
		ValidierungsController.setTextFeldRequired(this.vornameText);
		ValidierungsController.setTextFeldRequired(this.ortText);
		ValidierungsController.setTextFeldRequired(this.plzText);
		ValidierungsController.setTextFeldRequired(this.strasseText);
		ValidierungsController.setTextFeldRequired(this.telefonNrText);
		ValidierungsController.setTextFeldRequired(this.jahrgangText);
		ValidierungsController.checkIfInteger(this.jahrgangText);

		// Event für die Suche
		this.fahrerSuche.addValueChangeListener(event -> {
			tabelleAbfuellen();
		});

		// Popup Window initialisieren
		this.popup = new Window("Fahrer verwalten");
		this.popup.center();
		this.popup.setModal(true);
		this.popup.setWidth("400px");

		// Komponenten zum Layout hinzufügen
		this.fahrerVerwaltungsLayout.addComponent(this.titel);
		this.fahrerVerwaltungsLayout.addComponent(this.neuerFahrer);
		this.fahrerVerwaltungsLayout.addComponent(this.fahrerSuche);
		this.fahrerVerwaltungsLayout.addComponent(this.table);

		this.init = true; // Die View ist initialisiert
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
	 * Funktion füllt die Tabelle mit allen Fahrern ab.
	 */
	@SuppressWarnings("unchecked") //Cast checked "von Hand"
	private void tabelleAbfuellen() {
		ArrayList<Fahrer> fahrer;
		this.table.removeAllItems();

		if (this.fahrerSuche.getValue() == null || this.fahrerSuche.getValue().equals("")) {
			// Alle Fahrer ohne Suche
			fahrer = this.mController.ladeFahrermutationslisteAlle();
		} else {
			// Fahrer mit Suche
			fahrer = this.mController.ladeFahrermutationslisteAlleMitSuche(this.fahrerSuche.getValue());
		}

		// Durch alle Fahrer iterieren
		for (Fahrer f : fahrer) {
			Object newItemId = this.table.addItem();
			Item row = this.table.getItem(newItemId);

			Button bearbeiten = new Button("Bearbeiten");
			Button loeschen = new Button("Loeschen");
			Button speichern = new Button("Speichern");

			fahrerSpeichernEvent(speichern, row, f, false);
			popupEvent(bearbeiten, speichern, f);
			loeschenEvent(loeschen, f, row);

			// Tabelle mit Werten abfüllen
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
	 * Hier wird der Event für den Speichern Button im Popup definiert.
	 * 
	 * @param speichern
	 * @param row
	 * @param f
	 * @param neuerFahrer
	 * @param bearbeiten
	 */
	@SuppressWarnings("unchecked") //Cast checked "von Hand"
	private void fahrerSpeichernEvent(Button speichern, Item row, Fahrer f, boolean neuerFahrer) {
		// Speichern Event hinzufügen
		speichern.addClickListener(event -> {
			if (textfelderValidieren()) {

				Club club = (Club) this.clubs.getValue();
				Fahrer newFahrer = new Fahrer(this.nachnameText.getValue(), this.vornameText.getValue(),
						Integer.parseInt(this.jahrgangText.getValue()), this.telefonNrText.getValue(),
						this.strasseText.getValue(), Integer.parseInt(this.plzText.getValue()), this.ortText.getValue(),
						club);

				if (!neuerFahrer) {
					newFahrer.setFahrerID(f.getFahrerID());

					// Bearbeiteter Fahrer in die Datenbank speichern
					this.mController.speichereFahrerBearbeitenAlle(newFahrer);

					// Werte in die Tabelle füllen
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

					// Werte in die Tabelle füllen
					newRow.getItemProperty(COLUMN_VORNAME).setValue(newFahrer.getVorname());
					newRow.getItemProperty(COLUMN_NACHNAME).setValue(newFahrer.getName());
					newRow.getItemProperty(COLUMN_JAHRGANG).setValue(newFahrer.getJahrgang());
					newRow.getItemProperty(COLUMN_ORT).setValue(newFahrer.getOrt());
					newRow.getItemProperty(COLUMN_BUTTON).setValue(newBearbeiten);
					newRow.getItemProperty(COLUMN_LOESCHEN).setValue(newLoeschen);

				}

				this.popup.close();
			}
		});
	}

	/**
	 * Event für den berbeiten Button. Wird der Bearbeiten Button gedrückt
	 * erscheint ein PopUp.
	 * 
	 * @param popupButton
	 *            - Button pro Tabelleneintrag
	 * @param speichern
	 *            - Speicherbutton auf dem Popup
	 * @param f
	 *            - ausgewählter Fahrer
	 */
	private void popupEvent(Button popupButton, Button speichern, Fahrer f) {
		// Event für Bearbeiten Button
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

			// PopupLayout mit Komponenten abfüllen
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
		// Löesch-Button Event
		loeschen.addClickListener(event -> {
			this.mController.fahrerLoeschen(f.getFahrerID());
			tabelleAbfuellen();
		});
	}

	/**
	 * Füllt das Fahrer Popup Formular mit dem gegebenen Fahrer ab.
	 * 
	 * @param f
	 *            - ausgewählter Fahrer
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
	 * Validiert alle Textfelder.
	 * 
	 * @return - true wenn Validierung Ok ist.
	 */
	private boolean textfelderValidieren() {
		if (this.vornameText.isValid() && this.nachnameText.isValid() && this.jahrgangText.isValid()
				&& this.strasseText.isValid() && this.plzText.isValid() && this.ortText.isValid()
				&& this.telefonNrText.isValid() && this.clubs.isValid()) {
			return true;
		}
		return false;
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
