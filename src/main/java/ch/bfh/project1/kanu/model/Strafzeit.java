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

	private int torNummer;
	private boolean torBeruehrt;
	private boolean torVerpasst;
	
	public int getTorNummer() {
		return torNummer;
	}
	public void setTorNummer(int torNummer) {
		this.torNummer = torNummer;
	}
	public boolean getTorBeruehrt() {
		return torBeruehrt;
	}
	public void setTorBeruehrt(boolean torBeruehrt) {
		this.torBeruehrt = torBeruehrt;
	}
	public boolean getTorVerpasst() {
		return torVerpasst;
	}
	public void setTorVerpasst(boolean torVerpasst) {
		this.torVerpasst = torVerpasst;
	}

}
