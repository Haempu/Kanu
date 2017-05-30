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
import java.util.List;

import ch.bfh.project1.kanu.model.AltersKategorie;
import ch.bfh.project1.kanu.model.Benutzer;
import ch.bfh.project1.kanu.model.Club;
import ch.bfh.project1.kanu.model.Fahrer;
import ch.bfh.project1.kanu.model.FahrerResultat;
import ch.bfh.project1.kanu.model.Rangliste;
import ch.bfh.project1.kanu.model.Rennen;
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
		COLUMN_RENNEN_ID("rennen_id"), COLUMN_FAHRER_ID("fahrer_id"), COLUMN_KATEGORIE(
				"kategorie_id"), COLUMN_BOOTKLASSE("boot_id"), CLOUMN_ALL("*");

		private final String column;

		Table_Rangliste(String column) {
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

	public enum Table_Fahrer {
		COLUMN_ID("fahrer_id"), COLUMN_CLUB_ID("club_id"), COLUMN_NAME("name"), COLUMN_VORNAME(
				"vorname"), COLUMN_JAHRGANG(
						"jahrgang"), COLUMN_TELNR("telnnr"), COLUMN_PLZ("plz"), COLUMN_ORT("ort"), CLOUMN_ALL("*");

		private final String column;

		Table_Fahrer(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_FahrerRennen {
		COLUMN_FAHRER_ID("fahrer_id"), COLUMN_RENNEN_ID("rennen_id"), COLUMN_KATEGORIE(
				"kategorie_id"), COLUMN_BOOTKLASSE(
						"boots_kategorie"), COLUMN_BEZAHLT("bezahlt"), COLUMN_CLUB_ID("club_id"), CLOUMN_ALL("*");

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
	 * Liest die Fahrer inkl. Club aus der Datenbank raus.
	 * 
	 * @param column
	 *            Spalte, in der gesucht werden soll
	 * @param value
	 *            Wert, nach dem gesucht werden soll
	 * @return Liste der Fahrer, die die Suchkriterien erfüllen
	 */
	public <T> List<Fahrer> selectFahrerBy(Table_Fahrer column, T value) {
		String selectStmt = "SELECT fahrer_id, club_id, name, vorname, jahrgang, telnr, strasse, plz, ort FROM fahrer JOIN club USING(club_id) WHERE "
				+ column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Fahrer.CLOUMN_ALL))
			selectStmt = "SELECT fahrer_id, club_id, name, vorname, jahrgang, telnr, strasse, plz, ort FROM fahrer JOIN club USING(club_id)";
		List<Fahrer> fahrer = new ArrayList<Fahrer>();
		for (Row row : executeSelect(selectStmt)) {
			Integer idFahrer = (Integer) row.getRow().get(0).getKey();
			Integer idClub = (Integer) row.getRow().get(1).getKey();
			Club club = ladeClubByID(idClub);
			String name = (String) row.getRow().get(2).getKey();
			String vorname = (String) row.getRow().get(3).getKey();
			Integer jg = (Integer) row.getRow().get(4).getKey();
			String telnr = (String) row.getRow().get(5).getKey();
			String strasse = (String) row.getRow().get(6).getKey();
			Integer plz = (Integer) row.getRow().get(7).getKey();
			String ort = (String) row.getRow().get(8).getKey();
			fahrer.add(new Fahrer(idFahrer, club, name, vorname, jg, telnr, strasse, plz, ort));
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
		String where = makeWhereRennen(column, value);
		String selectStmt = "SELECT fahrer_id, club_id, rennen_id, kategorie_id, startplatz, startzeit1, startzeit2, zeit1, zeit2 FROM fahrer_rennen JOIN fahrer USING(fahrer_id) JOIN club USING(club_id) "
				+ where;

		if (column.equals(Table_Club.CLOUMN_ALL))
			selectStmt = "SELECT fahrer_id, club_id, rennen_id, kategorie_id, startplatz, startzeit1, startzeit2, zeit1, zeit2 FROM fahrer_rennen JOIN fahrer USING(fahrer_id) JOIN club USING(club_id)";
		List<FahrerResultat> fahrer = new ArrayList<FahrerResultat>();
		for (Row row : executeSelect(selectStmt)) {
			Integer idFahrer = (Integer) row.getRow().get(0).getKey();
			Fahrer f = ladeFahrer(idFahrer);
			Integer clubID = (Integer) row.getRow().get(1).getKey();
			Club club = ladeClubByID(clubID);
			Integer rennenID = (Integer) row.getRow().get(2).getKey();
			Rennen r = ladeRennen(rennenID);
			Integer kategorieID = (Integer) row.getRow().get(3).getKey();
			AltersKategorie kat = ladeKategorie(kategorieID);
			Integer startplatz = (Integer) row.getRow().get(4).getKey();
			// String startzeit1 = (String) row.getRow().get(5).getKey();
			// String startzeit2 = (String) row.getRow().get(6).getKey();
			// String laufzeit1 = (String) row.getRow().get(8).getKey();
			// String laufzeit2 = (String) row.getRow().get(9).getKey();
			// TODO: change 0.0d and null values
			fahrer.add(new FahrerResultat(f, 0.0d, 0.0d, r, kat, startplatz, null, null));
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
		selectStmt = "SELECT ges_zeit1, ges_zeit2, fahrer_id, rennen_id, kategorie_id, boot_id, f.name, vorname, c.club_name, "
				+ "c.club_id FROM (SELECT zeit1 + t.strafzeit AS ges_zeit1, fahrer_id, rennen_id, kategorie_id, boot_id "
				+ "FROM (SELECT fahrer_id, rennen_id, kategorie_id, boot_id, lauf, SUM(strafzeit) AS strafzeit FROM strafzeiten "
				+ "NATURAL JOIN fahrer_rennen GROUP BY fahrer_id, rennen_id, kategorie_id, boot_id, lauf) as t NATURAL JOIN "
				+ "fahrer_rennen WHERE t.lauf = 1) as z LEFT JOIN (SELECT zeit2 + t.strafzeit AS ges_zeit2, fahrer_id, rennen_id, "
				+ "kategorie_id, boot_id FROM (SELECT fahrer_id, rennen_id, kategorie_id, boot_id, lauf, SUM(strafzeit) AS strafzeit "
				+ "FROM strafzeiten NATURAL JOIN fahrer_rennen GROUP BY fahrer_id, rennen_id, kategorie_id, boot_id, lauf) as t "
				+ "NATURAL JOIN fahrer_rennen WHERE t.lauf = 2) as b USING(fahrer_id, rennen_id, kategorie_id, boot_id) JOIN "
				+ "fahrer as f USING(fahrer_id) JOIN club as c USING(club_id) " + where + ";";
		List<FahrerResultat> resultat = new ArrayList<FahrerResultat>();
		for (Row row : executeSelect(selectStmt)) {
			double zeit1 = row.getRow().get(0).getKey() == null ? (double) 0 : (double) row.getRow().get(0).getKey();
			double zeit2 = row.getRow().get(1).getKey() == null ? (double) 0 : (double) row.getRow().get(1).getKey();
			Integer idFahrer = (Integer) row.getRow().get(2).getKey();
			Fahrer f = ladeFahrer(idFahrer);
			Integer rennenID = (Integer) row.getRow().get(3).getKey();
			Rennen r = ladeRennen(rennenID);
			Integer kategorieID = (Integer) row.getRow().get(4).getKey();
			AltersKategorie kat = ladeKategorie(kategorieID);

			Integer bootID = (Integer) row.getRow().get(5).getKey();
			String vorname = (String) row.getRow().get(6).getKey();
			String name = (String) row.getRow().get(7).getKey();
			String clubname = (String) row.getRow().get(8).getKey();
			Integer clubID = (Integer) row.getRow().get(9).getKey();

			// TODO: startnummer??
			resultat.add(new FahrerResultat(f, zeit1, zeit2, r, kat, null, null, null));
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

	private <T> String makeWhere(T[] column, T[] value) {
		String where = "WHERE ";
		for (int x = 0; x < column.length; x++) {
			if (value.length > x) {
				if (x > 0)
					where += " AND ";
				where += ((Table_Rangliste) column[x]).getValue();
				if (value[x] != null)
					where += " = " + value[x];
				else
					where += " IS NULL";
			}
		}
		return where;
	}

	private <T> String makeWhereRennen(T[] column, T[] value) {
		String where = "WHERE ";
		for (int x = 0; x < column.length; x++) {
			if (value.length > x) {
				if (x > 0)
					where += " AND ";
				where += ((Table_FahrerRennen) column[x]).getValue();
				if (value[x] != null)
					where += " = " + value[x];
				else
					where += " IS NULL";
			}
		}
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
			return null; // TODO abklären
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
	 * @param altersKategorie
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
	 * Funktion löscht einen Fahrer aus der Datenbank
	 * 
	 * @param fahrerID
	 * @return
	 */
	public boolean fahrerLoeschen(Integer fahrerID) {
		ExecuteResult res = executeUpdate("DELETE FROM fahrer WHERE fahrer_id = " + fahrerID + ";");
		return res.isSuccess();
	}

	/**
	 * Meldet einen Fahrer von einem Rennen ab (eine Kategorie und Bootsklasse)
	 * 
	 * @param fahrerID
	 *            Die Fahrer ID
	 * @param rennenID
	 *            Die Rennen ID
	 * @param alterskategorieID
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
			return null; // TODO abzuklären
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
		ExecuteResult res;
		if (fahrer.getFahrerID() < 1) {
			res = executeUpdate("INSERT INTO fahrer (club_id, name, vorname, jahrgang, telnr, strasse, plz, ort) "
					+ "VALUES (" + fahrer.getClub().getClubID() + ", '" + fahrer.getName() + "', '"
					+ fahrer.getVorname() + "', " + fahrer.getJahrgang() + ", '" + fahrer.getTelNr() + "', '"
					+ fahrer.getStrasse() + "', " + fahrer.getPlz() + ", '" + fahrer.getOrt() + "');");

			return res.isSuccess();

		} else {
			res = executeUpdate("UPDATE fahrer SET club_id = " + fahrer.getClub().getClubID() + ", name = '"
					+ fahrer.getName() + "', vorname = '" + fahrer.getVorname() + "', jahrgang = "
					+ fahrer.getJahrgang() + ", telnr = '" + fahrer.getTelNr() + "', strasse = '" + fahrer.getStrasse()
					+ "', plz = " + fahrer.getPlz() + ", ort = '" + fahrer.getOrt() + "' WHERE fahrer_id = "
					+ fahrer.getFahrerID() + ";");
		}
		return res.isSuccess();
	}

	public Integer speichereNeuenFahrer(Fahrer fahrer) {
		ExecuteResult res;
		res = executeUpdate("INSERT INTO fahrer (club_id, name, vorname, jahrgang, telnr, strasse, plz, ort) "
				+ "VALUES (" + fahrer.getClub().getClubID() + ", '" + fahrer.getName() + "', '" + fahrer.getVorname()
				+ "', " + fahrer.getJahrgang() + ", '" + fahrer.getTelNr() + "', '" + fahrer.getStrasse() + "', "
				+ fahrer.getPlz() + ", '" + fahrer.getOrt() + "');");

		return (Integer) res.getGeneratedIDs().get(0);
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
			return null; // TODO abzuklären
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
			return null; // TODO abzuklären
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
	public List<Club> ladeAngemeldeteClubs() { // TODO löschen, nicht brauchbar
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
	public List<FahrerResultat> ladeFahreranmeldungslisteClub(int clubID) {
		ArrayList<FahrerResultat> fahrer = new ArrayList<FahrerResultat>();
		fahrer.addAll(selectStartlisteBy(new Table_FahrerRennen[] { Table_FahrerRennen.COLUMN_CLUB_ID },
				new Integer[] { clubID }));
		// Nur Fahrer hinzufügen, welche noch nicht vorhanden sind
		/*
		 * for (Fahrer f : selectFahrerBy(Table_Fahrer.COLUMN_CLUB_ID, clubID))
		 * { boolean vorhanden = false; for (FahrerResultat x : fahrer) { if
		 * (x.getFahrer().getFahrerID() == f.getFahrerID()) { vorhanden = true;
		 * break; } } if (!vorhanden) fahrer.add(f); }
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
	public List<FahrerResultat> ladeFehlererfassung(Integer rennenID) {
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
	public List<FahrerResultat> ladeFehlererfassung(Integer rennenID, Integer kategorieID, Integer bootID) {
		return selectStartlisteBy(new Table_FahrerRennen[] { Table_FahrerRennen.COLUMN_RENNEN_ID,
				Table_FahrerRennen.COLUMN_KATEGORIE, Table_FahrerRennen.COLUMN_BOOTKLASSE },
				new Integer[] { rennenID, kategorieID, bootID });
	}

	/**
	 * Liest zu einer bestimmten Kategorie von einem Rennen die Startliste aus
	 * 
	 * @param rennenID
	 *            Die ID des Rennens
	 * @param kategorieID
	 *            Die ID der Kategorie
	 * 
	 * @return Eine Liste von Fahrern
	 */
	public List<FahrerResultat> ladeStartliste(Integer rennenID, Integer kategorieID) {
		return selectStartlisteBy(
				new Table_FahrerRennen[] { Table_FahrerRennen.COLUMN_RENNEN_ID, Table_FahrerRennen.COLUMN_KATEGORIE },
				new Integer[] { rennenID, kategorieID });
	}

	public List<FahrerResultat> ladeStartlisteMitSuche(Integer rennenID, Integer kategorieID, String suche) {
		String selectStmt = "SELECT fahrer_id, club_id, rennen_id, kategorie_id, startplatz, startzeit1, startzeit2, zeit1, zeit2 FROM fahrer_rennen JOIN fahrer USING(fahrer_id) JOIN club USING(club_id)"
				+ " WHERE (rennen_id = " + rennenID + " AND kategorie_id = " + kategorieID + ") AND (name LIKE '%"
				+ suche + "%'" + " OR vorname LIKE '%" + suche + "%'" + " OR jahrgang LIKE '%" + suche
				+ "%'  OR startplatz LIKE '%" + suche + "%');";
		List<FahrerResultat> fahrer = new ArrayList<FahrerResultat>();
		for (Row row : executeSelect(selectStmt)) {
			Integer idFahrer = (Integer) row.getRow().get(0).getKey();
			Fahrer f = ladeFahrer(idFahrer);
			Integer clubID = (Integer) row.getRow().get(1).getKey();
			Club club = ladeClubByID(clubID);
			Integer rID = (Integer) row.getRow().get(2).getKey();
			Rennen r = ladeRennen(rID);
			Integer katID = (Integer) row.getRow().get(3).getKey();
			AltersKategorie kat = ladeKategorie(katID);
			Integer startplatz = (Integer) row.getRow().get(4).getKey();
			// String startzeit1 = (String) row.getRow().get(5).getKey();
			// String startzeit2 = (String) row.getRow().get(6).getKey();
			// String laufzeit1 = (String) row.getRow().get(8).getKey();
			// String laufzeit2 = (String) row.getRow().get(9).getKey();
			// TODO: change 0.0d and null values
			fahrer.add(new FahrerResultat(f, 0.0d, 0.0d, r, kat, startplatz, null, null));
		}
		return fahrer;
	}

	/**
	 * Erfasst eine Strafzeit zu einem Fahrer und Rennen. Überschreibt eine
	 * bereits vorhandene Strafzeit zu einem Tor!
	 * 
	 * @param fahrerID
	 *            Die Fahrer ID
	 * @param rennenID
	 *            Die Rennen ID
	 * @param bootID
	 *            Die Bootskategorie ID
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
	public boolean fehlerErfassen(Integer fahrerID, Integer rennenID, Integer bootID, Integer kategorieID, Integer lauf,
			Integer tornummer, Integer strafzeit) {
		ExecuteResult res = executeUpdate(
				"INSERT INTO strafzeiten (fahrer_id, rennen_id, kategorie_id, boot_id, lauf, tor_nr, strafzeit) VALUES "
						+ "(" + fahrerID + ", " + rennenID + ", " + kategorieID + ", " + bootID + ", " + lauf + ", "
						+ tornummer + ", " + strafzeit + ") ON DUPLICATE KEY UPDATE strafzeit = " + strafzeit + ";");
		return res.isSuccess();
	}

	/**
	 * Löscht eine Strafzeit wieder
	 * 
	 * @param fahrerID
	 *            Die Fahrer ID
	 * @param rennenID
	 *            Die Rennen ID
	 * @param bootID
	 *            Die Bootskategorie ID
	 * @param kategorieID
	 *            Die ID der Kategorie
	 * @param lauf
	 *            1 für 1. Lauf, 2 für 2. Lauf
	 * @param tornummer
	 *            Die Tornummer
	 * @return true wenn gelöscht, false sonst
	 */
	public boolean fehlerLoeschen(Integer fahrerID, Integer rennenID, Integer bootID, Integer kategorieID, Integer lauf,
			Integer tornummer) {
		ExecuteResult res = executeUpdate("DELETE FROM strafzeiten WHERE fahrer_id = " + fahrerID + " AND"
				+ " rennen_id = " + rennenID + " AND tor_nr = " + tornummer + " AND kategorie_id = " + kategorieID
				+ " AND boot_id = " + bootID + " AND lauf = " + lauf + ";");
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

		String selectStmt = "SELECT fahrer_id, club_id, name, vorname, jahrgang, telnr, strasse, plz, ort FROM fahrer JOIN club USING(club_id) "
				+ "where name LIKE '%" + suche + "%'" + " OR vorname LIKE '%" + suche + "%'" + " OR jahrgang LIKE '%"
				+ suche + "%';";

		List<Fahrer> fahrer = new ArrayList<Fahrer>();
		for (Row row : executeSelect(selectStmt)) {
			Integer idFahrer = (Integer) row.getRow().get(0).getKey();
			Integer idClub = (Integer) row.getRow().get(1).getKey();
			Club club = ladeClubByID(idClub);
			String name = (String) row.getRow().get(2).getKey();
			String vorname = (String) row.getRow().get(3).getKey();
			Integer jg = (Integer) row.getRow().get(4).getKey();
			String telnr = (String) row.getRow().get(5).getKey();
			String strasse = (String) row.getRow().get(6).getKey();
			Integer plz = (Integer) row.getRow().get(7).getKey();
			String ort = (String) row.getRow().get(8).getKey();
			fahrer.add(new Fahrer(idFahrer, club, name, vorname, jg, telnr, strasse, plz, ort));
		}
		return fahrer;
	}

	/**
	 * fahrerlisteClub() benutzen!
	 * 
	 * @param clubID
	 * @return
	 */
	@Deprecated
	public List<Fahrer> ladeFahrermutationslisteClub(Integer clubID) { // TODO
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
			return null; // TODO abzuklären
	}

	/**
	 * Liest eine Kategorie aus der Datenbank
	 * 
	 * @param kategorieID
	 *            Die ID der kategorie
	 * @return Die Kategorie
	 */
	public AltersKategorie ladeKategorie(Integer kategorieID) {
		List<AltersKategorie> kategorien = selectKategorieBy(Table_Kategorien.COLUMN_ID, kategorieID);
		if (kategorien.size() > 0)
			return kategorien.get(0);
		else
			return null; // TODO abzuklären
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
		if (rennen.getRennenID() < 1) {
			res = executeUpdate("INSERT INTO rennen (name, datum, zeit, ort, anz_tore, anz_posten) VALUES " + "('"
					+ rennen.getName() + "', FROM_UNIXTIME(" + rennen.getDatum().getTime() / 1000 + "), FROM_UNIXTIME("
					+ rennen.getDatum().getTime() / 1000 + "), " + "'" + rennen.getOrt() + "', " + rennen.getAnzTore()
					+ ", " + rennen.getAnzPosten() + ");");
			if (res.isSuccess())
				rennen.setRennenID(res.generatedIDs.get(0)); // TODO Zeit vom
																// Rennen
																// richtig
																// machen!
		} else {
			res = executeUpdate("UPDATE rennen SET name = '" + rennen.getName() + "', datum = FROM_UNIXTIME("
					+ rennen.getDatum().getTime() / 1000 + "), " + "zeit = FROM_UNIXTIME("
					+ rennen.getDatum().getTime() / 1000 + "), ort = '" + rennen.getOrt() + "', anz_tore = "
					+ rennen.getAnzTore() + ", " + "anz_posten = " + rennen.getAnzPosten() + " WHERE rennen_id = "
					+ rennen.getRennenID() + ";");
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
	 * Gibt die Rangliste einer Bootsklasse zu einem Rennen zurück
	 * 
	 * @param rennen
	 *            Das Objekt des Rennens
	 * @param bootsKlasseID
	 *            Die ID der Bootsklasse
	 * @return Ein Ranglistenobjekt, das die FahrerResultate zum gegebenen
	 *         Rennen beinhaltet
	 */
	public Rangliste ladeRanglisteBootsKlasseID(Rennen rennen, Integer bootsKlasseID) {
		List<FahrerResultat> resultat = selectRanglisteBy(
				new Table_Rangliste[] { Table_Rangliste.COLUMN_RENNEN_ID, Table_Rangliste.COLUMN_BOOTKLASSE },
				new Integer[] { rennen.getRennenID(), bootsKlasseID });
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
	 * Gibt die Rangliste einer Kategorie und Bootsklasse zu einem Rennen zurück
	 * 
	 * @param rennen
	 *            Das Objekt des Rennens
	 * @param altersKategorieID
	 *            Die ID der Kategorie
	 * @param bootID
	 *            Die ID der Bootsklasse
	 * @return Ein Ranglistenobjekt, das die FahrerResultate zum gegebenen
	 *         Rennen beinhaltet
	 */
	public Rangliste ladeRanglisteAltersKategorie(Rennen rennen, Integer altersKategorieID, Integer bootID) {
		List<FahrerResultat> resultat = selectRanglisteBy(
				new Table_Rangliste[] { Table_Rangliste.COLUMN_RENNEN_ID, Table_Rangliste.COLUMN_KATEGORIE,
						Table_Rangliste.COLUMN_BOOTKLASSE },
				new Integer[] { rennen.getRennenID(), altersKategorieID, bootID });
		Rangliste rl = new Rangliste();
		rl.setRennen(rennen);
		rl.setResultate(resultat);
		return rl;
	}

	public boolean setzeNeueKategorie(Integer fahrerID, Integer alteKategorieID, Integer neueKategorieID,
			Integer rennenID) {
		boolean success = true;
		success = fahrerAbmelden(fahrerID, rennenID, alteKategorieID);
		success = fahrerAnmelden(fahrerID, rennenID, neueKategorieID);
		return success;
	}

}