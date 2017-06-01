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

	@Deprecated //Wird nirgendwo gebraucht --> löschen!
	public FahrerResultat(Fahrer fahrer, Integer zeit1, Integer zeit2, Rennen rennen, AltersKategorie kategorie,
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

	public FahrerResultat() {
		// TODO Im Moment nicht gebraucht --> löschen?
	}

	public Fahrer getFahrer() {
		return fahrer;
	}

	public void setFahrer(Fahrer fahrer) {
		this.fahrer = fahrer;
	}

	public Integer getZeitErsterLauf() {
		return zeitErsterLauf;
	}

	public void setZeitErsterLauf(Integer zeitErsterLauf) {
		this.zeitErsterLauf = zeitErsterLauf;
	}

	public Integer getZeitZweiterLauf() { //TODO Zeit formatiert für Rangliste geben
		return zeitZweiterLauf;
	}
	
	public String getZeitTotal() { //TODO formatieren!
		if(zeitZweiterLauf == 0)
			return zeitErsterLauf + strafzeit1 + "";
		return Math.min(zeitErsterLauf + strafzeit1, zeitZweiterLauf + strafzeit2) + "";
	}
	
	public Integer getZeitCompare()
	{
		if(zeitZweiterLauf == 0)
			return zeitErsterLauf;
		return Math.min(zeitErsterLauf + strafzeit1, zeitZweiterLauf + strafzeit2);
	}

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

	public Integer getStrafzeit1()
	{
		return strafzeit1;
	}

	public void setStrafzeit1(Integer strafzeit1)
	{
		this.strafzeit1 = strafzeit1;
	}

	public Integer getStrafzeit2()
	{
		return strafzeit2;
	}

	public void setStrafzeit2(Integer strafzeit2)
	{
		this.strafzeit2 = strafzeit2;
	}

	public Integer getGesamtzeit2()
	{
		return gesamtzeit2;
	}

	public void setGesamtzeit2(Integer gesamtzeit2)
	{
		this.gesamtzeit2 = gesamtzeit2;
	}

	public Integer getGesamtzeit1()
	{
		return gesamtzeit1;
	}

	public void setGesamtzeit1(Integer gesamtzeit1)
	{
		this.gesamtzeit1 = gesamtzeit1;
	}
}