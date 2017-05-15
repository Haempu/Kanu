package ch.bfh.project1.kanu.model;

import java.util.Date;
import java.util.List;

/**
 * Die Klasse Rennen wird pro Rennen generiert. Vor dem Rennen werden die
 * Attribute Datum, Ort, Anzahl Tore und Anzahl Posten definiert.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 28.03.2017
 * @version 1.0
 *
 */

public class Rennen {
	private Integer rennenID;
	private String titel;
	private String name;
	private Date datum;
	private String veranstalter; //Kann auch Integer sein (Club ID); falls überhaupt benötigt
	private String ort;
	private int anzTore;
	private int anzPosten;
	private List<AltersKategorie> kategorien;


	public Integer getRennenID() {
		return rennenID;
	}
	public void setRennenID(Integer rennenID) {
		this.rennenID = rennenID;
	}
	public String getTitel() {
		return titel;
	}
	public void setTitel(String titel) {
		this.titel = titel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDatum()
	{
		return datum;
	}
	public void setDatum(Date datum)
	{
		this.datum = datum;
	}
	public String getVeranstalter() {
		return veranstalter;
	}
	public void setVeranstalter(String veranstalter) {
		this.veranstalter = veranstalter;
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
	public List<AltersKategorie> getKategorien()
	{
		return kategorien;
	}
	public void setKategorien(List<AltersKategorie> kategorien)
	{
		this.kategorien = kategorien;
	}
	
	@Deprecated
	public Date getDatumVon() {
		return datum;
	}
	
	@Deprecated
	public Date getDatumBis() {
		return datum;
	}
}
