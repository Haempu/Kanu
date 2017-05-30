package ch.bfh.project1.kanu.model;

/**
 * Die Klasse Fahrerresultat beinhaltet das effektive Resultat der Läufe.
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
	private AltersKategorie kategorie;
	private Integer startnummer;
	private String startzeitEins;
	private String startzeitZwei;

	public FahrerResultat(Fahrer fahrer, double zeit1, double zeit2, Rennen rennen, AltersKategorie kategorie,
			Integer startnummer, String startzeitEins, String startzeitZwei) {
		this.fahrer = fahrer;
		this.zeitErsterLauf = zeit1;
		this.zeitZweiterLauf = zeit2;
		this.rennen = rennen;
		this.kategorie = kategorie;
		this.startnummer = startnummer;
		this.setStartzeitEins(startzeitEins);
		this.setStartzeitZwei(startzeitZwei);
	}

	public FahrerResultat() {
		// TODO Im Moment nicht gebraucht --> löschen?
	}

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

	public AltersKategorie getKategorie() {
		return kategorie;
	}

	public void setKategorie(AltersKategorie kategorie) {
		this.kategorie = kategorie;
	}

	public int getStartnummer() {
		return startnummer;
	}

	public void setStartnummer(int startnummer) {
		this.startnummer = startnummer;
	}

	public String getStartzeitEins() {
		return startzeitEins;
	}

	public void setStartzeitEins(String startzeitEins) {
		this.startzeitEins = startzeitEins;
	}

	public String getStartzeitZwei() {
		return startzeitZwei;
	}

	public void setStartzeitZwei(String startzeitZwei) {
		this.startzeitZwei = startzeitZwei;
	}
}
