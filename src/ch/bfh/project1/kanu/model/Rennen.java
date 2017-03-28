package ch.bfh.project1.kanu.model;

import java.util.Date;

public class Rennen {
	/**
	 * Enth�lt jeweils ein Rennen.
	 *
	 * @author Aebischer Patrik, B�siger Elia, Gestach Lukas
	 * @date 28.03.2017
	 * @version 1.0
	 *
	 */
	
	private int rennenID;
	private String name;
	private Date datum;
	private String ort;
	private int anzTore;
	private int anzPosten;
	
	public int getRennenID() {
		return rennenID;
	}
	public void setRennenID(int rennenID) {
		this.rennenID = rennenID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public String getOrt() {
		return ort;
	}
	public void setOrt(String ort) {
		this.ort = ort;
	}
	public int getAnzTore() {
		return anzTore;
	}
	public void setAnzTore(int anzTore) {
		this.anzTore = anzTore;
	}
	public int getAnzPosten() {
		return anzPosten;
	}
	public void setAnzPosten(int anzPosten) {
		this.anzPosten = anzPosten;
	}
	
}
