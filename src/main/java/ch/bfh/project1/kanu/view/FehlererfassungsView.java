package ch.bfh.project1.kanu.view;

import java.util.ArrayList;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

import ch.bfh.project1.kanu.model.Fahrer;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class FehlererfassungsView implements ViewTemplate {

	// membervariables
	private Label titel = new Label("Fehlererfassung");
	private Table table = new Table();
	private FormLayout fehlererfassungsLayout = new FormLayout();

	private static final String COLUMN_STARTNUMMER = "Startnummer";
	private static final String COLUMN_VORNAME = "Vorname";
	private static final String COLUMN_NACHNAME = "Nachname";
	private static final String COLUMN_JAHRGANG = "Jahrgang";
	private static final String COLUMN_ORT = "Ort";
	private static final String COLUMN_BUTTON = "Fehler erfassen";

	@Override
	public void viewInitialisieren() {
		this.fehlererfassungsLayout.setSpacing(true);
		this.titel.setStyleName("h2");

		this.table.addContainerProperty(COLUMN_STARTNUMMER, Integer.class, null);
		this.table.addContainerProperty(COLUMN_VORNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_NACHNAME, String.class, null);
		this.table.addContainerProperty(COLUMN_JAHRGANG, Integer.class, null);
		this.table.addContainerProperty(COLUMN_ORT, String.class, null);
		this.table.addContainerProperty(COLUMN_BUTTON, Button.class, null);

		tabelleAbfuellen();

		this.fehlererfassungsLayout.addComponent(this.titel);
		this.fehlererfassungsLayout.addComponent(this.table);
	}

	@Override
	public void viewAnzeigen(Component inhalt) {
		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(this.fehlererfassungsLayout);
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

			Button fehlerErfassen = new Button("Fehler erfassen");

			// TODO: lesen von fahrerresultat
			row.getItemProperty(COLUMN_STARTNUMMER).setValue(12);
			row.getItemProperty(COLUMN_VORNAME).setValue(f.getVorname());
			row.getItemProperty(COLUMN_NACHNAME).setValue(f.getName());
			row.getItemProperty(COLUMN_JAHRGANG).setValue(f.getJahrgang());
			row.getItemProperty(COLUMN_ORT).setValue(f.getOrt());
			row.getItemProperty(COLUMN_BUTTON).setValue(fehlerErfassen);
		}

		this.table.setPageLength(fahrer.size());
	}

}
