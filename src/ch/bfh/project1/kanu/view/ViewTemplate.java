package ch.bfh.project1.kanu.view;

/**
 * Die Klasse �ViewTemplate� definiert die �berklasse aller grafischen
 * Benutzeroberfl�chen. Sie gibt folgende Funktionen vor: - viewInitialisieren:
 * Funktion f�r die Initialisierung der Komponenten auf der grafischen
 * Benutzeroberfl�che - viewAnzeigen: Funktion f�r das Darstellen der grafischen
 * Benutzeroberfl�che
 * 
 * Das View Template Interface wird von den folgenden Klassen implementiert: -
 * LoginView - RechnungsView - StartlistenView - FehlererfassungsView -
 * RanglistenView - BenutzerprofilView - MutationsView - ZeiterfassungsView -
 * FahreranmeldungsView
 *
 * @author Aebischer Patrik, B�siger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public interface ViewTemplate {

	public void viewInitialisieren();

	public void viewAnzeigen();
}
