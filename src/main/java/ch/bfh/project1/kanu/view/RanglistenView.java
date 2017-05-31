package ch.bfh.project1.kanu.view;

import com.vaadin.ui.Component;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class RanglistenView implements ViewTemplate {

	private boolean init = false;

	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		// TODO Auto-generated method stub
		this.init = true;

	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@Override
	public void viewAnzeigen(Component inhalt) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean istInitialisiert() {
		return this.init;
	}

}
