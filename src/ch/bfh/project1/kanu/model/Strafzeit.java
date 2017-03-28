package ch.bfh.project1.kanu.model;

public class Strafzeit {
	/**
	 * Enth�lt die Strafzeit eines Fahrers w�hrend einem Rennen.
	 *
	 * @author Aebischer Patrik, B�siger Elia, Gestach Lukas
	 * @date 28.03.2017
	 * @version 1.0
	 *
	 */
	
	private Fahrer fahrer;
	private Rennen rennen;
	private int torNummer;
	
	public Fahrer getFahrer() {
		return fahrer;
	}
	public void setFahrer(Fahrer fahrer) {
		this.fahrer = fahrer;
	}
	public Rennen getRennen() {
		return rennen;
	}
	public void setRennen(Rennen rennen) {
		this.rennen = rennen;
	}
	public int getTorNummer() {
		return torNummer;
	}
	public void setTorNummer(int torNummer) {
		this.torNummer = torNummer;
	}
	
}
