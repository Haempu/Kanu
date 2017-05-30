package ch.bfh.project1.kanu.view;

import java.util.ArrayList;

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
import ch.bfh.project1.kanu.model.Fahrer;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class MutationsView implements ViewTemplate {

	private UI ui; // Haupt GUI

	// member variabeln: Übersichtstabelle
	private Label titel = new Label("Fahrerverwaltung");
	private Table table = new Table();
	private FormLayout fahrerVerwaltungsLayout = new FormLayout();

	// member variabel: Popup fenster
	private Window popup;
	private FormLayout popupLayoutMaster = new FormLayout();
	private TextField vornameText = new TextField("Vorname");
	private TextField nachnameText = new TextField("Nachname");
	private TextField jahrgangText = new TextField("Jahrgang");
	private TextField strasseText = new TextField("Strasse");
	private TextField plzText = new TextField("Postleitzahl");
	private TextField ortText = new TextField("Ort");
	private TextField telefonNrText = new TextField("Telefonnummer");
	private NativeSelect clubs = new NativeSelect("Klub");
	private Button speichern = new Button("Speichrn");

	// Controller
	private MutationsController mController = new MutationsController();

	// Konstanten
	private static final String COLUMN_VORNAME = "Vorname";
	private static final String COLUMN_NACHNAME = "Nachname";
	private static final String COLUMN_JAHRGANG = "Jahrgang";
	private static final String COLUMN_ORT = "Ort";
	private static final String COLUMN_BUTTON = "Bearbeiten";

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

		this.table.addContainerProperty(COLUMN_VORNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_NACHNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_JAHRGANG, Integer.class, null);
		this.table.addContainerProperty(COLUMN_ORT, String.class, null);
		this.table.addContainerProperty(COLUMN_BUTTON, Button.class, null);

		tabelleAbfuellen();

		this.speichern.addClickListener(event -> {
			// TODO: Validation
			Fahrer fahrer = new Fahrer(this.nachnameText.getValue(), this.vornameText.getValue(),
					Integer.parseInt(this.jahrgangText.getValue()), this.telefonNrText.getValue(),
					this.strasseText.getValue(), Integer.parseInt(this.plzText.getValue()), this.ortText.getValue());

			this.popup.close();
			// TODO: add this
			/*
			 * if (SessionController.getBenutzerRolle().equals(BenutzerRolle.
			 * ROLLE_RECHNUNG)) { fahrer.setClub((Club) this.clubs.getValue());
			 * this.mController.speichereFahrerBearbeitenAlle(fahrer); } else {
			 * this.mController.speichereFahrerBearbeitenClub(fahrer); }
			 */
		});

		this.popupLayoutMaster.addComponent(this.vornameText);
		this.popupLayoutMaster.addComponent(this.nachnameText);
		this.popupLayoutMaster.addComponent(this.jahrgangText);
		this.popupLayoutMaster.addComponent(this.strasseText);
		this.popupLayoutMaster.addComponent(this.plzText);
		this.popupLayoutMaster.addComponent(this.ortText);
		this.popupLayoutMaster.addComponent(this.telefonNrText);

		// TODO: add benutzerrolle

		// if
		// (SessionController.getBenutzerRolle().equals(BenutzerRolle.ROLLE_RECHNUNG))
		// {
		// this.popupLayoutMaster.addComponent(this.clubs);
		// }

		this.popupLayoutMaster.addComponent(this.speichern);

		this.popup = new Window("Fahrer verwalten");
		this.popupLayoutMaster.addStyleName("popup");

		this.popup.setContent(this.popupLayoutMaster);
		this.popup.center();
		this.popup.setModal(true);

		this.fahrerVerwaltungsLayout.addComponent(this.titel);
		this.fahrerVerwaltungsLayout.addComponent(this.table);
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

		// if
		// (SessionController.getBenutzerRolle().equals(BenutzerRolle.ROLLE_RECHNUNG))
		// {
		fahrer = this.mController.ladeFahrermutationslisteAlle();
		// } else {
		// TODO: change club id
		// fahrer = this.mController.ladeFahrermutationslisteClub(1);
		// }

		for (Fahrer f : fahrer) {
			Object newItemId = this.table.addItem();
			Item row = this.table.getItem(newItemId);

			Button bearbeiten = new Button("Bearbeiten");

			bearbeiten.addClickListener(event -> {
				this.vornameText.setValue(f.getVorname());
				this.nachnameText.setValue(f.getName());
				this.jahrgangText.setValue(Integer.toString(f.getJahrgang()));
				this.strasseText.setValue(f.getStrasse());
				this.plzText.setValue(Integer.toString(f.getPlz()));
				this.ortText.setValue(f.getOrt());
				this.telefonNrText.setValue(f.getTelNr());

				// TODO: add benutzerrolle
				/*
				 * if
				 * (SessionController.getBenutzerRolle().equals(BenutzerRolle.
				 * ROLLE_RECHNUNG)) { this.clubs.setValue(f.getClub()); }
				 */

				this.ui.addWindow(this.popup);
			});

			row.getItemProperty(COLUMN_VORNAME).setValue(f.getVorname());
			row.getItemProperty(COLUMN_NACHNAME).setValue(f.getName());
			row.getItemProperty(COLUMN_JAHRGANG).setValue(f.getJahrgang());
			row.getItemProperty(COLUMN_ORT).setValue(f.getOrt());
			row.getItemProperty(COLUMN_BUTTON).setValue(bearbeiten);
		}

		this.table.setPageLength(fahrer.size());
	}

}
