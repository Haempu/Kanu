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
	private Integer zeitErsterLauf;
	private Integer zeitZweiterLauf;
	private Integer gesamtzeit1;
	private Integer gesamtzeit2;
	private Integer strafzeit1;
	private Integer strafzeit2;
	private boolean neusterEintrag;
	private Rennen rennen;
	private AltersKategorie kategorie;
	private Integer startnummer;
	private String startzeitEins;
	private String startzeitZwei;
	public FahrerResultat(Fahrer fahrer, Rennen rennen, AltersKategorie kat, String s1, String s2, Integer sn) {
		this.fahrer = fahrer;
		this.rennen = rennen;
		kategorie = kat;
		startnummer = sn;
		startzeitEins = s1;
		startzeitZwei = s2;
	}

	public FahrerResultat(Fahrer fahrer, Integer gesamtzeit1, Integer gesamtzeit2, Integer zeit1, Integer zeit2, Integer strafzeit1, Integer strafzeit2, Integer startplatz, Rennen rennen, AltersKategorie kat) {
		this.fahrer = fahrer;
		this.zeitErsterLauf = zeit1;
		this.zeitZweiterLauf = zeit2;
		this.strafzeit1 = strafzeit1;
		this.strafzeit2 = strafzeit2;
		this.setGesamtzeit1(gesamtzeit1);
		this.setGesamtzeit2(gesamtzeit2);
		this.rennen = rennen;
		startnummer = startplatz;
		kategorie = kat;
	}
	
	public FahrerResultat(AltersKategorie kat)
	{
		kategorie = kat;
		zeitErsterLauf = 0;
		zeitZweiterLauf = 0;
	}

	public Fahrer getFahrer() {
		return fahrer;
	}

	public void setFahrer(Fahrer fahrer) {
		this.fahrer = fahrer;
	}

	/**
	 * Gibt die Zeit vom ersten Lauf im "Date"-Format
	 * @return
	 */
	public Integer getZeitErsterLauf() {
		return zeitErsterLauf;
	}

	/**
	 * Setzt die Zeit für den ersten Lauf im "Date"-Format
	 * @param zeitErsterLauf
	 */
	public void setZeitErsterLauf(Integer zeitErsterLauf) {
		this.zeitErsterLauf = zeitErsterLauf;
	}

	/**
	 * Gibt die Zeit vom zweiten Lauf im "Date"-Format
	 * @return
	 */
	public Integer getZeitZweiterLauf() { 
		return zeitZweiterLauf;
	}
	
	/**
	 * Gibt die Gesamtzeit in ms zurück
	 * @return
	 */
	public Integer getZeitTotal() {
		if(gesamtzeit2 == 0)
			return gesamtzeit1;
		return Math.min(gesamtzeit1, gesamtzeit2);
	}
	
	/**
	 * Util Methode, um die Fahrer zu vergleichen und sortieren (gibt die bessere Zeit in ms zurück).
	 * @return
	 */
	public Integer getZeitCompare()
	{
		if(zeitZweiterLauf == 0)
			return zeitErsterLauf;
		return Math.min(zeitErsterLauf + strafzeit1, zeitZweiterLauf + strafzeit2);
	}

	/**
	 * Setzt die Zeit des zweiten Laufes im "Date"-Format
	 * @param zeitZweiterLauf
	 */
	public void setZeitZweiterLauf(Integer zeitZweiterLauf) {
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

	public Integer getStartnummer() {
		return startnummer;
	}

	public void setStartnummer(Integer startnummer) {
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

	/**
	 * Strafzeit in Sekunden!
	 * @return
	 */
	public Integer getStrafzeit1()
	{
		return strafzeit1;
	}

	/**
	 * Strafzeit in Sekunden!
	 * @param strafzeit1
	 */
	public void setStrafzeit1(Integer strafzeit1)
	{
		this.strafzeit1 = strafzeit1;
	}

	/**
	 * Strafzeit in Sekunden
	 * @return
	 */
	public Integer getStrafzeit2()
	{
		return strafzeit2;
	}

	/**
	 * Strafzeit in Sekunden
	 * @param strafzeit2
	 */
	public void setStrafzeit2(Integer strafzeit2)
	{
		this.strafzeit2 = strafzeit2;
	}

	/**
	 * Gesamtzeit in ms!
	 * @return
	 */
	public Integer getGesamtzeit2()
	{
		return gesamtzeit2;
	}

	/**
	 * Gesamtzeit in ms!
	 * @param gesamtzeit2
	 */
	public void setGesamtzeit2(Integer gesamtzeit2)
	{
		this.gesamtzeit2 = gesamtzeit2;
	}

	/**
	 * Gesamtzeit in ms!
	 * @return
	 */
	public Integer getGesamtzeit1()
	{
		return gesamtzeit1;
	}

	/**
	 * Gesamtzeit in ms!
	 * @param gesamtzeit1
	 */
	public void setGesamtzeit1(Integer gesamtzeit1)
	{
		this.gesamtzeit1 = gesamtzeit1;
	}
}