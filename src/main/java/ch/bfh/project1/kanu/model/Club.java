package ch.bfh.project1.kanu.model;

/**
 * Ein Club ist ein Kanu-Club, mit all seinen Fahrern. Ein Club beinhaltet eine
 * Kennung, einen Namen, einen Clubverantwortlichen und eine Adresse.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 28.03.2017
 * @version 1.0
 *
 */

public class Club {

	private Integer clubID;
	private String kennung;
	private String name;
	private Clubverantwortlicher clubverantwortlicher;
	private String strasse;
	private int plz;
	private String ort;
	private boolean bezahlt;

	public Club(int clubID, String name, boolean bezahlt) {
		this.clubID = clubID;
		this.name = name;
		this.bezahlt = bezahlt;
	}

	public Club(Integer clubID, String kennung, String name) {
		this.clubID = clubID;
		this.kennung = kennung;
		this.name = name;
	}

	public Club() {

	}

	public int getClubID() {
		return clubID;
	}

	public void setClubID(int clubID) {
		this.clubID = clubID;
	}

	public String getKennung() {
		return kennung;
	}

	public void setKennung(String kennung) {
		this.kennung = kennung;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Clubverantwortlicher getClubverantwortlicher() {
		return clubverantwortlicher;
	}

	public void setClubverantwortlicher(Clubverantwortlicher clubverantwortlicher) {
		this.clubverantwortlicher = clubverantwortlicher;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public int getPlz() {
		return plz;
	}

	public void setPlz(int plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public boolean isBezahlt() {
		return bezahlt;
	}

	public void setBezahlt(boolean bezahlt) {
		this.bezahlt = bezahlt;
	}

	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * Wird gebraucht damit das NativeSelect ein gleiches Objekt erkennt.
	 */
	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		if (obj != null && ((Club) obj).getClubID() == clubID) {
			return true;
		}
		return false;
	}

	/**
	 * Wird gebraucht damit das NativeSelect ein gleiches Objekt erkennt.
	 */
	@Override
	public int hashCode() {
		int result = clubID != null ? clubID.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}
}
