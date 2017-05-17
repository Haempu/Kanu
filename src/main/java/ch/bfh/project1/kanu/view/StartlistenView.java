package ch.bfh.project1.kanu.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

import ch.bfh.project1.kanu.controller.StartlistenController;
import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Rennen;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class StartlistenView implements ViewTemplate {

	private UI ui; // Haupt GUI
	private Button gesamteStartlisteAlsPDF = new Button("Gesamte Startliste als PDF herunterladen");
	private StartlistenController sController = new StartlistenController();
	private Rennen rennen;
	
	private GridLayout layout = new GridLayout(3, 3);
	
	private List<ListSelect> block = new ArrayList<ListSelect>();
	private List<Button> badd = new ArrayList<Button>();
	private List<Button> brem = new ArrayList<Button>();
	private ListSelect kategorien = new ListSelect();
	
	private ClickListener cladd, clrem;

	public StartlistenView(UI ui) {
		this.ui = ui;
	}
	
	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		if(rennen == null)
			rennen = sController.ladeRennen(0); //NullPointer Exception verhindern
		
		kategorien.setMultiSelect(true);
		
		cladd = new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ListSelect tmp = (ListSelect) event.getButton().getData();
				@SuppressWarnings("unchecked")
				Set<Integer> temp = (Set<Integer>) kategorien.getValue();
				for(int id : temp)
				{
					tmp.addItem(id);
					tmp.setItemCaption(id, kategorien.getItemCaption(id));
					kategorien.removeItem(id);
				}
			}
		};
		
		clrem = new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ListSelect tmp = (ListSelect) event.getButton().getData();
				@SuppressWarnings("unchecked")
				Set<Integer> temp = (Set<Integer>) tmp.getValue();
				for(int id : temp)
				{
					kategorien.addItem(id);
					kategorien.setItemCaption(id, tmp.getItemCaption(id));
					tmp.removeItem(id);
				}
			}
		};
		
		if(rennen.getRennenID() == null)
			throw new IllegalArgumentException("Kein Rennen angegeben");
		ListSelect tmp1 = new ListSelect();
		tmp1.setMultiSelect(true);
		Button tmp = new Button("<<");
		tmp.setData(tmp1);
		tmp.addClickListener(cladd);
		badd.add(tmp); //TODO setData brauchen!
		Button tmp2 = new Button(">>");
		tmp2.setData(tmp1);
		tmp2.addClickListener(clrem);
		brem.add(tmp2);
		block.add(tmp1);
		layout.addComponent(block.get(0), 0, 0, 0, 1);
		layout.addComponent(badd.get(0), 1, 0);
		layout.addComponent(brem.get(0), 1, 1);
		layout.setComponentAlignment(tmp, Alignment.BOTTOM_CENTER);
		layout.setComponentAlignment(tmp2, Alignment.TOP_CENTER);
		
		kategorien.clear();
		for(AltersKategorie kat : rennen.getKategorien())
		{
			kategorien.addItem(kat.getAltersKategorieID());
			kategorien.setItemCaption(kat.getAltersKategorieID(), kat.getName());
		}
		//kategorien.setRows(kategorien.size());
		layout.addComponent(kategorien, 2, 0, 2, layout.getRows() - 1);
		layout.setComponentAlignment(kategorien, Alignment.MIDDLE_CENTER);
		
		Button bneu = new Button("Block hinzufügen");
		layout.addComponent(bneu, 0, 2);
		
		bneu.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				addBox();
			}
		});
		
		layout.setSpacing(true);
	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@Override
	public void viewAnzeigen(Component inhalt) {
		
		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(layout);
	}
	
	private void addBox()
	{
		ListSelect tmp = new ListSelect();
		tmp.setMultiSelect(true);
		Button badd = new Button("<<");
		badd.setData(tmp);
		badd.addClickListener(cladd);
		this.badd.add(badd);
		Button brem = new Button(">>");
		brem.addClickListener(clrem);
		brem.setData(tmp);
		this.brem.add(brem);
		block.add(tmp);
		Button tmp2 = (Button) layout.getComponent(0, layout.getRows() - 1);
		layout.removeComponent(tmp2);
		int rows = layout.getRows();
		layout.setRows(rows + 2);
		layout.addComponent(block.get(block.size() - 1), 0, rows - 1, 0, rows);
		layout.addComponent(this.badd.get(this.badd.size() - 1), 1, rows - 1);
		layout.addComponent(this.brem.get(this.brem.size() - 1), 1, rows);
		layout.addComponent(tmp2, 0, rows + 1);
		ListSelect tmp3 = (ListSelect) layout.getComponent(2, 0);
		layout.removeComponent(tmp3);
		layout.addComponent(tmp3, 2, 0, 2, layout.getRows() - 1);
		layout.setComponentAlignment(tmp3, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(badd, Alignment.BOTTOM_CENTER);
		layout.setComponentAlignment(brem, Alignment.TOP_CENTER);
	}

	public Rennen getRennen() {
		return rennen;
	}

	/**
	 * Setzt das Rennen, für welches die Startliste generiert werden soll
	 * @param rennen
	 */
	public void setRennen(Rennen rennen) {
		this.rennen = rennen;
	}

}
