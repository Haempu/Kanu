package ch.bfh.project1.kanu.view;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.BootsKlasse;
import ch.bfh.project1.kanu.model.Fahrer;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class FahreranmeldungsView implements ViewTemplate {

	// UI Komponenten
	private Label titel = new Label("Fahreranmeldung");
	private Table table = new Table();
	private FormLayout fahreranmeldungsLayout = new FormLayout();

	// Membervariablen
	private List<BootsKlasse> bootsKlassenListe;
	private List<AltersKategorie> altersKategorienListe;

	// Statische Variablen
	private static final String COLUMN_ANGEMELDET = "Angemeldet";
	private static final String COLUMN_VORNAME = "Vorname";
	private static final String COLUMN_NACHNAME = "Nachname";
	private static final String COLUMN_JAHRGANG = "Jahrgang";
	private static final String COLUMN_BOOTSKLASSE = "Bootsklasse";
	private static final String COLUMN_ALTERSKATEGORIE = "Alterskategorie";

	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		this.fahreranmeldungsLayout.setSpacing(true);
		// TODO: aus db lesen
		this.titel.setValue("Fahreranmeldung: Kanu Club Grenchen");
		this.titel.setStyleName("h2");

		this.bootsKlassenListe = new ArrayList<BootsKlasse>();
		this.altersKategorienListe = new ArrayList<AltersKategorie>();

		// TODO: dieser part löschen da aus der db
		this.bootsKlassenListe.add(new BootsKlasse(1, "K1-Mannschaft"));
		this.bootsKlassenListe.add(new BootsKlasse(2, "K1-Herren"));
		this.bootsKlassenListe.add(new BootsKlasse(3, "K1-Frauen"));
		this.bootsKlassenListe.add(new BootsKlasse(4, "K2-Mannschaft"));

		this.altersKategorienListe.add(new AltersKategorie(1, "Schüler"));
		this.altersKategorienListe.add(new AltersKategorie(2, "Senioren"));

		this.table.addContainerProperty(COLUMN_ANGEMELDET, CheckBox.class, null);
		this.table.addContainerProperty(COLUMN_VORNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_NACHNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_JAHRGANG, Integer.class, null);
		this.table.addContainerProperty(COLUMN_BOOTSKLASSE, NativeSelect.class, null);
		this.table.addContainerProperty(COLUMN_ALTERSKATEGORIE, NativeSelect.class, null);

		tabelleAbfuellen();

		this.fahreranmeldungsLayout.addComponent(this.titel);
		this.fahreranmeldungsLayout.addComponent(this.table);
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
	 * Funktion füllt die Tabelle mit allen Clubs ab.
	 */
	private void tabelleAbfuellen() {
		// TODO: read data out of db and remove dummies
		ArrayList<Fahrer> fahrer = new ArrayList<Fahrer>();
		fahrer.add(new Fahrer(1, "Hans", "Müller", 1993, true));
		fahrer.add(new Fahrer(2, "Hans", "Müller", 1993, true));
		fahrer.add(new Fahrer(3, "Hans", "Müller", 1993, false));
		fahrer.add(new Fahrer(4, "Hans", "Müller", 1993, false));
		fahrer.add(new Fahrer(5, "Hans", "Müller", 1993, true));

		for (Fahrer f : fahrer) {
			Object newItemId = this.table.addItem();
			Item row = this.table.getItem(newItemId);

			CheckBox angemeldetCheckbox = new CheckBox();

			if (f.isAngemeldet()) {
				angemeldetCheckbox.setValue(true);
			} else {
				angemeldetCheckbox.setValue(false);
			}

			angemeldetCheckbox.setImmediate(true);

			NativeSelect bootsKlassen = new NativeSelect();
			bootsKlassen.setRequired(true);
			NativeSelect altersKategorien = new NativeSelect();
			altersKategorien.setRequired(true);

			for (BootsKlasse bk : this.bootsKlassenListe) {
				bootsKlassen.addItem(bk);
			}

			for (AltersKategorie ak : this.altersKategorienListe) {
				altersKategorien.addItem(ak);
			}

			angemeldetCheckbox.addValueChangeListener(event -> {
				if (angemeldetCheckbox.getValue() == true) {
					/* TODO: write to db */
				} else {
					/* TODO: write to db */
				}
			});

			bootsKlassen.addValueChangeListener(event -> {
				/* TODO: write to db */
			});

			altersKategorien.addValueChangeListener(event -> {
				/* TODO: write to db */
			});

			row.getItemProperty(COLUMN_ANGEMELDET).setValue(angemeldetCheckbox);
			row.getItemProperty(COLUMN_VORNAME).setValue(f.getVorname());
			row.getItemProperty(COLUMN_NACHNAME).setValue(f.getName());
			row.getItemProperty(COLUMN_JAHRGANG).setValue(f.getJahrgang());
			row.getItemProperty(COLUMN_BOOTSKLASSE).setValue(bootsKlassen);
			row.getItemProperty(COLUMN_ALTERSKATEGORIE).setValue(altersKategorien);
		}

		this.table.setPageLength(fahrer.size());
	}
}
