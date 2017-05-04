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
import ch.bfh.project1.kanu.controller.SessionController;
import ch.bfh.project1.kanu.model.Benutzer.BenutzerRolle;
import ch.bfh.project1.kanu.model.Fahrer;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class RennVerwaltungsView implements ViewTemplate {

	private UI ui;

	// member variabeln: Übersichtstabelle
	private Label titel = new Label("Fahrerverwaltung");
	private Table table = new Table();
	private FormLayout fahrerVerwaltungsLayout = new FormLayout();

	// member variabel: Popup fenster
	private Window popup;
	private FormLayout popupLayoutMaster = new FormLayout();
	private FormLayout popupLayoutClubverantwortlicher = new FormLayout();

	private TextField vornameText = new TextField("Vorname");
	private TextField nachnameText = new TextField("Nachname");
	private TextField jahrgangText = new TextField("Jahrgang");
	private TextField plzText = new TextField("Postleitzahl");
	private TextField ortText = new TextField("Ort");
	private TextField telefonNrText = new TextField("Telefonnummer");
	private NativeSelect clubs = new NativeSelect("Klub");
	private Label rennenLabel = new Label("Rennen");
	private NativeSelect bootsKlassen = new NativeSelect("Bootsklasse");
	private NativeSelect altersKategorien = new NativeSelect("Alterskategorie");
	private Label laufzeitenLabel = new Label("Laufzeiten");
	private TextField laufzeitEins = new TextField("1. Laufzeit");
	private TextField laufzeitZwei = new TextField("2. Laufzeit");
	private Button speichern = new Button("Speichrn");

	private MutationsController mController = new MutationsController();

	private static final String COLUMN_VORNAME = "Vorname";
	private static final String COLUMN_NACHNAME = "Nachname";
	private static final String COLUMN_JAHRGANG = "Jahrgang";
	private static final String COLUMN_ORT = "Ort";
	private static final String COLUMN_BUTTON = "Bearbeiten";

	public RennVerwaltungsView(UI ui) {
		this.ui = ui;
	}

	@Override
	public void viewInitialisieren() {
		this.fahrerVerwaltungsLayout.setSpacing(true);
		this.titel.setStyleName("h2");
		this.rennenLabel.setStyleName("h2");
		this.laufzeitenLabel.setStyleName("h3");

		this.table.addContainerProperty(COLUMN_VORNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_NACHNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_JAHRGANG, Integer.class, null);
		this.table.addContainerProperty(COLUMN_ORT, String.class, null);
		this.table.addContainerProperty(COLUMN_BUTTON, Button.class, null);

		tabelleAbfuellen();

		this.laufzeitEins.setInputPrompt("mm:ss:hh");
		this.laufzeitZwei.setInputPrompt("mm:ss:hh");

		this.speichern.addClickListener(event -> {
			// TODO: speichern button
			// mController.
		});

		// TODO: master & Clubverantwortlicher

		this.popupLayoutMaster.addComponent(this.vornameText);
		this.popupLayoutMaster.addComponent(this.nachnameText);
		this.popupLayoutMaster.addComponent(this.jahrgangText);
		this.popupLayoutMaster.addComponent(this.plzText);
		this.popupLayoutMaster.addComponent(this.ortText);
		this.popupLayoutMaster.addComponent(this.telefonNrText);
		this.popupLayoutMaster.addComponent(this.clubs);
		this.popupLayoutMaster.addComponent(this.rennenLabel);
		this.popupLayoutMaster.addComponent(this.bootsKlassen);
		this.popupLayoutMaster.addComponent(this.altersKategorien);
		this.popupLayoutMaster.addComponent(this.laufzeitenLabel);
		this.popupLayoutMaster.addComponent(this.laufzeitEins);
		this.popupLayoutMaster.addComponent(this.laufzeitZwei);
		this.popupLayoutMaster.addComponent(this.speichern);

		this.popup = new Window("Zeit erfassen");
		this.popupLayoutMaster.addStyleName("popup");

		this.popup.setContent(this.popupLayoutMaster);
		this.popup.center();

		this.fahrerVerwaltungsLayout.addComponent(this.titel);
		this.fahrerVerwaltungsLayout.addComponent(this.table);
	}

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

		if (SessionController.getBenutzerRolle().equals(BenutzerRolle.ROLLE_RECHNUNG)) {
			fahrer = this.mController.ladeFahrermutationslisteAlle();
		} else {
			// TODO: change club id
			fahrer = this.mController.ladeFahrermutationslisteClub(1);
		}

		for (Fahrer f : fahrer) {
			Object newItemId = this.table.addItem();
			Item row = this.table.getItem(newItemId);

			Button bearbeiten = new Button("Bearbeiten");

			bearbeiten.addClickListener(event -> {
				this.vornameText.setValue(f.getVorname());
				this.nachnameText.setValue(f.getName());
				this.jahrgangText.setValue(Integer.toString(f.getJahrgang()));
				this.plzText.setValue(Integer.toString(f.getPlz()));
				this.ortText.setValue(f.getOrt());
				this.telefonNrText.setValue(f.getTelNr());

				this.ui.addWindow(this.popup);
			});

			// TODO: lesen von fahrerresultat
			row.getItemProperty(COLUMN_VORNAME).setValue(f.getVorname());
			row.getItemProperty(COLUMN_NACHNAME).setValue(f.getName());
			row.getItemProperty(COLUMN_JAHRGANG).setValue(f.getJahrgang());
			row.getItemProperty(COLUMN_ORT).setValue(f.getOrt());
			row.getItemProperty(COLUMN_BUTTON).setValue(bearbeiten);
		}

		this.table.setPageLength(fahrer.size());
	}

}
