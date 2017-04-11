package ch.bfh.project1.kanu.model;
/**
 * Die Klasse „Fahrerresultat“ beinhaltet das effektive Resultat der Läufe. 
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 28.03.2017
 * @version 1.0
 *
 */

public class FahrerResultat {
	private Fahrer fahrer;
	private double zeitErsterLauf;
	private double zeitZweiterLauf;
	private boolean neusterEintrag;
	private Rennen rennen;
	private Kategorie kategorie;
	
	public Fahrer getFahrer() {
		return fahrer;
	}
	public void setFahrer(Fahrer fahrer) {
		this.fahrer = fahrer;
	}
	public double getZeitErsterLauf() {
		return zeitErsterLauf;
	}
	public void setZeitErsterLauf(double zeitErsterLauf) {
		this.zeitErsterLauf = zeitErsterLauf;
	}
	public double getZeitZweiterLauf() {
		return zeitZweiterLauf;
	}
	public void setZeitZweiterLauf(double zeitZweiterLauf) {
		this.zeitZweiterLauf = zeitZweiterLauf;
	}
	public boolean isNeusterEintrag() {
		return neusterEintrag;
	}
	public void setNeusterEintrag(boolean neusterEintrag) {
		this.neusterEintrag = neusterEintrag;
	}
	public Rennen getRennen() {
		return rennen;
	}
	public void setRennen(Rennen rennen) {
		this.rennen = rennen;
	}
	public Kategorie getKategorie() {
		return kategorie;
	}
	public void setKategorie(Kategorie kategorie) {
		this.kategorie = kategorie;
	}
}
