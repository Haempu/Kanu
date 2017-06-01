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
	private Date datumVon;
	private Date datumBis;
	private Club veranstalter;
	private String ort;
	private int anzTore;
	private int anzPosten;
	private List<AltersKategorie> kategorien;

	public Rennen() {

	}
	
	public Rennen(Integer rennenID)
	{
		this.rennenID = rennenID;
	}
	
	public Rennen(Integer rennenID, String name, String titel, Club club, Date datumVon, Date datumBis, String ort, Integer anzTore, Integer anzPosten,
			List<AltersKategorie> kategorien) {
		this.rennenID = rennenID;
		this.name = name;
		this.datumBis = datumBis;
		this.datumVon = datumVon;
		this.ort = ort;
		this.anzPosten = anzPosten;
		this.anzTore = anzTore;
		this.kategorien = kategorien;
		this.titel = titel;
		veranstalter = club;
	}

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
	
	public Date getDatumVon()
	{
		return datumVon;
	}

	public void setDatumVon(Date datum)
	{
		this.datumVon = datum;
	}
	public Date getDatumBis()
	{
		return datumBis;
	}
	public void setDatumBis(Date datum)
	{
		this.datumBis = datum;
	}

	public Club getVeranstalter() {
		return veranstalter;
	}

	public void setVeranstalter(Club veranstalter) {
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

	public List<AltersKategorie> getKategorien() {
		return kategorien;
	}

	public void setKategorien(List<AltersKategorie> kategorien) {
		this.kategorien = kategorien;
	}

	@Override
	public String toString() {
		return this.name + " " + this.ort;
	}
}
