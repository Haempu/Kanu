package ch.bfh.project1.kanu.model;

/**
 * In der Klasse Strafzeit wird jedes Tor in einem Rennen, welches nicht ohne
 * Strafzeit passiert wurde, wird hier die Strafzeit für den Fahrer und das
 * Rennen gespeichert.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 28.03.2017
 * @version 1.0
 *
 */

public class Strafzeit {

	private Integer torNummer;
	private Integer lauf;
	private Integer strafzeit;
	private boolean verpasst, beruehrt;
	
	public Strafzeit(Integer torNr, Integer lauf, Integer strafzeit)
	{
		torNummer = torNr;
		this.lauf = lauf;
		this.strafzeit = strafzeit;
		if(strafzeit == 5)
			beruehrt = true;
		else
			beruehrt = false;
		if(strafzeit == 60)
			verpasst = true;
		else
			verpasst = false;
	}

	public Integer getTorNummer() {
		return torNummer;
	}

	public void setTorNummer(Integer torNummer) {
		this.torNummer = torNummer;
	}

	public Integer getLauf() {
		return lauf;
	}

	public void setLauf(Integer lauf) {
		this.lauf = lauf;
	}

	public Integer getStrafzeit() {
		return strafzeit;
	}

	public void setStrafzeit(Integer strafzeit) {
		this.strafzeit = strafzeit;
	}

	public boolean isVerpasst() {
		return verpasst;
	}

	public void setVerpasst(boolean verpasst) {
		this.strafzeit = verpasst==true ? 60 : 0;
		this.verpasst = verpasst;
	}

	public boolean isBeruehrt() {
		return beruehrt;
	}

	public void setBeruehrt(boolean beruehrt) {
		this.strafzeit = beruehrt==true ? 5 : 0;
		this.beruehrt = beruehrt;
	}

}
