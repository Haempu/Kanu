package ch.bfh.project1.kanu.model;

/**
 * /**
 * Die Klasse FahrerRennen beinhaltet alle Informationen eines zu einem Rennen angemeldeten Fahrers.
 * 
 * Für jede Anmeldung wird ein solches Objekt erstellt. Ist zum Beispiel der gleiche Fahrer bei einem Rennen
 * in der Kategorie A sowie in der Kategorie B angemeldet, gibt es auch zwei solcher Objekte.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.05.2017
 * @version 1.0
 *
 */

@Deprecated
public class FahrerRennen extends Fahrer {
	
	private Integer kategorieID;
	private Integer rennenID;
	private Integer bootID;
	private Integer startplatz;
	private String startzeit1, startzeit2;
	
	public FahrerRennen(Integer fahrerID, String name, String vorname, Integer rennenID, Integer kategorieID, Integer bootID, Integer startplatz, String startzeit1, String startzeit2)
	{
		super(fahrerID, new Club(), name, vorname, 0, "", "", 0, "");
		this.kategorieID = kategorieID;
		this.rennenID = rennenID;
		this.bootID = bootID;
		this.setStartplatz(startplatz);
		this.setStartzeit1(startzeit1);
		this.startzeit2 = startzeit2;
	}

	public Integer getKategorieID() {
		return kategorieID;
	}

	public void setKategorieID(Integer kategorieID) {
		this.kategorieID = kategorieID;
	}

	public Integer getRennenID() {
		return rennenID;
	}

	public void setRennenID(Integer rennenID) {
		this.rennenID = rennenID;
	}

	public Integer getBootID() {
		return bootID;
	}

	public void setBootID(Integer bootID) {
		this.bootID = bootID;
	}

	public Integer getStartplatz() {
		return startplatz;
	}

	public void setStartplatz(Integer startplatz) {
		this.startplatz = startplatz;
	}

	public String getStartzeit1()
	{
		return startzeit1;
	}

	public void setStartzeit1(String startzeit1)
	{
		this.startzeit1 = startzeit1;
	}

	public String getStartzeit2()
	{
		return startzeit2;
	}

	public void setStartzeit2(String startzeit2)
	{
		this.startzeit2 = startzeit2;
	}

}
