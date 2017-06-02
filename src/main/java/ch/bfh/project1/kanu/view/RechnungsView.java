package ch.bfh.project1.kanu.view;

import java.util.ArrayList;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

import ch.bfh.project1.kanu.controller.RechnungsController;
import ch.bfh.project1.kanu.model.Club;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class RechnungsView implements ViewTemplate {
	// Allgemein
	private boolean init = false;

	// UI Komponenten
	private Label titel = new Label("Rechnungsverwaltung");
	private Table table = new Table();
	private Button allePDFgenerieren = new Button("Alle PDF herunterladen");
	private FormLayout rechnungsViewLayout = new FormLayout();
	private ArrayList<Club> clubs;

	// controller
	private RechnungsController rController = new RechnungsController();

	// Konstanten
	private static final String COLUMN_CLUB = "Club";
	private static final String COLUMN_BEZAHLT = "bezahlt";
	private static final String COLUMN_PDF = "Rechnung";

	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		this.rechnungsViewLayout.setSpacing(true);
		this.titel.setStyleName("h2");

		this.table.addContainerProperty(COLUMN_CLUB, String.class, null);
		this.table.addContainerProperty(COLUMN_BEZAHLT, CheckBox.class, null);
		this.table.addContainerProperty(COLUMN_PDF, Button.class, null);

		tabelleAbfuellen();

		this.allePDFgenerieren.addClickListener(event -> {
			for (Club club : this.clubs) {
				// TODO: rennenID
				// this.rController.rechnungErstellen(club);
			}
		});

		this.rechnungsViewLayout.addComponent(this.titel);
		this.rechnungsViewLayout.addComponent(this.table);
		this.rechnungsViewLayout.addComponent(this.allePDFgenerieren);

		this.init = true;
	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@Override
	public void viewAnzeigen(Component inhalt) {
		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(this.rechnungsViewLayout);
	}

	/**
	 * Funktion füllt die Tabelle mit allen Clubs ab.
	 */
	private void tabelleAbfuellen() {
		this.clubs = this.rController.ladeAngemeldeteClubs();

		for (Club club : this.clubs) {
			Object newItemId = this.table.addItem();
			Item row = this.table.getItem(newItemId);

			CheckBox bezahltCheckbox = new CheckBox();
			Button pdfGenerieren = new Button("PDF herunterladen");

			if (club.isBezahlt()) {
				bezahltCheckbox.setValue(true);
			} else {
				bezahltCheckbox.setValue(false);
			}

			bezahltCheckbox.setImmediate(true);
			bezahltCheckbox.addValueChangeListener(event -> {
				if (bezahltCheckbox.getValue() == true) {
					this.rController.rechnungBezahlen(club, true);
				} else {
					this.rController.rechnungBezahlen(club, false);
				}
			});
			// Generiert ein PDF von der ausgewählten Rechnung
			pdfGenerieren.addClickListener(event -> {
				this.rController.rechnungErstellen(club, 2); // TODO: Rennen von View laden nicht einfach Rennen 2
			});

			row.getItemProperty(COLUMN_CLUB).setValue(club.getName());
			row.getItemProperty(COLUMN_BEZAHLT).setValue(bezahltCheckbox);
			row.getItemProperty(COLUMN_PDF).setValue(pdfGenerieren);
		}

		this.table.setPageLength(clubs.size());
	}

	@Override
	public boolean istInitialisiert() {
		return this.init;
	}
}
