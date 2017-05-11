package ch.bfh.project1.kanu.model;

public class FahrerRennen extends Fahrer {
	
	private Integer kategorieID;
	private Integer rennenID;
	private Integer bootID;
	private Integer startplatz;
	private String startzeit;
	
	public FahrerRennen(Integer fahrerID, String name, String vorname, Integer rennenID, Integer kategorieID, Integer bootID, Integer startplatz, String startzeit)
	{
		super(fahrerID, new Club(), name, vorname, 0, "", "", 0, "");
		this.kategorieID = kategorieID;
		this.rennenID = rennenID;
		this.bootID = bootID;
		this.startplatz = startplatz;
		this.startzeit = startzeit;
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

}
