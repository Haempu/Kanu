package ch.bfh.project1.kanu.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ch.bfh.project1.kanu.controller.RennverwaltungsController;
import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Rennen;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class RennVerwaltungsView implements ViewTemplate {

	private UI ui; // Haupt GUI
	
	private FormLayout rennenLayout = new FormLayout();
	
	private Window popUpWindow;

	// member variabeln
	private Label titel = new Label("Rennen Verwaltung");
	private Label lname = new Label("Name");
	private Label lort = new Label("Ort");
	private Label ldatum = new Label("Datum");
	private Label lzeit = new Label("Zeit");
	private Label ltore = new Label("Anzahl Tore");
	private Label lkategorie = new Label("Kategorien");
	private Label lposten = new Label("Anzahl Posten");
	
	private TextField tname = new TextField();
	private TextField tort = new TextField();
	private DateField ddatum = new DateField();
	private DateField dzeit = new DateField();
	private TextField ttore = new TextField();
	private TextField tposten = new TextField();
	
	private ListSelect likategorie = new ListSelect();

	// member variabel: Popup fenster
	private Rennen rennen;

	// Controller
	private RennverwaltungsController rController = new RennverwaltungsController();

	// Konstanten

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
	
	@SuppressWarnings("unchecked")
	private void showPopup(final Rennen rennen)
	{
		this.rennen = rennen;
		boolean neu = rennen.getRennenID() < 1 ? true : false;
		Button speichern = new Button("Speichern");
		final GridLayout layout = new GridLayout(2, 8);
		
		popUpWindow = new Window();
		popUpWindow.center();
		popUpWindow.setModal(true);
		speichern.addClickListener(event -> {
			rennen.setAnzPosten(Integer.parseInt(tposten.getValue()));
			rennen.setAnzTore(Integer.parseInt(ttore.getValue()));
			rennen.setOrt(tort.getValue());
			rennen.setName(tname.getValue());
			rennen.setDatum(ddatum.getValue());
			Set<Integer> temp = (Set<Integer>) likategorie.getValue();
			List<AltersKategorie> kategorien = new ArrayList<AltersKategorie>();
			for(int id : temp)
			{
				kategorien.add(new AltersKategorie(id, likategorie.getItemCaption(id)));
			}
			rennen.setKategorien(kategorien);
			if(rController.speichereRennen(rennen))
			{
				updateRennen();
				popUpWindow.close();
			}
		});
		ddatum.setDateFormat("dd.MM.yyyy");
		dzeit.setDateFormat("HH:mm");
		likategorie.clear();
		for(AltersKategorie kat : rController.ladeKategorien())
		{
			likategorie.addItem(kat.getAltersKategorieID());
			likategorie.setItemCaption(kat.getAltersKategorieID(), kat.getName());
		}
		likategorie.setMultiSelect(true);
		likategorie.setRows(10);
		if(likategorie.size() == 0)
		{
			likategorie.setEnabled(false);
			likategorie.addItem(-1);
			likategorie.setItemCaption(-1, "Keine Kategorien");
			likategorie.setRows(1);
			speichern.setEnabled(false);
		}
		else if(!neu)
		{
			for(AltersKategorie ak : rennen.getKategorien())
			{
				likategorie.select(ak.getAltersKategorieID());
			}
		}
		if(!neu)
		{
			tname.setValue(rennen.getName());
			ddatum.setValue(rennen.getDatum());
			dzeit.setValue(rennen.getDatum());
			tort.setValue(rennen.getOrt());
			tposten.setValue(rennen.getAnzPosten() + "");
			ttore.setValue(rennen.getAnzTore() + "");
		}
		else
		{
			tname.setValue("");
			ddatum.setValue(null);
			dzeit.setValue(null);
			tort.setValue("");
			tposten.setValue("");
			ttore.setValue("");
		}
		
		layout.addComponent(lname, 0, 0);
		layout.addComponent(tname, 1, 0);
		layout.addComponent(ldatum, 0, 1);
		layout.addComponent(ddatum, 1, 1);
		layout.addComponent(lzeit, 0, 2);
		layout.addComponent(dzeit, 1, 2);
		layout.addComponent(lort, 0, 3);
		layout.addComponent(tort, 1, 3);
		layout.addComponent(ltore, 0, 4);
		layout.addComponent(ttore, 1, 4);
		layout.addComponent(lposten, 0, 5);
		layout.addComponent(tposten, 1, 5);
		layout.addComponent(lkategorie, 0, 6);
		layout.addComponent(likategorie, 1, 6);
		layout.addComponent(speichern, 0, 7);
		
		layout.setSpacing(true);
		layout.setMargin(true);
		
		popUpWindow.setContent(layout);
		popUpWindow.setWidth("600px");
		popUpWindow.setHeight("450px");
		popUpWindow.setCaption(!neu ? "Renndetails" : "Neues Rennen");
		UI.getCurrent().addWindow(popUpWindow);
	}

	private void updateRennen()
	{
		rennenLayout.removeAllComponents();
		List<Rennen> lrennen = rController.ladeRennen();
		Label larennen = new Label("Bereits erfasste Rennen:");
		larennen.setStyleName("h3");
		rennenLayout.addComponent(larennen);
		Table trennen = new Table();
		trennen.addContainerProperty("Name", String.class, null);
		trennen.addContainerProperty("Ort", String.class, null);
		trennen.addContainerProperty("Datum", String.class, null);
		trennen.addContainerProperty("Bearbeiten", Button.class, null);
		for(Rennen r : lrennen)
		{
			//TODO: Rennen ausgeben
			//Wenn auf Rennen geklickt wird, showPopup mit dem Rennen Objekt aufrufen (Popup zum Rennen bearbeiten)
			Object id = trennen.addItem();
			Item row = trennen.getItem(id);
			row.getItemProperty("Name").setValue(r.getName());
			row.getItemProperty("Ort").setValue(r.getOrt());
			row.getItemProperty("Datum").setValue(r.getDatum().toGMTString()); //TODO
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
		rennenLayout.addComponent(trennen);
		Button bneu = new Button("Rennen erfassen");
		bneu.addClickListener(event -> {
			Rennen rennen = new Rennen();
			rennen.setRennenID(0);
			showPopup(rennen);
		});
		
		rennenLayout.addComponent(bneu);
	}
}
