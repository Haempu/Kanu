package ch.bfh.project1.kanu.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Item;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import ch.bfh.project1.kanu.controller.RennverwaltungsController;
import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Club;
import ch.bfh.project1.kanu.model.Rennen;

/**
 * Hier können Rennen verwaltet sowie neue hinzugefügt werden.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class RennVerwaltungsView implements ViewTemplate {

	private boolean init = false; // Ist die View initialisiert

	// UI Komponenten
	private UI ui;
	private FormLayout rennenLayout = new FormLayout();
	private Window popUpWindow;
	private Label titel = new Label("Rennen Verwaltung");
	private Label lname = new Label("Name");
	private Label ltitel = new Label("Titel");
	private Label lort = new Label("Ort");
	private Label ldatum = new Label("Datum");
	private Label lzeit = new Label("Zeit");
	private Label ltore = new Label("Anzahl Tore");
	private Label lkategorie = new Label("Kategorien");
	private Label lposten = new Label("Anzahl Posten");
	private Label lveranstalter = new Label("Veranstalter");

	private TextField tname = new TextField();
	private TextField ttitel = new TextField();
	private TextField tort = new TextField();
	private DateField ddatum = new DateField();
	private DateField dzeit = new DateField();
	private TextField ttore = new TextField();
	private TextField tposten = new TextField();
	private NativeSelect nveranstalter = new NativeSelect();
	private ListSelect likategorie = new ListSelect();

	// Controller
	private RennverwaltungsController rController = new RennverwaltungsController();

	/**
	 * Konstruktor: RennVerwaltungsView
	 * 
	 * @param ui
	 */
	public RennVerwaltungsView(UI ui) {
		this.ui = ui;
	}

	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		titel.setStyleName("h2");
		rennenLayout.addComponent(titel);
		this.init = true;
	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@Override
	public void viewAnzeigen(Component inhalt) {
		updateRennen();
		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(rennenLayout);
	}

	/**
	 * Funktion zeigt das Popup für das gegebene Rennen.
	 * 
	 * @param rennen
	 */
	@SuppressWarnings("unchecked")
	private void showPopup(final Rennen rennen) {
		boolean neu = rennen.getRennenID() < 1 ? true : false;
		Button speichern = new Button("Speichern");
		final GridLayout layout = new GridLayout(2, 10);

		// Popup Window
		this.popUpWindow = new Window();
		this.popUpWindow.center();
		this.popUpWindow.setModal(true);

		// Speicher-Event
		speichern.addClickListener(event -> {
			rennen.setAnzPosten(Integer.parseInt(this.tposten.getValue()));
			rennen.setAnzTore(Integer.parseInt(this.ttore.getValue()));
			rennen.setOrt(this.tort.getValue());
			rennen.setName(this.tname.getValue());
			rennen.setDatumVon(this.ddatum.getValue());
			rennen.setDatumBis(this.dzeit.getValue());
			rennen.setTitel(this.ttitel.getValue());
			rennen.setVeranstalter(new Club((Integer) this.nveranstalter.getValue(), "", ""));

			Set<Integer> temp = (Set<Integer>) this.likategorie.getValue();
			List<AltersKategorie> kategorien = new ArrayList<AltersKategorie>();

			for (int id : temp) {
				kategorien.add(new AltersKategorie(id, this.likategorie.getItemCaption(id)));
			}
			rennen.setKategorien(kategorien);

			// Rennen speichern
			if (this.rController.speichereRennen(rennen)) {
				updateRennen();
				this.popUpWindow.close();
			}
		});

		this.ddatum.setDateFormat("dd.MM.yyyy HH:mm");
		this.dzeit.setDateFormat("dd.MM.yyyy HH:mm");
		this.ddatum.setLocale(VaadinSession.getCurrent().getLocale());
		this.dzeit.setLocale(VaadinSession.getCurrent().getLocale());
		this.ddatum.setResolution(Resolution.MINUTE);
		this.dzeit.setResolution(Resolution.MINUTE);
		this.nveranstalter.clear();

		// Clubs in ListSelect abfüllen
		for (Club c : this.rController.ladeClubs()) {
			this.nveranstalter.addItem(c.getClubID());
			this.nveranstalter.setItemCaption(c.getClubID(), c.getName());
		}
		this.likategorie.clear();

		// Kategorien in ListSelect abfüllen
		for (AltersKategorie kat : this.rController.ladeKategorien()) {
			this.likategorie.addItem(kat.getAltersKategorieID());
			this.likategorie.setItemCaption(kat.getAltersKategorieID(), kat.getName());
		}

		this.likategorie.setMultiSelect(true);
		this.likategorie.setRows(10);
		if (this.likategorie.size() == 0) {
			this.likategorie.setEnabled(false);
			this.likategorie.addItem(-1);
			this.likategorie.setItemCaption(-1, "Keine Kategorien");
			this.likategorie.setRows(1);
			speichern.setEnabled(false);
		} else if (!neu) {
			for (AltersKategorie ak : rennen.getKategorien()) {
				this.likategorie.select(ak.getAltersKategorieID());
			}
		}
		if (!neu) {
			// Komponenten mit Werten abfüllen
			this.tname.setValue(rennen.getName());
			this.ddatum.setValue(rennen.getDatumVon());
			this.dzeit.setValue(rennen.getDatumBis());
			this.tort.setValue(rennen.getOrt());
			this.tposten.setValue(rennen.getAnzPosten() + "");
			this.ttore.setValue(rennen.getAnzTore() + "");
			this.ttitel.setValue(rennen.getTitel());
			this.nveranstalter.select(rennen.getVeranstalter().getClubID());
		} else {
			// Komponenten mit leeren Werten abfüllen
			this.tname.setValue("");
			this.ddatum.setValue(null);
			this.dzeit.setValue(null);
			this.tort.setValue("");
			this.tposten.setValue("");
			this.ttore.setValue("");
			this.ttitel.setValue("");
			this.nveranstalter.select(nveranstalter.getNullSelectionItemId());
		}

		// Komponenten zum Layout hinzufügen
		layout.addComponent(this.lname, 0, 0);
		layout.addComponent(this.tname, 1, 0);
		layout.addComponent(this.ltitel, 0, 1);
		layout.addComponent(this.ttitel, 1, 1);
		layout.addComponent(this.ldatum, 0, 2);
		layout.addComponent(this.ddatum, 1, 2);
		layout.addComponent(this.lzeit, 0, 3);
		layout.addComponent(this.dzeit, 1, 3);
		layout.addComponent(this.lveranstalter, 0, 4);
		layout.addComponent(this.nveranstalter, 1, 4);
		layout.addComponent(this.lort, 0, 5);
		layout.addComponent(this.tort, 1, 5);
		layout.addComponent(this.ltore, 0, 6);
		layout.addComponent(this.ttore, 1, 6);
		layout.addComponent(this.lposten, 0, 7);
		layout.addComponent(this.tposten, 1, 7);
		layout.addComponent(this.lkategorie, 0, 8);
		layout.addComponent(this.likategorie, 1, 8);
		layout.addComponent(speichern, 0, 9);

		layout.setSpacing(true);
		layout.setMargin(true);

		this.popUpWindow.setContent(layout);
		this.popUpWindow.setWidth("600px");
		this.popUpWindow.setCaption(!neu ? "Renndetails" : "Neues Rennen");
		UI.getCurrent().addWindow(this.popUpWindow);
	}

	/**
	 * Funktion updated ein bestehenendes Rennen.
	 */
	@SuppressWarnings("unchecked")
	private void updateRennen() {
		this.rennenLayout.removeAllComponents();
		List<Rennen> lrennen = this.rController.ladeRennen();
		Label larennen = new Label("Bereits erfasste Rennen:");
		larennen.setStyleName("h3");
		this.rennenLayout.addComponent(larennen);

		// Tabellenstruktur
		Table trennen = new Table();
		trennen.addContainerProperty("Name", String.class, null);
		trennen.addContainerProperty("Ort", String.class, null);
		trennen.addContainerProperty("Datum", String.class, null);
		trennen.addContainerProperty("Bearbeiten", Button.class, null);

		// Rennen iterieren
		for (Rennen r : lrennen) {
			Object id = trennen.addItem();
			Item row = trennen.getItem(id);
			row.getItemProperty("Name").setValue(r.getName());
			row.getItemProperty("Ort").setValue(r.getOrt());
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			row.getItemProperty("Datum").setValue(sdf.format(r.getDatumVon()));
			Button bbearbeiten = new Button("Bearbeiten");
			bbearbeiten.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					showPopup(r);
				}
			});
			row.getItemProperty("Bearbeiten").setValue(bbearbeiten);
		}
		trennen.setPageLength(lrennen.size());
		this.rennenLayout.addComponent(trennen);
		Button bneu = new Button("Rennen erfassen");

		// Event für neues Rennen
		bneu.addClickListener(event -> {
			Rennen rennen = new Rennen();
			rennen.setRennenID(0);
			showPopup(rennen);
		});

		this.rennenLayout.addComponent(bneu);
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
