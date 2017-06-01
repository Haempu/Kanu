package ch.bfh.project1.kanu.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import ch.bfh.project1.kanu.controller.DBController;
import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rangliste;
import ch.bfh.project1.kanu.model.Rennen;
import ch.bfh.project1.kanu.util.ResultatComparator;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class RanglistenView implements ViewTemplate {

	private boolean init = false;
	private VerticalLayout layout;
	private Rennen rennen;
	private DBController db;

	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		// TODO Auto-generated method stub
		this.init = true;
		layout = new VerticalLayout();
		db = DBController.getInstance();
	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@Override
	public void viewAnzeigen(Component inhalt) {
		if (rennen == null)
			throw new IllegalArgumentException("Kein Rennen angegeben");
		layout.removeAllComponents();
		Rangliste rangliste = db.ladeRanglisteRennen(rennen);
		int altKat = -1;
		List<FahrerResultat> res = new ArrayList<FahrerResultat>();
		rangliste.getResultate().add(new FahrerResultat(null, null, null, null, new AltersKategorie(-2, "")));
		for (FahrerResultat f : rangliste.getResultate()) {
			if (altKat != f.getKategorie().getAltersKategorieID()) {
				altKat = f.getKategorie().getAltersKategorieID();
				if (res.size() > 0) {
					// Tabelle anzeigen
					Collections.sort(res, new ResultatComparator());
					Table trangliste = new Table();
					trangliste.addContainerProperty("#", String.class, null);
					trangliste.addContainerProperty("Vorname", String.class, null);
					trangliste.addContainerProperty("Name", String.class, null);
					trangliste.addContainerProperty("Zeit", String.class, null);
					int i = 1;
					for (FahrerResultat r : res) {
						Object id = trangliste.addItem();
						Item row = trangliste.getItem(id);
						row.getItemProperty("#").setValue(i + "");
						row.getItemProperty("Vorname").setValue(r.getFahrer().getVorname());
						row.getItemProperty("Name").setValue(r.getFahrer().getName());
						row.getItemProperty("Zeit")
								.setValue(Math.min(r.getZeitErsterLauf(), r.getZeitZweiterLauf()) + "");
						i++;
					}
					trangliste.setPageLength(res.size());
					layout.addComponent(new Label("Kategorie " + f.getKategorie().getName()));
					layout.addComponent(trangliste);
					res.clear();
				}
			}
			res.add(f);
		}

		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(layout);
	}

	@Override
	public boolean istInitialisiert() {
		return this.init;
	}

	public void setRennen(Rennen rennen) {
		this.rennen = rennen;
	}

}
