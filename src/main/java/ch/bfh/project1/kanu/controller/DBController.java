package ch.bfh.project1.kanu.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Benutzer;
import ch.bfh.project1.kanu.model.Club;
import ch.bfh.project1.kanu.model.Fahrer;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rangliste;
import ch.bfh.project1.kanu.model.Rennen;
import ch.bfh.project1.kanu.model.Strafzeit;
import ch.bfh.project1.kanu.util.Row;

/**
 * Die Klasse DBController ist zuständig, die Daten aus der Datenbank zur
 * Verfügung zu stellen und auf die Datenbank zu schreiben.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class DBController {
	private String dbHost;
	private String db;
	private String dbUser;
	private String dbUserPassword;

	private Connection connection;

	private static DBController instance;

	public enum Table_Club {
		COLUMN_ID("club_id"), COLUMN_NAME("name"), CLOUMN_ALL("*");

		private final String column;

		Table_Club(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_Rennen {
		COLUMN_ID("rennen_id"), COLUMN_NAME("name"), COLUMN_datum("datum"), COLUMN_ZEIT("zeit"), COLUMN_ORT(
				"ort"), COLUMN_ALL("*");

		private final String column;

		Table_Rennen(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_Kat_Rennen {
		COLUMN_RENNEN_ID("rennen_id"), COLUMN_KATEGORIE_ID("kategorie_id"), COLUMN_GEBUEHR("gebuehr"), CLOUMN_ALL("*");

		private final String column;

		Table_Kat_Rennen(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_Rangliste {
		COLUMN_RENNEN_ID("rennen_id"), COLUMN_FAHRER_ID("fahrer_id"), COLUMN_KATEGORIE("kategorie_id"), COLUMN_CLUB_ID(
				"c.club_id"), CLOUMN_ALL("*");

		private final String column;

		Table_Rangliste(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_Strafzeit {
		COLUMN_RENNEN_ID("rennen_id"), COLUMN_FAHRER_ID("fahrer_id"), COLUMN_KATEGORIE("kategorie_id"), COLUMN_LAUF(
				"lauf"), CLOUMN_ALL("*");

		private final String column;

		Table_Strafzeit(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_Kategorien {
		COLUMN_ID("kategorie_id"), COLUMN_NAME("name"), COLUMN_ALL("*");

		private final String column;

		Table_Kategorien(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_Block_Rennen {
		COLUMN_KATEGORIE_ID("kategorie_id"), COLUMN_RENNEN_ID("rennen_id"), COLUMN_BLOCK_NR("block_nr"), COLUMN_ALL(
				"*");

		private final String column;

		Table_Block_Rennen(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_Fahrer {
		COLUMN_ID("fahrer_id"), COLUMN_CLUB_ID("club_id"), COLUMN_NAME("name"), COLUMN_VORNAME(
				"vorname"), COLUMN_JAHRGANG("jahrgang"), COLUMN_TELNR(
						"telnnr"), COLUMN_PLZ("plz"), COLUMN_ORT("ort"), CLOUMN_ALL("*"), COLUMN_FTS("fts");

		private final String column;

		Table_Fahrer(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_FahrerRennen {
		COLUMN_FAHRER_ID("fahrer_id"), COLUMN_RENNEN_ID("rennen_id"), COLUMN_KATEGORIE("kategorie_id"), COLUMN_BEZAHLT(
				"bezahlt"), COLUMN_CLUB_ID("club_id"), CLOUMN_ALL("*"), COLUMN_FTS("fts");

		private final String column;

		Table_FahrerRennen(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_Benutzer {
		COLUMN_ID("user_id"), COLUMN_BENUTZERNAME("benutzername"), COLUMN_EMAIL("email"), COLUMN_NAME(
				"name"), COLUMN_VORNAME("vorname"), COLUMN_CLUB_ID("club_id"), COLUMN_RECHTE("rechte"), CLOUMN_ALL("*");

		private final String column;

		Table_Benutzer(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	private DBController() {
		dbHost = "sds-ranking.ch";
		db = "kanu";
		dbUser = "kanu";
		dbUserPassword = "!kanuPW?";

		try {
			connect();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO_1: log exceptions
		}
	}

	public static synchronized DBController getInstance() {
		if (instance == null) {
			instance = new DBController();
		}
		return instance;
	}

	/**
	 * Connects to the database through jdbc and the underlining mysql-jdbc
	 * driver.
	 *
	 * @throws ClassNotFoundException
	 *             if the mysql-jdbc driver class cannot be located.
	 * @throws SQLException
	 *             if a database access error occurs.
	 */
	public void connect() throws ClassNotFoundException, SQLException {
		// Load mysql-jdbc driver
		Class.forName("com.mysql.jdbc.Driver");
		// Connect to database
		connection = DriverManager.getConnection("jdbc:mysql://" + dbHost + "/" + db + "", dbUser, dbUserPassword);
	}

	/**
	 * Disconnects from the database. It is strongly recommended that an
	 * application explicitly commits or rolls back an active transaction prior
	 * to calling the disconnect method.
	 *
	 * @throws SQLException
	 *             if a database access error occurs.
	 */
	public void disconnect() throws SQLException {
		connection.close();
	}

	/**
	 * Liest die Clubs aus der Datenbank aus
	 * 
	 * @param column
	 *            Spalte, in der gesucht werden soll
	 * @param value
	 *            Wert, nach dem gesucht werden soll
	 * @return Liste der Clubs, die die Suchkriterien einhalten
	 */
	public <T> List<Club> selectClubBy(Table_Club column, T value) {
		String selectStmt = "SELECT * FROM club WHERE " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Club.CLOUMN_ALL))
			selectStmt = "SELECT * FROM club";
		List<Club> clubs = new ArrayList<Club>();

		for (Row row : executeSelect(selectStmt)) {
			Integer idClub = (Integer) row.getRow().get(0).getKey();
			String clubKennung = (String) row.getRow().get(1).getKey();
			String name = (String) row.getRow().get(2).getKey();
			clubs.add(new Club(idClub, clubKennung, name));
		}
		return clubs;
	}

	/**
	 * Liest die Strafzeiten aus der Datenbank
	 * 
	 * @param column
	 *            Spalten, in denen gesucht werden soll
	 * @param value
	 *            Werte, nach denen gesucht werden soll
	 * @return Eine Liste mit Strafzeiten
	 */
	public <T> List<Strafzeit> selectStrafzeitBy(Table_Strafzeit[] column, T[] value) {
		String where = makeWhere(column, value);
		String selectStmt = "SELECT * FROM strafzeiten " + where; // TODO
		List<Strafzeit> strafzeiten = new ArrayList<Strafzeit>();

		for (Row row : executeSelect(selectStmt)) {
			Integer lauf = (Integer) row.getRow().get(3).getKey();
			Integer torNr = (Integer) row.getRow().get(4).getKey();
			Integer strafzeit = (Integer) row.getRow().get(5).getKey();
			strafzeiten.add(new Strafzeit(torNr, lauf, strafzeit));
		}
		return strafzeiten;
	}

	/**
	 * Liest die Blöcke aus der Datenbank
	 * 
	 * @param column
	 *            Die Spalte, in der gesucht werden soll
	 * @param value
	 *            Der Wert, nach dem gesucht werden soll
	 * @return Eine Liste mit Kategorien, die passen
	 */
	public <T> List<AltersKategorie> selectBloeckeBy(Table_Block_Rennen column, T value) {
		String selectStmt = "SELECT * FROM block_rennen JOIN kategorien USING(kategorie_id) WHERE " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Block_Rennen.COLUMN_ALL))
			selectStmt = "SELECT * FROM block_rennen JOIN kategorien USING(kategorie_id)";
		selectStmt += " ORDER BY block_nr, nr ASC";
		List<AltersKategorie> kats = new ArrayList<AltersKategorie>();

		for (Row row : executeSelect(selectStmt)) {
			Integer katID = (Integer) row.getRow().get(0).getKey();
			// Integer rennenID = (Integer) row.getRow().get(1).getKey();
			Integer blockNr = (Integer) row.getRow().get(2).getKey();
			Integer nr = (Integer) row.getRow().get(3).getKey();
			String name = (String) row.getRow().get(4).getKey();
			kats.add(new AltersKategorie(katID, name, blockNr, nr));
		}
		return kats;
	}

	/**
	 * Liest die Fahrer inkl. Club aus der Datenbank raus.
	 * 
	 * @param column
	 *            Spalte, in der gesucht werden soll
	 * @param value
	 *            Wert, nach dem gesucht werden soll
	 * @return Liste der Fahrer, die die Suchkriterien erfüllen
	 */
	public <T> List<Fahrer> selectFahrerBy(Table_Fahrer column, T value) {
		String selectStmt = "SELECT * FROM fahrer JOIN club USING(club_id) WHERE " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Fahrer.CLOUMN_ALL))
			selectStmt = "SELECT * FROM fahrer JOIN club USING(club_id)";
		if (column.equals(Table_Fahrer.COLUMN_FTS))
			selectStmt = "SELECT * FROM fahrer JOIN club USING(club_id) WHERE name LIKE '% " + value + "%' OR "
					+ "vorname LIKE '%" + value + "%' OR jahrgang LIKE '%" + value + "%'";
		List<Fahrer> fahrer = new ArrayList<Fahrer>();
		for (Row row : executeSelect(selectStmt)) {
			Integer idClub = (Integer) row.getRow().get(0).getKey();
			Integer idFahrer = (Integer) row.getRow().get(1).getKey();
			String name = (String) row.getRow().get(2).getKey();
			String vorname = (String) row.getRow().get(3).getKey();
			Integer jg = (Integer) row.getRow().get(4).getKey();
			String telnr = (String) row.getRow().get(5).getKey();
			String strasse = (String) row.getRow().get(6).getKey();
			Integer plz = (Integer) row.getRow().get(7).getKey();
			String ort = (String) row.getRow().get(8).getKey();
			String kennung = (String) row.getRow().get(9).getKey();
			String club_name = (String) row.getRow().get(10).getKey();
			fahrer.add(new Fahrer(idFahrer, new Club(idClub, kennung, club_name), name, vorname, jg, telnr, strasse,
					plz, ort));
		}
		return fahrer;
	}

	/**
	 * Liest die angemeldeten Fahrer zu einem Rennen aus
	 * 
	 * @param column
	 *            Array von Spalten, in denen gesucht werden soll
	 * @param value
	 *            Array von Werten, nach denen gesucht werden soll
	 * @return
	 */
	public <T> List<FahrerResultat> selectStartlisteBy(Table_FahrerRennen[] column, T[] value) {
		String where = makeWhere(column, value);
		String selectStmt = "SELECT fahrer_id, rennen_id, kategorie_id, startplatz, startzeit1, startzeit2, f.name, vorname, jahrgang, club_id, club_name, t.name AS kat_name, plz, ort"
				+ " FROM fahrer_rennen JOIN fahrer AS f USING(fahrer_id) JOIN club USING(club_id) JOIN kategorien AS t USING (kategorie_id) "
				+ where;
		if (column[0].equals(Table_FahrerRennen.CLOUMN_ALL))
			selectStmt = "SELECT * FROM fahrer_rennen JOIN fahrer USING(fahrer_id) JOIN club USING(club_id)";
		if (column[0].equals(Table_FahrerRennen.COLUMN_FTS))
			selectStmt = "SELECT fahrer_id, rennen_id, kategorie_id, startplatz, startzeit1, startzeit2, f.name, vorname, jahrgang, club_id, club_name, t.name AS kat_name, plz, ort"
					+ " FROM fahrer_rennen JOIN fahrer AS f USING(fahrer_id) JOIN club USING(club_id) JOIN kategorien AS t USING (kategorie_id) WHERE (rennen_id = "
					+ value[0] + " AND kategorie_id = " + value[1] + ") AND (name LIKE '%" + value[2]
					+ "%' OR vorname LIKE '%" + value[2] + "%' OR jahrgang LIKE '%" + value[2] + "%')";
		selectStmt += " ORDER BY startplatz";
		List<FahrerResultat> fahrer = new ArrayList<FahrerResultat>();
		for (Row row : executeSelect(selectStmt)) {
			Integer idFahrer = (Integer) row.getRow().get(0).getKey();
			Integer rennenID = (Integer) row.getRow().get(1).getKey();
			Integer kategorieID = (Integer) row.getRow().get(2).getKey();
			Integer startplatz = row.getRow().get(3).getKey() == null ? 0 : (Integer) row.getRow().get(3).getKey();
			Time startzeit = (Time) row.getRow().get(4).getKey();
			Time startzeit2 = (Time) row.getRow().get(5).getKey();
			String name = (String) row.getRow().get(6).getKey();
			String vorname = (String) row.getRow().get(7).getKey();
			Integer jg = (Integer) row.getRow().get(8).getKey();

			Integer clubID = (Integer) row.getRow().get(9).getKey();
			String clubName = (String) row.getRow().get(10).getKey();
			String kat_name = (String) row.getRow().get(11).getKey();
			Integer plz = (Integer) row.getRow().get(12).getKey();
			String ort = (String) row.getRow().get(13).getKey();

			startzeit = startzeit == null ? new Time(0) : startzeit;
			startzeit2 = startzeit2 == null ? new Time(0) : startzeit2;
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(startzeit);
			String s1 = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
			cal.setTime(startzeit2);
			String s2 = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
			Rennen rennen = new Rennen();
			rennen.setRennenID(rennenID);
			Fahrer f = new Fahrer(idFahrer, name, vorname, jg, true);
			f.setPlz(plz);
			f.setOrt(ort);
			f.setClub(new Club(clubID, "", clubName));
			fahrer.add(new FahrerResultat(f, rennen, new AltersKategorie(kategorieID, kat_name), s1, s2, startplatz));
		}
		return fahrer;
	}

	/**
	 * Liest die Rennen aus der Datenbank
	 * 
	 * @param column
	 *            Spalte, in der gesucht werden soll
	 * @param value
	 *            Wert, nach dem gesucht werden soll
	 * @return
	 */
	public <T> List<Rennen> selectRennenBy(Table_Rennen column, T value) {
		String selectStmt = "SELECT * FROM rennen where " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Rennen.COLUMN_ALL))
			selectStmt = "SELECT * FROM rennen";
		List<Rennen> rennen = new ArrayList<Rennen>();
		for (Row row : executeSelect(selectStmt)) {
			Integer rennenID = (Integer) row.getRow().get(0).getKey();
			String name = (String) row.getRow().get(1).getKey();
			Date datum = (Date) row.getRow().get(2).getKey();
			Time zeit = (Time) row.getRow().get(3).getKey();
			String ort = (String) row.getRow().get(4).getKey();
			Integer anzTore = (Integer) row.getRow().get(5).getKey();
			Integer anzPosten = (Integer) row.getRow().get(6).getKey();
			// Zeit berechnen
			Calendar cal = Calendar.getInstance();
			cal.setTime(datum);
			Calendar temp = Calendar.getInstance();
			temp.setTime(zeit);
			cal.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
			cal.set(Calendar.SECOND, temp.get(Calendar.SECOND));
			rennen.add(new Rennen(rennenID, name, cal.getTime(), ort, anzTore, anzPosten,
					selectKategorieRennenBy(rennenID)));
		}
		return rennen;
	}

	/**
	 * Liest die Benutzer aus der Datenbank
	 * 
	 * @param column
	 *            Spalte, in der gesucht werden soll
	 * @param value
	 *            Wert, nach dem gesucht werden soll
	 * @return
	 */
	public <T> List<Benutzer> selectBenutzerBy(Table_Benutzer column, T value) {
		String selectStmt = "SELECT * FROM benutzer LEFT JOIN club USING(club_id) WHERE " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Club.CLOUMN_ALL))
			selectStmt = "SELECT * FROM benutzer LEFT JOIN club USING(club_id)";
		List<Benutzer> benutzer = new ArrayList<Benutzer>();
		for (Row row : executeSelect(selectStmt)) {
			Integer idClub = (Integer) row.getRow().get(0).getKey();
			Integer idBenutzer = (Integer) row.getRow().get(1).getKey();
			// String benutzername = (String) row.getRow().get(2).getKey();
			String passwort = (String) row.getRow().get(2).getKey();
			String email = (String) row.getRow().get(3).getKey();
			// String name = (String) row.getRow().get(4).getKey();
			// String vorname = (String) row.getRow().get(5).getKey();
			Integer rechte = (Integer) row.getRow().get(6).getKey();
			// String kennung = (String) row.getRow().get(7).getKey();
			// String club_name = (String) row.getRow().get(8).getKey();
			benutzer.add(new Benutzer(idBenutzer, idClub, email, passwort, rechte)); // TODO
		}
		return benutzer;
	}

	/**
	 * Liest die Kategorien aus der Datenbank
	 * 
	 * @param column
	 *            Die Spalte, in der gesucht werden soll
	 * @param value
	 *            Der Wert, nach dem gesucht werden soll
	 * @return Eine Liste mit den Kategorien
	 */
	public <T> List<AltersKategorie> selectKategorieBy(Table_Kategorien column, T value) {
		String selectStmt = "SELECT * FROM kategorien WHERE " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Kategorien.COLUMN_ALL))
			selectStmt = "SELECT * FROM kategorien;";
		List<AltersKategorie> kategorie = new ArrayList<AltersKategorie>();
		for (Row row : executeSelect(selectStmt)) {
			Integer kategorieID = (Integer) row.getRow().get(0).getKey();
			String name = (String) row.getRow().get(1).getKey();
			kategorie.add(new AltersKategorie(kategorieID, name));
		}
		return kategorie;
	}

	/**
	 * Liest für ein Rennen die zugelassenen Kategorien aus
	 * 
	 * @param rennenID
	 *            Die ID des Rennens
	 * @return eine Liste der zugelassenen Kategorien
	 */
	public <T> List<AltersKategorie> selectKategorieRennenBy(Integer rennenID) {
		String selectStmt = "SELECT * FROM kategorien JOIN kat_rennen USING("
				+ Table_Kat_Rennen.COLUMN_KATEGORIE_ID.getValue() + ") WHERE "
				+ Table_Kat_Rennen.COLUMN_RENNEN_ID.getValue();
		if (rennenID != null)
			selectStmt += " = '" + rennenID + "'";
		List<AltersKategorie> kategorie = new ArrayList<AltersKategorie>();
		for (Row row : executeSelect(selectStmt)) {
			Integer kategorieID = (Integer) row.getRow().get(0).getKey();
			String name = (String) row.getRow().get(1).getKey();
			Integer gebuehr = (Integer) row.getRow().get(3).getKey();
			kategorie.add(new AltersKategorie(kategorieID, name, gebuehr));
		}
		return kategorie;
	}

	public <T> List<FahrerResultat> selectRanglisteBy(Table_Rangliste[] column, T[] value) {
		String selectStmt;
		/*
		 * Gibt die Rangliste aus; nur die bessere Zeit beider Läufe! selectStmt
		 * =
		 * "SELECT min(ges_zeit) AS zeit, fahrer_id, rennen_id, kategorie_id, boot_id, f.name, vorname, c.club_name, c.club_id "
		 * +
		 * "FROM (SELECT zeit1 + t.strafzeit AS ges_zeit, fahrer_id, rennen_id, kategorie_id, boot_id "
		 * +
		 * "FROM (SELECT fahrer_id, rennen_id, kategorie_id, boot_id, lauf, SUM(strafzeit) AS strafzeit "
		 * +
		 * "FROM strafzeiten NATURAL JOIN fahrer_rennen GROUP BY fahrer_id, rennen_id, kategorie_id, "
		 * +
		 * "boot_id, lauf) as t NATURAL JOIN fahrer_rennen WHERE t.lauf = 1 UNION SELECT zeit2 + t.strafzeit "
		 * +
		 * "AS ges_zeit, fahrer_id, rennen_id, kategorie_id, boot_id FROM (SELECT fahrer_id, rennen_id, "
		 * +
		 * "kategorie_id, boot_id, lauf, SUM(strafzeit) AS strafzeit FROM strafzeiten NATURAL JOIN "
		 * +
		 * "fahrer_rennen GROUP BY fahrer_id, rennen_id, kategorie_id, boot_id, lauf) as t "
		 * +
		 * "NATURAL JOIN fahrer_rennen WHERE t.lauf = 2) as b JOIN fahrer as f USING(fahrer_id) JOIN club as c USING(club_id) GROUP BY fahrer_id, rennen_id, "
		 * + "kategorie_id, boot_id WHERE rennen_id = " + rennen.getRennenID() +
		 * ";"; //ORDER BY min(ges_zeit)
		 */
		// Gibt die Zeiten beider Läufe aus
		String where = makeWhere(column, value);
		selectStmt = "SELECT IFNULL(ges_zeit1, 0), IFNULL(ges_zeit2, 0), fahrer_id, rennen_id, kategorie_id, f.name, vorname, c.club_name, "
				+ "c.club_id FROM (SELECT zeit1 + IFNULL(t.strafzeit, 0) AS ges_zeit1, fahrer_id, rennen_id, kategorie_id "
				+ "FROM (SELECT fahrer_id, rennen_id, kategorie_id, lauf, SUM(strafzeit) AS strafzeit FROM fahrer_rennen "
				+ "LEFT JOIN strafzeiten USING(fahrer_id, rennen_id, kategorie_id) GROUP BY fahrer_id, rennen_id, kategorie_id, lauf) as t NATURAL JOIN "
				+ "fahrer_rennen WHERE t.lauf = 1 OR t.lauf IS NULL) as z LEFT JOIN (SELECT zeit2 + IFNULL(t.strafzeit, 0) AS ges_zeit2, fahrer_id, rennen_id, "
				+ "kategorie_id FROM (SELECT fahrer_id, rennen_id, kategorie_id, lauf, SUM(strafzeit) AS strafzeit "
				+ "FROM fahrer_rennen LEFT JOIN strafzeiten USING(fahrer_id, rennen_id, kategorie_id) GROUP BY fahrer_id, rennen_id, kategorie_id, lauf) as t "
				+ "NATURAL JOIN fahrer_rennen WHERE t.lauf = 2 OR t.lauf IS NULL) as b USING(fahrer_id, rennen_id, kategorie_id) JOIN "
				+ "fahrer as f USING(fahrer_id) JOIN club as c USING(club_id) " + where + " ORDER BY kategorie_id;";
		List<FahrerResultat> resultat = new ArrayList<FahrerResultat>();
		for (Row row : executeSelect(selectStmt)) {
			java.math.BigDecimal zeit1 = row.getRow().get(0).getKey() == null ? java.math.BigDecimal.ZERO
					: (java.math.BigDecimal) row.getRow().get(0).getKey();
			java.math.BigDecimal zeit2 = row.getRow().get(1).getKey() == null ? java.math.BigDecimal.ZERO
					: (java.math.BigDecimal) row.getRow().get(1).getKey();
			Integer idFahrer = (Integer) row.getRow().get(2).getKey();
			Integer rennenID = (Integer) row.getRow().get(3).getKey();
			Integer kategorieID = (Integer) row.getRow().get(4).getKey();
			String vorname = (String) row.getRow().get(6).getKey();
			String name = (String) row.getRow().get(5).getKey();
			String clubname = (String) row.getRow().get(7).getKey();
			Integer clubID = (Integer) row.getRow().get(8).getKey();
			Club club = new Club(clubID, "", clubname);
			Fahrer fahrer = new Fahrer(idFahrer, club, name, vorname, 0, "", "", 0, "");
			Rennen rennen = new Rennen();
			rennen.setRennenID(rennenID);
			Integer z1 = zeit1.intValue();
			Integer z2 = zeit2.intValue();
			resultat.add(new FahrerResultat(fahrer, z1, z2, rennen, ladeKategorie(kategorieID)));
		}
		return resultat;
	}

	// --- UTIL METHODS ---
	// Select and update methods are synchronized because the database isolation
	// level may not be strong enough to lock these operations
	// on the database itself.
	// Can be changed if the database isolation level is high enough or the
	// performance is too low .
	// see ->
	// https://en.wikipedia.org/wiki/Isolation_(database_systems)#Read_uncommitted
	// and JAVA Monitoring and synchronizing.

	private <T> String makeWhere(Table_Rangliste[] column, T[] value) {
		String where = "WHERE ";
		for (int x = 0; x < column.length; x++) {
			if (value.length > x) {
				if (x > 0)
					where += " AND ";
				where += column[x].getValue();
				if (value[x] != null)
					where += " = " + value[x];
				else
					where += " IS NULL";
			}
		}
		return where;
	}

	private <T> String makeWhere(Table_FahrerRennen[] column, T[] value) {
		String where = "WHERE ";
		for (int x = 0; x < column.length; x++) {
			if (value.length > x) {
				if (x > 0)
					where += " AND ";
				where += column[x].getValue();
				if (value[x] != null)
					where += " = " + value[x];
				else
					where += " IS NULL";
			}
		}
		return where;
	}

	private <T> String makeWhere(Table_Strafzeit[] column, T[] value) {
		String where = "WHERE ";
		for (int x = 0; x < column.length; x++) {
			if (value.length > x) {
				if (x > 0)
					where += " AND ";
				where += column[x].getValue();
				if (value[x] != null)
					where += " = " + value[x];
				else
					where += " IS NULL";
			}
		}
		where += " AND tor_nr > 0"; // Dummy Einträge rausfiltern
		return where;
	}

	/**
	 * Performs an INSERT, UPDATE, or DELETE statement on the database.
	 *
	 * @param query
	 *            - The query to be executed.
	 * @return An ExecuteResult containing the result of the update operation.
	 */
	private synchronized ExecuteResult executeUpdate(String query) {
		Statement updateStmt = null;
		ExecuteResult result = new ExecuteResult();

		try {
			updateStmt = connection.createStatement();
			updateStmt.executeUpdate(query);
			ResultSet generatedKeys = updateStmt.getGeneratedKeys();
			int i = 1;
			while (generatedKeys.next()) {
				result.addID(generatedKeys.getInt(i));
				i++;
			}
			result.setSuccess(true);
		} catch (SQLException e) {
			// TODO_1: log exceptions
			System.out.println("SQlException: " + e.getMessage());
		} finally {
			try {
				// Release resources
				if (updateStmt != null)
					updateStmt.close();
			} catch (SQLException e) {
				// TODO_1: log exceptions
				System.out.println("SQlException: " + e.getMessage());
			}
		}
		return result;
	}

	/**
	 * Executes a select statement on the database.
	 *
	 * @param query
	 *            - The query to be executed.
	 * @return A list containing the rows returned from the select statement or
	 *         an empty list.
	 */
	private synchronized ArrayList<Row> executeSelect(String query) {
		ArrayList<Row> results = new ArrayList<Row>();

		Statement selectStmt = null;
		ResultSet result = null;
		try {
			selectStmt = connection.createStatement();
			result = selectStmt.executeQuery(query);
			// Fetch rows
			results = Row.formTable(result);
		} catch (SQLException e) {
			// TODO_1: log exceptions
		} finally {
			try {
				// Release resources
				if (selectStmt != null)
					selectStmt.close();
				if (result != null)
					result.close();
			} catch (SQLException e) {
				// TODO_1: log exceptions
			}
		}
		return results;
	}

	/**
	 * Used to get the result of an execute statement.
	 */
	class ExecuteResult {
		private boolean success = false;
		private List<Integer> generatedIDs;

		public ExecuteResult() {
			generatedIDs = new ArrayList<Integer>();
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public boolean isSuccess() {
			return success;
		}

		public void addID(Integer id) {
			generatedIDs.add(id);
		}

		public List<Integer> getGeneratedIDs() {
			return generatedIDs;
		}
	}

	/**
	 * Liest alle Clubs aus der Datenbank aus
	 * 
	 * @return Die Clubs als Liste
	 */
	public List<Club> ladeClubs() {
		return selectClubBy(Table_Club.CLOUMN_ALL, null);
	}

	/**
	 * Liest einen Club nach ID aus der Datenbank aus
	 * 
	 * @param clubID
	 *            Die ID des Clubs
	 * @return Das Club Objekt; null wenns keinen Club mit der ID gibt
	 */
	public Club ladeClubByID(Integer clubID) {
		List<Club> clubs = selectClubBy(Table_Club.COLUMN_ID, clubID);
		if (clubs.size() > 0)
			return clubs.get(0);
		else
			return null; // TODO_1 abklären
	}

	/**
	 * Speichert einen Club in der Datenbank. Um einen neuen Club einzufügen,
	 * muss die ID kleiner als 1 sein, ansonsten wird ein bestehender Club
	 * upgedatet (sofern vorhanden, sonst werden die Daten verworfen)
	 * 
	 * @param club
	 *            Das Club Objekt
	 * @return true wenn erfolgreich, false sonst
	 */
	public boolean speichereClub(Club club) {
		ExecuteResult res;
		if (club.getClubID() < 1)
			res = executeUpdate(
					"INSERT INTO club (kennung, name) VALUES ('" + club.getKennung() + "', '" + club.getName() + "');");
		else
			res = executeUpdate("UPDATE club SET kennung = '" + club.getKennung() + "', name = '" + club.getName()
					+ "' WHERE club_id = " + club.getClubID() + ";");
		return res.isSuccess();
	}

	/**
	 * Führt ein SQL Query aus. Da SELECT querys etwas schwieriger sind als
	 * UPDATE und INSERT querys, werden nur letztgenannte unterstützt
	 * (respektive es wird schlicht nichts zurückgegeben).
	 * 
	 * @param query
	 *            Das SQL Query als solches, muss mit einem ";" abgeschlossen
	 *            werden
	 * @return true wenn das Query erfolgreich ausgeführt wurde, false sonst
	 */
	public boolean runQuery(String query) {
		if (query.matches("(?i)DROP.*")) // DROP wird nicht erlaubt!
			return false;
		ExecuteResult res = executeUpdate(query);
		return res.isSuccess();
	}

	/**
	 * Liest alle Fahrer eines Clubs aus der Datenbank und gibt sie als Liste
	 * zurück
	 * 
	 * @param clubID
	 *            ID des Clubs
	 * @return Liste der Fahrer
	 */
	public List<Fahrer> fahrerlisteClub(Integer clubID) {
		return selectFahrerBy(Table_Fahrer.COLUMN_CLUB_ID, clubID);
	}

	/**
	 * Meldet einen Fahrer für ein Rennen in der gewünschten Alters- sowie
	 * Bootskategorie an.
	 * 
	 * @param fahrerID
	 *            Die Fahrer ID
	 * @param rennenID
	 *            Die Rennen ID
	 * @param Kategorie
	 *            Die Alterskategorie
	 * @return true wenn erfolgreich, false sonst
	 */
	public boolean fahrerAnmelden(Integer fahrerID, Integer rennenID, Integer altersKategorie) {
		ExecuteResult res = executeUpdate("INSERT INTO fahrer_rennen (fahrer_id, rennen_id, kategorie_id) VALUES ("
				+ fahrerID + ", " + rennenID + ", " + altersKategorie + ");");
		return res.isSuccess();
	}

	/**
	 * Meldet einen Fahrer von einem Rennen ab (alle Kategorien!)
	 * 
	 * @param fahrerID
	 *            Die Fahrer ID
	 * @param rennenID
	 *            Die Rennen ID
	 * @return true wenn erfolgreich, false sonst
	 */
	public boolean fahrerAbmelden(Integer fahrerID, Integer rennenID) {
		ExecuteResult res = executeUpdate(
				"DELETE FROM fahrer_rennen WHERE fahrer_id = " + fahrerID + " AND rennen_id = " + rennenID + ";");
		return res.isSuccess();
	}

	/**
	 * Meldet einen Fahrer von einem Rennen ab (eine Kategorie und Bootsklasse)
	 * 
	 * @param fahrerID
	 *            Die Fahrer ID
	 * @param rennenID
	 *            Die Rennen ID
	 * @param KategorieID
	 *            Die ID der Kategorie
	 * @return true wenn abgemeldet, false sonst
	 */
	public boolean fahrerAbmelden(Integer fahrerID, Integer rennenID, Integer alterskategorieID) {
		ExecuteResult res = executeUpdate("DELETE FROM fahrer_rennen WHERE fahrer_id = " + fahrerID
				+ " AND rennen_id = " + rennenID + " AND kategorie_id = " + alterskategorieID + ";");
		return res.isSuccess();
	}

	/**
	 * Liest zu einer gegebenen ID den Fahrer aus der Datenbank
	 * 
	 * @param fahrerID
	 *            Die ID des gewünschten Fahrers
	 * @return Das Fahrer Objekt, null wenn Fahrer nicht vorhanden
	 */
	public Fahrer ladeFahrer(Integer fahrerID) {
		List<Fahrer> fahrer = selectFahrerBy(Table_Fahrer.COLUMN_ID, fahrerID);
		if (fahrer.size() > 0)
			return fahrer.get(0);
		else
			return null; // TODO_1 abzuklären
	}

	/**
	 * Speichert einen Fahrer in der Datenbank. Um einen neuen Fahrer
	 * einzufügen, muss die ID kleiner als 1 sein, ansonsten wird ein
	 * bestehender Fahrer upgedatet (sofern vorhanden, sonst werden die Daten
	 * verworfen)
	 * 
	 * @param fahrer
	 *            Der Fahrer mit den Infos
	 * @return boolean true wenn erfolgreich, false sonst
	 */
	public boolean speichereFahrer(Fahrer fahrer) {
		return speichereFahrerIntern(fahrer) > 0;
	}

	/**
	 * Speichert einen neuen Fahrer in der Datenbank und gibt die ID des Fahrers
	 * zurück.
	 * 
	 * @param fahrer
	 *            Der Fahrer
	 * @return Die ID des Fahrers, sofern gespeichert, -1 sonst
	 */
	public Integer speichereNeuenFahrer(Fahrer fahrer) {
		fahrer.setFahrerID(0);
		return speichereFahrerIntern(fahrer);
	}

	/**
	 * Speichert den Fahrer
	 * 
	 * @param fahrer
	 * @return
	 */
	private Integer speichereFahrerIntern(Fahrer fahrer) {
		ExecuteResult res;
		Integer id = -1;
		if (fahrer.getFahrerID() < 1 || fahrer.getFahrerID() == null) {
			res = executeUpdate("INSERT INTO fahrer (club_id, name, vorname, jahrgang, telnr, strasse, plz, ort) "
					+ "VALUES (" + fahrer.getClub().getClubID() + ", '" + fahrer.getName() + "', '"
					+ fahrer.getVorname() + "', " + fahrer.getJahrgang() + ", '" + fahrer.getTelNr() + "', '"
					+ fahrer.getStrasse() + "', " + fahrer.getPlz() + ", '" + fahrer.getOrt() + "');");
			if (res.isSuccess())
				id = res.getGeneratedIDs().get(0);
		} else {
			res = executeUpdate("UPDATE fahrer SET club_id = " + fahrer.getClub().getClubID() + ", name = '"
					+ fahrer.getName() + "', vorname = '" + fahrer.getVorname() + "', jahrgang = "
					+ fahrer.getJahrgang() + ", telnr = '" + fahrer.getTelNr() + "', strasse = '" + fahrer.getStrasse()
					+ "', plz = " + fahrer.getPlz() + ", ort = '" + fahrer.getOrt() + "' WHERE fahrer_id = "
					+ fahrer.getFahrerID() + ";");
			if (res.isSuccess())
				id = fahrer.getFahrerID();
		}
		return id;
	}

	/**
	 * Löscht einen Fahrer aus der Datenbank
	 * 
	 * @param fahrerID
	 *            Die ID des zu löschenden Fahrers
	 * @return true wenn erfolgreich, false sonst
	 */
	public boolean fahrerLoeschen(Integer fahrerID) {
		ExecuteResult res = executeUpdate("DELETE FROM fahrer WHERE fahrer_id = " + fahrerID + ";");
		return res.isSuccess();
	}

	/**
	 * Speichert einen Fahrer inklusive Rennergebnis.
	 * 
	 * @param fahrer
	 *            Das Fahrer Objekt
	 * @param resultat
	 *            Das FahrerResultat Objekt
	 * @return true wenn erfolgreich, false sonst
	 */
	public boolean speichereFahrer(Fahrer fahrer, FahrerResultat resultat) {
		ExecuteResult res;
		res = executeUpdate("INSERT INTO fahrer_rennen (fahrer_id, rennen_id, kategorie_id, startplatz, zeit1, zeit2)"
				+ " VALUES (" + fahrer.getFahrerID() + ", " + resultat.getRennen().getRennenID() + ", "
				+ resultat.getKategorie().getAltersKategorieID() + ", " + resultat.getStartnummer() + ", "
				+ resultat.getZeitErsterLauf() + ", " + resultat.getZeitZweiterLauf() + ") ON DUPLICATE KEY UPDATE "
				+ "startplatz = " + resultat.getStartnummer() + ", zeit1 = " + resultat.getZeitErsterLauf() + ", "
				+ "zeit2 = " + resultat.getZeitZweiterLauf() + ";");
		return speichereFahrer(fahrer) && res.isSuccess();
	}

	/**
	 * Lädt einen Benutzer aus der Datenbank
	 * 
	 * @param benutzerID
	 *            Die Benutzer ID
	 * @return Das Benutzer Objekt, null wenn Benutzer nicht vorhanden
	 */
	public Benutzer ladeBenutzer(Integer benutzerID) {
		List<Benutzer> benutzer = selectBenutzerBy(Table_Benutzer.COLUMN_ID, benutzerID);
		if (benutzer.size() > 0)
			return benutzer.get(0);
		else
			return null; // TODO_1 abzuklären
	}

	/**
	 * Funktion lädt einen Benutzer mit der Email Adresse
	 * 
	 * @param emailAdresse
	 * @return
	 */
	public Benutzer ladeBenutzerMitEmail(String emailAdresse) {
		List<Benutzer> benutzer = selectBenutzerBy(Table_Benutzer.COLUMN_EMAIL, emailAdresse);
		if (benutzer.size() > 0)
			return benutzer.get(0);
		else
			return null; // TODO_1 abzuklären
	}

	/**
	 * Speichert einen Benutzer. Ist die ID kleiner als 1, wird ein neuer
	 * Benutzer angelegt, ansonsten ein bestehender geupdatet. Ist kein Benutzer
	 * mit der ID vorhanden, werden die Daten verworfen.
	 * 
	 * @param benutzer
	 *            Das Benutzer Objekt
	 * @return true wenn erfolg, false sonst
	 */
	public boolean speichereBenutzer(Benutzer benutzer) {
		ExecuteResult res;
		if (benutzer.getBenutzerID() < 1) {
			res = executeUpdate("INSERT INTO benutzer (passwort, email, rechte) " + "VALUES ('" + benutzer.getPasswort()
					+ "', '" + benutzer.getEmailAdresse() + "', " + benutzer.getRechte() + ");");
		} else {
			res = executeUpdate("UPDATE benutzer SET email = '" + benutzer.getEmailAdresse() + "', passwort = '"
					+ benutzer.getPasswort() + "', rechte = " + benutzer.getRechte() + " WHERE user_id = "
					+ benutzer.getBenutzerID() + ";");
		}
		return res.isSuccess();
	}

	/**
	 * Welches Rennen denn? Die ID als Argument fehlt...
	 * 
	 * @return
	 */
	@Deprecated
	public List<Club> ladeAngemeldeteClubs() { // TODO_2 löschen, nicht
												// brauchbar
		return new ArrayList<Club>();
	}

	/**
	 * Gibt alle Clubs für ein gegebenes Rennen zurück
	 * 
	 * @param rennenID
	 *            Die ID des Rennens
	 * @return Eine Liste von Clubs
	 */
	public List<Club> ladeAngemeldeteClubs(Integer rennenID) {
		String selectStmt = "SELECT DISTINCT club_id, kennung, club_name FROM fahrer_rennen NATURAL JOIN fahrer NATURAL JOIN club WHERE rennen_id = "
				+ rennenID;
		List<Club> clubs = new ArrayList<Club>();

		for (Row row : executeSelect(selectStmt)) {
			Integer idClub = (Integer) row.getRow().get(0).getKey();
			String clubKennung = (String) row.getRow().get(1).getKey();
			String name = (String) row.getRow().get(2).getKey();
			clubs.add(new Club(idClub, clubKennung, name));
		}
		return clubs;
	}

	/**
	 * Liste mit allen Fahrer vom Club Sollte ein Fahrer für mehrere Kategorien
	 * im gleichen Rennen angemeldet sein, kommt dieser Fahrer mehrfach vor!
	 * 
	 * @param clubID
	 *            Die ID des Clubs
	 * @return Eine Liste mit Fahrer. Fahrer, welche angemeldet sind, haben den
	 *         Typ "FahrerRennen", die andern nur "Fahrer"
	 */
	public List<FahrerResultat> ladeFahreranmeldungslisteClub(int clubID) { // TODO
																			// Rennen
																			// ID
		ArrayList<FahrerResultat> fahrer = new ArrayList<FahrerResultat>();
		fahrer.addAll(selectStartlisteBy(new Table_FahrerRennen[] { Table_FahrerRennen.COLUMN_CLUB_ID },
				new Integer[] { clubID }));
		// Nur Fahrer hinzufügen, welche noch nicht vorhanden sind
		/*
		 * for(Fahrer f : selectFahrerBy(Table_Fahrer.COLUMN_CLUB_ID, clubID))
		 * //TODO löschen? { boolean vorhanden = false; for(FahrerResultat x :
		 * fahrer) { if(x.getFahrer().getFahrerID() == f.getFahrerID()) {
		 * vorhanden = true; break; } } if(!vorhanden) fahrer.add(new
		 * FahrerResultat(f, null, null, null, null, null)); }
		 */
		return fahrer;
	}

	/**
	 * Lädt die Startliste eines Rennens und gibt eine Liste von FahrerRennen
	 * Objekten zurück
	 * 
	 * @param rennenID
	 *            Die ID des Rennens
	 * @return Eine Liste von Fahrern
	 */
	public List<FahrerResultat> ladeStartliste(Integer rennenID) {
		return selectStartlisteBy(new Table_FahrerRennen[] { Table_FahrerRennen.COLUMN_RENNEN_ID },
				new Integer[] { rennenID });
	}

	/**
	 * Liest zu einer bestimmten Kategorie von einem Rennen die Startliste aus
	 * 
	 * @param rennenID
	 *            Die ID des Rennens
	 * @param kategorieID
	 *            Die ID der Kategorie
	 * @param bootID
	 *            Die ID der Bootsklasse
	 * @return Eine Liste von Fahrern
	 */
	public List<FahrerResultat> ladeStartliste(Integer rennenID, Integer kategorieID) {
		return selectStartlisteBy(
				new Table_FahrerRennen[] { Table_FahrerRennen.COLUMN_RENNEN_ID, Table_FahrerRennen.COLUMN_KATEGORIE },
				new Integer[] { rennenID, kategorieID });
	}

	/**
	 * Lädt die Startliste mit der Suche aus der Datenbank
	 * 
	 * @param rennenID
	 *            Die ID des Rennens
	 * @param kategorieID
	 *            Die Kategorie ID
	 * @param suche
	 *            Der String, nach dem gesucht werden soll
	 * @return
	 */
	public List<FahrerResultat> ladeStartlisteMitSuche(Integer rennenID, Integer kategorieID, String suche) {
		return selectStartlisteBy(new Table_FahrerRennen[] { Table_FahrerRennen.COLUMN_FTS },
				new String[] { rennenID + "", kategorieID + "", suche });
	}

	/**
	 * Speichert den Startplatz und die Startzeit eines Fahrer zu einem Rennen
	 * 
	 * @param fahrer
	 *            Das Objekt mit dem Fahrer
	 * @return true wenn ok, false sonst
	 */
	public boolean speichereStartplatz(FahrerResultat fahrer) {
		String s1 = fahrer.getStartzeitEins() == null ? "startzeit1" : "'" + fahrer.getStartzeitEins() + "'";
		String s2 = fahrer.getStartzeitZwei() == null ? "startzeit2" : "'" + fahrer.getStartzeitZwei() + "'";
		String sp = fahrer.getStartnummer() == 0 ? "startplatz" : fahrer.getStartnummer() + "";
		ExecuteResult res = executeUpdate("UPDATE fahrer_rennen SET startzeit1 = " + s1 + ", startzeit2 = " + s2 + ", "
				+ "startplatz = " + sp + " WHERE fahrer_id = " + fahrer.getFahrer().getFahrerID() + " AND rennen_id = "
				+ fahrer.getRennen().getRennenID() + " AND kategorie_id = "
				+ fahrer.getKategorie().getAltersKategorieID() + "");
		return res.isSuccess();
	}

	/**
	 * Erfasst eine Strafzeit zu einem Fahrer und Rennen. Überschreibt eine
	 * bereits vorhandene Strafzeit zu einem Tor!
	 * 
	 * @param fahrerID
	 *            Die Fahrer ID
	 * @param rennenID
	 *            Die Rennen ID
	 * @param kategorieID
	 *            Die ID der Kategorie
	 * @param lauf
	 *            1 für 1. Lauf, 2 für 2. Lauf
	 * @param tornummer
	 *            Die Tornummer
	 * @param strafzeit
	 *            Die Strafzeit in Sekunden
	 * @return true wenn gespeichert, false sonst
	 */
	public boolean fehlerErfassen(Integer fahrerID, Integer rennenID, Integer kategorieID, Integer lauf,
			Integer tornummer, Integer strafzeit) {
		ExecuteResult res = executeUpdate(
				"INSERT INTO strafzeiten (fahrer_id, rennen_id, kategorie_id, lauf, tor_nr, strafzeit) VALUES " + "("
						+ fahrerID + ", " + rennenID + ", " + kategorieID + ", " + lauf + ", " + tornummer + ", "
						+ strafzeit + ") ON DUPLICATE KEY UPDATE strafzeit = " + strafzeit + ";");
		return res.isSuccess();
	}

	/**
	 * Löscht eine Strafzeit wieder
	 * 
	 * @param fahrerID
	 *            Die Fahrer ID
	 * @param rennenID
	 *            Die Rennen ID
	 * @param kategorieID
	 *            Die ID der Kategorie
	 * @param lauf
	 *            1 für 1. Lauf, 2 für 2. Lauf
	 * @param tornummer
	 *            Die Tornummer
	 * @return true wenn gelöscht, false sonst
	 */
	public boolean fehlerLoeschen(Integer fahrerID, Integer rennenID, Integer kategorieID, Integer lauf,
			Integer tornummer) {
		ExecuteResult res = executeUpdate("DELETE FROM strafzeiten WHERE fahrer_id = " + fahrerID + " AND"
				+ " rennen_id = " + rennenID + " AND tor_nr = " + tornummer + " AND kategorie_id = " + kategorieID
				+ " AND lauf = " + lauf + ";");
		return res.isSuccess();
	}

	/**
	 * Lädt alle Fahrer aus der Datenbank
	 * 
	 * @return Eine Liste mit allen Fahrern
	 */
	public List<Fahrer> ladeFahrermutationslisteAlle() {
		return selectFahrerBy(Table_Fahrer.CLOUMN_ALL, null);
	}

	public List<Fahrer> ladeFahrermutationslisteAlleMitSuche(String suche) {
		return selectFahrerBy(Table_Fahrer.COLUMN_FTS, suche);
	}

	public List<FahrerResultat> ladeAngemeldetenFahrerByClub(Integer clubID, Integer rennenID) {
		return selectRanglisteBy(
				new Table_Rangliste[] { Table_Rangliste.COLUMN_CLUB_ID, Table_Rangliste.COLUMN_RENNEN_ID },
				new Integer[] { clubID, rennenID });
	}

	/**
	 * fahrerlisteClub() benutzen!
	 * 
	 * @param clubID
	 * @return
	 */
	@Deprecated
	public List<Fahrer> ladeFahrermutationslisteClub(Integer clubID) { // TODO_2
																		// löschen,
																		// doppelt
		return selectFahrerBy(Table_Fahrer.COLUMN_CLUB_ID, clubID);
	}

	/**
	 * Liest zu einem Fahrer alle(!) Resultate aus
	 * 
	 * @param fahrerID
	 *            Die Fahrer ID
	 * @return Eine Liste von FahrerResultaten
	 */
	public List<FahrerResultat> ladeFahrerresultat(Integer fahrerID) {
		return selectRanglisteBy(new Table_Rangliste[] { Table_Rangliste.COLUMN_FAHRER_ID },
				new Integer[] { fahrerID });
	}

	/**
	 * Liest zu einem Fahrer alle(!) Resultate aus
	 * 
	 * @param fahrerID
	 *            Die Fahrer ID
	 * @return Eine Liste von FahrerResultaten
	 */
	public FahrerResultat ladeFahrerresultatByKategorie(Integer fahrerID, Integer katID, Integer rennenID) {
		return selectRanglisteBy(new Table_Rangliste[] { Table_Rangliste.COLUMN_FAHRER_ID,
				Table_Rangliste.COLUMN_KATEGORIE, Table_Rangliste.COLUMN_RENNEN_ID },
				new Integer[] { fahrerID, katID, rennenID }).get(0);
	}

	/**
	 * speichereFahrer() benutzen!
	 * 
	 * @param fahrer
	 */
	@Deprecated
	public void speichereFahrerBearbeitenAlle(Fahrer fahrer) {
		speichereFahrer(fahrer);
	}

	/**
	 * speichereFahrer() benutzen!
	 * 
	 * @param fahrer
	 */
	@Deprecated
	public void speichereFahrerBearbeitenClub(Fahrer fahrer) {
		speichereFahrer(fahrer);
	}

	/**
	 * Liest die Rennen aus der Datenbank
	 * 
	 * @return Eine Liste mit Rennen
	 */
	public List<Rennen> ladeRennen() {
		return selectRennenBy(Table_Rennen.COLUMN_ALL, null);
	}

	/**
	 * Liest ein Rennen aus der Datenbank
	 * 
	 * @param rennenID
	 *            Die ID des Rennens
	 * @return Das Rennen
	 */
	public Rennen ladeRennen(Integer rennenID) {
		List<Rennen> rennen = selectRennenBy(Table_Rennen.COLUMN_ID, rennenID);
		if (rennen.size() > 0)
			return rennen.get(0);
		else
			return null; // TODO_1 abzuklären
	}

	/**
	 * Speichert ein Rennen in der Datenbank. Ist die ID des Rennens kleiner als
	 * 1, wird ein neues Rennen gespeichert, ist die ID grösser als 1, wird ein
	 * bestehendes Rennen geupdatet. Ist die ID nicht vorhanden, werden die
	 * Daten verworfen.
	 * 
	 * @param rennen
	 *            Das Rennen Objekt
	 * @return true wenn erfolgreich, false sonst
	 */
	public boolean speichereRennen(Rennen rennen) {
		ExecuteResult res;
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(rennen.getDatumVon());
		String zeit = String.format("%02d:%02d:00", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		System.out.println(zeit);
		if (rennen.getRennenID() < 1) {
			res = executeUpdate("INSERT INTO rennen (name, datum, zeit, ort, anz_tore, anz_posten) VALUES " + "('"
					+ rennen.getName() + "', FROM_UNIXTIME(" + rennen.getDatumVon().getTime() / 1000 + "), '" + zeit
					+ "', " + "'" + rennen.getOrt() + "', " + rennen.getAnzTore() + ", " + rennen.getAnzPosten()
					+ ");");
			if (res.isSuccess())
				rennen.setRennenID(res.generatedIDs.get(0));
		} else {
			res = executeUpdate("UPDATE rennen SET name = '" + rennen.getName() + "', datum = FROM_UNIXTIME("
					+ rennen.getDatumVon().getTime() / 1000 + "), " + "zeit = '" + zeit + "', ort = '" + rennen.getOrt()
					+ "', anz_tore = " + rennen.getAnzTore() + ", " + "anz_posten = " + rennen.getAnzPosten()
					+ " WHERE rennen_id = " + rennen.getRennenID() + ";");
			if (res.isSuccess())
				res = executeUpdate("DELETE FROM kat_rennen WHERE rennen_id = " + rennen.getRennenID());
		}
		String sql = "INSERT INTO kat_rennen (rennen_id, kategorie_id, gebuehr) VALUES ";
		int x = 0;
		for (AltersKategorie kat : rennen.getKategorien()) {
			if (x > 0)
				sql += ", ";
			x++;
			sql += "(" + rennen.getRennenID() + ", " + kat.getAltersKategorieID() + ", " + kat.getGebuehr() + ")";
		}
		if (res.isSuccess())
			res = executeUpdate(sql);
		return res.isSuccess();
	}

	/**
	 * Gibt alle Kategorien zurück, die in der db gespeichert sind
	 * 
	 * @return Eine Liste mit Kategorien
	 */
	public List<AltersKategorie> ladeKategorien() {
		return selectKategorieBy(Table_Kategorien.COLUMN_ALL, null);
	}

	/**
	 * Liest eine Kategorie zur Datenbank aus
	 * 
	 * @param kategorieID
	 *            Die ID der Kategorie
	 * @return Die Kategorie
	 */
	public AltersKategorie ladeKategorie(Integer kategorieID) {
		List<AltersKategorie> kategorien = selectKategorieBy(Table_Kategorien.COLUMN_ID, kategorieID);
		if (kategorien.size() > 0)
			return kategorien.get(0);
		else
			return null; // TODO_1 abklären
	}

	/**
	 * Das macht grundsätzlich nicht der DB Controller. Der DB Kontroller stellt
	 * die beiden Funktionen (an und abmelden) zur Verfügung
	 * 
	 * @param fahrerID
	 * @param kategorie
	 * @param neueKategorie
	 * @param rennenID
	 * @return
	 */
	public boolean setzeNeueKategorie(Integer fahrerID, Integer kategorie, Integer neueKategorie, Integer rennenID) {
		boolean tmp = fahrerAbmelden(fahrerID, rennenID, kategorie);
		return tmp && fahrerAnmelden(fahrerID, rennenID, neueKategorie);
	}

	/**
	 * Gibt die Resultate der Fahrer zu einem Rennen zurück
	 * 
	 * @param rennen
	 *            Das Objekt des Rennens
	 * @return ein Ranglistenobjekt, das die FahrerResultate zum gegebenen
	 *         Rennen beinhaltet
	 */
	public Rangliste ladeRanglisteRennen(Rennen rennen) {
		List<FahrerResultat> resultat = selectRanglisteBy(new Table_Rangliste[] { Table_Rangliste.COLUMN_RENNEN_ID },
				new Integer[] { rennen.getRennenID() });
		Rangliste rl = new Rangliste();
		rl.setRennen(rennen);
		rl.setResultate(resultat);
		return rl;
	}

	/**
	 * Gibt die Rangliste einer Kategorie zu einem Rennen zurück
	 * 
	 * @param rennen
	 *            Das Objekt des Rennens
	 * @param altersKategorieID
	 *            Die ID der Kategorie
	 * @return Ein Ranglistenobjekt, das die FahrerResultate zum gegebenen
	 *         Rennen beinhaltet
	 */
	public Rangliste ladeRanglisteAltersKategorie(Rennen rennen, Integer altersKategorieID) {
		List<FahrerResultat> resultat = selectRanglisteBy(
				new Table_Rangliste[] { Table_Rangliste.COLUMN_RENNEN_ID, Table_Rangliste.COLUMN_KATEGORIE },
				new Integer[] { rennen.getRennenID(), altersKategorieID });
		Rangliste rl = new Rangliste();
		rl.setRennen(rennen);
		rl.setResultate(resultat);
		return rl;
	}

	/**
	 * Löscht die Blöcke inkl. deren Kategorien eines Rennens aus der Datenbank
	 * 
	 * @param rennenID
	 *            Die ID des Rennens
	 * @return true wenn erfolgreich, false sonst
	 */
	public boolean loescheBloecke(Integer rennenID) {
		ExecuteResult res = executeUpdate("DELETE FROM block_rennen WHERE rennen_id = " + rennenID);
		return res.isSuccess();
	}

	/**
	 * Liest zu einem Rennen die Blöcke inkl. Kategorien aus der Datenbank
	 * 
	 * @param rennenID
	 *            Die ID des Rennens
	 * @return Eine Liste mit Kategorien
	 */
	public List<AltersKategorie> ladeBloecke(Integer rennenID) {
		return selectBloeckeBy(Table_Block_Rennen.COLUMN_RENNEN_ID, rennenID);
	}

	/**
	 * Speichert die Konfiguration eines Blockes
	 * 
	 * @param rennenID
	 * @param blockNr
	 * @param kategorien
	 * @return
	 */
	public boolean speichereBlock(Integer rennenID, Integer blockNr, List<Integer> kategorien) {
		String sql = "INSERT INTO block_rennen (rennen_id, block_nr, kategorie_id, nr) VALUES ";
		int x = 0;
		for (Integer k : kategorien) {
			if (x > 0)
				sql += ", ";
			x++;
			sql += "(" + rennenID + ", " + blockNr + ", " + k + ", " + x + ")";
		}
		ExecuteResult res = executeUpdate(sql);
		return res.isSuccess();
	}

	/**
	 * Liest die Strafzeiten zur Datenbank raus.
	 * 
	 * @param fahrerID
	 *            Die Fahrer ID
	 * @param rennenID
	 *            Die Rennen ID
	 * @param kategorieID
	 *            Die ID der Kategorie
	 * @param lauf
	 *            Der Lauf
	 * @return Eine Liste mit Strafzeiten (ist die Liste leer, wurde noch nichts
	 *         erfasst oder der Lauf wurde fehlerfrei durchgeführt)
	 */
	public List<Strafzeit> ladeStrafzeit(Integer fahrerID, Integer rennenID, Integer kategorieID, Integer lauf) {
		return selectStrafzeitBy(
				new Table_Strafzeit[] { Table_Strafzeit.COLUMN_FAHRER_ID, Table_Strafzeit.COLUMN_RENNEN_ID,
						Table_Strafzeit.COLUMN_KATEGORIE, Table_Strafzeit.COLUMN_LAUF },
				new Integer[] { fahrerID, rennenID, kategorieID, lauf });
	}

	/**
	 * Speicher eine Strafzeit in der Datenbank; ist für dieses Tor in diesem
	 * Rennen bereits eine Strafzeit eingetragen, wird diese überschrieben!
	 * 
	 * @param fahrerID
	 *            Die Fahrer ID
	 * @param rennenID
	 *            Die Rennen ID
	 * @param kategorieID
	 *            Die Kategorie ID
	 * @param lauf
	 *            Der Lauf
	 * @param tor
	 *            Das Tor
	 * @param strafzeit
	 *            Die Strafzeit (in ms)
	 * @return true wenn erfolgreich, false sonst
	 */
	public boolean speichereStrafzeit(Integer fahrerID, Integer rennenID, Integer kategorieID, Integer lauf,
			Integer tor, Integer strafzeit) {
		boolean tmp = speichereStrafzeitIntern(fahrerID, rennenID, kategorieID, lauf, tor, strafzeit);
		lauf = lauf == 1 ? 2 : 1; // Dummy Eintrag, siehe Beschreibung von
									// interner Methode
		return tmp && speichereStrafzeitIntern(fahrerID, rennenID, kategorieID, lauf, 0, 0);
	}

	/**
	 * Speichert einen Fehler in der db Als Workaround, weil die Rangliste sonst
	 * nicht richtig aus der Datenbank gelgesen wird, muss für jeden Eintrag zum
	 * einen Lauf mindestens ein Eintrag zum anderen Lauf erstellt werden. Dies
	 * wird hier mit Tor 0 und einer Strafzeit von 0 gemacht (dieser Dummy
	 * Eintrag wird nicht zurückgegeben).
	 * 
	 * @param fahrerID
	 * @param rennenID
	 * @param kategorieID
	 * @param lauf
	 * @param tor
	 * @param strafzeit
	 * @return
	 */
	private boolean speichereStrafzeitIntern(Integer fahrerID, Integer rennenID, Integer kategorieID, Integer lauf,
			Integer tor, Integer strafzeit) {
		ExecuteResult res = executeUpdate(
				"INSERT INTO strafzeiten (fahrer_id, rennen_id, kategorie_id, lauf, tor_nr, strafzeit) VALUES" + " ("
						+ fahrerID + ", " + rennenID + ", " + kategorieID + ", " + lauf + ", " + tor + ", " + strafzeit
						+ ") ON DUPLICATE KEY UPDATE" + " strafzeit = " + strafzeit);
		return res.isSuccess();
	}
}