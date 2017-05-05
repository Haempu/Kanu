package ch.bfh.project1.kanu.view;

import com.vaadin.ui.Component;

/**
 * Die Klasse ViewTemplate definiert die Überklasse aller grafischen
 * Benutzeroberflächen. Sie gibt folgende Funktionen vor: - viewInitialisieren:
 * Funktion für die Initialisierung der Komponenten auf der grafischen
 * Benutzeroberfläche - viewAnzeigen: Funktion für das Darstellen der grafischen
 * Benutzeroberfläche
 * 
 * Das View Template Interface wird von den folgenden Klassen implementiert: -
 * LoginView - RechnungsView - StartlistenView - FehlererfassungsView -
 * RanglistenView - BenutzerprofilView - MutationsView - ZeiterfassungsView -
 * FahreranmeldungsView
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public interface ViewTemplate {

	/**
	 * Die Funktion initialisiert die View
	 */
	public void viewInitialisieren();

	/**
	 * Die Funktion zeigt die View an.
	 */
	public void viewAnzeigen(Component inhalt);
}
