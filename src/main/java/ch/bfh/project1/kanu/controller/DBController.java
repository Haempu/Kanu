package ch.bfh.project1.kanu.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.project1.kanu.model.Benutzer;
import ch.bfh.project1.kanu.model.Club;
import ch.bfh.project1.kanu.model.Fahrer;
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
	}		public enum Table_Rennen {		COLUMN_ID("rennen_id"), COLUMN_NAME("name"), COLUMN_datum("datum"), COLUMN_ORT("ort"), CLOUMN_ALL("*");		private final String column;		Table_Rennen(String column) {			this.column = column;		}		public String getValue() {			return column;		}	}
	
	public enum Table_Fahrer {
		COLUMN_ID("fahrer_id"), COLUMN_CLUB_ID("club_id"), COLUMN_NAME("name"), 
		COLUMN_VORNAME("vorname"), COLUMN_JAHRGANG("jahrgang"), COLUMN_TELNR("telnnr"),
		COLUMN_PLZ("plz"), COLUMN_ORT("ort"), CLOUMN_ALL("*");

		private final String column;

		Table_Fahrer(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}
	
	public enum Table_Benutzer {
		COLUMN_ID("user_id"), COLUMN_BENUTZERNAME("benutzername"), COLUMN_EMAIL("email"), 
		COLUMN_NAME("name"), COLUMN_VORNAME("vorname"), COLUMN_CLUB_ID("club_id"),
		COLUMN_RECHTE("rechte"), CLOUMN_ALL("*");

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
		/**	 * Liest die Clubs aus der Datenbank aus	 * @param column Spalte, in der gesucht werden soll	 * @param value Wert, nach dem gesucht werden soll	 * @return Liste der Clubs, die die Suchkriterien einhalten	 */
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
	 * @param column Spalte, in der gesucht werden soll
	 * @param value Wert, nach dem gesucht werden soll
	 * @return Liste der Fahrer, die die Suchkriterien erfüllen
	 */
	public <T> List<Fahrer> selectFahrerBy(Table_Fahrer column, T value) {
		String selectStmt = "SELECT * FROM fahrer JOIN club USING(club_id) WHERE " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Club.CLOUMN_ALL))
			selectStmt = "SELECT * FROM fahrer JOIN club USING(club_id)";
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
			fahrer.add(new Fahrer(idFahrer, new Club(idClub, kennung, club_name), name, vorname, jg, telnr, strasse, plz, ort));
		}
		return fahrer;
	}
	//TODO Startliste Tabelle in DB
	public <T> List<Benutzer> selectBenutzerBy(Table_Benutzer column, T value) {
		String selectStmt = "SELECT * FROM user JOIN club USING(club_id) WHERE " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Club.CLOUMN_ALL))
			selectStmt = "SELECT * FROM user JOIN club USING(club_id)";
		List<Benutzer> benutzer = new ArrayList<Benutzer>();
		for (Row row : executeSelect(selectStmt)) {
			Integer idClub = (Integer) row.getRow().get(0).getKey();
			Integer idBenutzer = (Integer) row.getRow().get(1).getKey();
			String benutzername = (String) row.getRow().get(2).getKey();
			String passwort = (String) row.getRow().get(3).getKey();
			String name = (String) row.getRow().get(4).getKey();
			String vorname = (String) row.getRow().get(5).getKey();
			Integer rechte = (Integer) row.getRow().get(6).getKey();
			String kennung = (String) row.getRow().get(7).getKey();
			String club_name = (String) row.getRow().get(8).getKey();
			benutzer.add(new Benutzer()); //TODO
		}
		return benutzer;
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
	 * @return Die Clubs als Liste
	 */
	public List<Club> ladeClubs()
	{
		return selectClubBy(Table_Club.CLOUMN_ALL, null);
	}
	
	/**
	 * Liest einen Club nach ID aus der Datenbank aus
	 * @param clubID Die ID des Clubs
	 * @return Das Club Objekt; null wenns keinen Club mit der ID gibt
	 */
	public Club ladeClubByID(Integer clubID)
	{
		List<Club> clubs = selectClubBy(Table_Club.COLUMN_ID, clubID);
		if(clubs.size() > 0)
			return clubs.get(0);
		else
			return null; //TODO abklären
	}
	
	/**
	 * Speichert einen Club in der Datenbank. Um einen neuen Club einzufügen, muss die ID kleiner als 1 sein, 
	 * ansonsten wird ein bestehender Club upgedatet (sofern vorhanden, sonst werden die Daten verworfen) 
	 * @param club Das Club Objekt
	 * @return true wenn erfolgreich, false sonst
	 */
	public boolean speichereClub(Club club)
	{
		ExecuteResult res;
		if(club.getClubID() < 1)
			res = executeUpdate("INSERT INTO club (kennung, name) VALUES ('" + club.getKennung() + "', '" + club.getName() + "');");
		else
			res = executeUpdate("UPDATE club SET kennung = '" + club.getKennung() + "', name = '" + club.getName() + "' WHERE club_id = " + club.getClubID() + ";");
		return res.isSuccess();
	}
	
	/**
	 * Führt ein SQL Query aus. Da SELECT querys etwas schwieriger sind als UPDATE und INSERT querys, werden nur letztgenannte unterstützt (respektive es wird schlicht nichts zurückgegeben).
	 * @param query Das SQL Query als solches, muss mit einem ";" abgeschlossen werden
	 * @return true wenn das Query erfolgreich ausgeführt wurde, false sonst
	 */
	public boolean runQuery(String query)
	{
		if(query.matches("(?i)DROP.*")) //DROP wird nicht erlaubt!
			return false;
		ExecuteResult res = executeUpdate(query);
		return res.isSuccess();
	}

	/**
	 * Liest alle Fahrer eines Clubs aus der Datenbank und gibt sie als Liste zurück
	 * @param clubID ID des Clubs
	 * @return Liste der Fahrer
	 */
	public List<Fahrer> fahrerlisteClub(Integer clubID) {
		return selectFahrerBy(Table_Fahrer.COLUMN_CLUB_ID, clubID);
	}

	/**
	 * Meldet einen Fahrer für ein Rennen in der gewünschten Alters- sowie Bootskategorie an.
	 * @param fahrerID Die Fahrer ID
	 * @param rennenID Die Rennen ID
	 * @param bootsKlasse Die Bootkslasse
	 * @param altersKategorie Die Alterskategorie
	 * @return true wenn erfolgreich, false sonst
	 */
	public boolean fahrerAnmelden(Integer fahrerID, Integer rennenID, Integer bootsKlasse, Integer altersKategorie) {
		ExecuteResult res = executeUpdate("INSERT INTO fahrer_rennen (fahrer_id, rennen_id, kategorie_id, boots_kategorie) VALUES ("
				+ fahrerID + ", " + rennenID + ", " + altersKategorie + ", " + bootsKlasse + ");");
		return res.isSuccess();
	}

	/**
	 * Meldet einen Fahrer von einem Rennen ab (alle Kategorien!)
	 * @param fahrerID Die Fahrer ID
	 * @param rennenID Die Rennen ID
	 * @return true wenn erfolgreich, false sonst
	 */
	public boolean fahrerAbmelden(Integer fahrerID, Integer rennenID) {
		ExecuteResult res = executeUpdate("DELETE FROM fahrer_rennen WHERE fahrer_id = " + fahrerID + " AND rennen_id = " + rennenID + ";");
		return res.isSuccess();
	}

	/**
	 * Liest zu einer gegebenen ID den Fahrer aus der Datenbank
	 * 
	 * @param fahrerID Die ID des gewünschten Fahrers
	 * @return Das Fahrer Objekt, null wenn Fahrer nicht vorhanden
	 */
	public Fahrer ladeFahrer(int fahrerID) {
		List<Fahrer> fahrer = selectFahrerBy(Table_Fahrer.COLUMN_ID, fahrerID);
		if(fahrer.size() > 0)
			return fahrer.get(0);
		else
			return null; //TODO abzuklären
	}

	/**
	 * Speichert einen Fahrer in der Datenbank. Um einen neuen Fahrer einzufügen, muss die ID kleiner als 1 sein, 
	 * ansonsten wird ein bestehender Fahrer upgedatet (sofern vorhanden, sonst werden die Daten verworfen)
	 * 
	 * @param fahrer Der Fahrer mit den Infos
	 * @return boolean true wenn erfolgreich, false sonst
	 */
	public boolean speichereFahrer(Fahrer fahrer) {
		ExecuteResult res;
		if(fahrer.getFahrerID() < 1)
		{
			res = executeUpdate("INSERT INTO fahrer (club_id, name, vorname, jahrgang, telnr, strasse, plz, ort) "
					+ "VALUES (" + fahrer.getClub().getClubID() + ", '" + fahrer.getName() + "', '" + fahrer.getVorname()
					+ "', " + fahrer.getJahrgang() + ", '" + fahrer.getTelNr() + "', '" + fahrer.getStrasse()
					+ "', " + fahrer.getPlz() + ", '" + fahrer.getOrt() + "');");
		}
		else
		{
			res = executeUpdate("UPDATE fahrer SET club_id = " + fahrer.getClub().getClubID() 
					+ ", name = '" + fahrer.getName() + "', vorname = '" + fahrer.getVorname()
					+ "', jahrgang = " + fahrer.getJahrgang() + ", telnr = '" + fahrer.getTelNr() 
					+ "', strasse = '" + fahrer.getStrasse() + "', plz = " + fahrer.getPlz() 
					+ ", ort = '" + fahrer.getOrt() + "' WHERE fahrer_id = " + fahrer.getFahrerID() + ";");
		}
		return res.isSuccess();
	}

	/**
	 * Lädt einen Benutzer aus der Datenbank
	 * @param benutzerID Die Benutzer ID
	 * @return Das Benutzer Objekt, null wenn Benutzer nicht vorhanden
	 */
	public Benutzer ladeBenutzer(int benutzerID) {
		List<Benutzer> benutzer = selectBenutzerBy(Table_Benutzer.COLUMN_ID, benutzerID);
		if(benutzer.size() > 0)
			return benutzer.get(0);
		else
			return null; //TODO abzuklären
	}

	/**
	 * Speichert einen Benutzer. Ist die ID kleiner als 1, wird ein neuer Benutzer angelegt, ansonsten ein bestehender
	 * geupdatet. Ist kein Benutzer mit der ID vorhanden, werden die Daten verworfen.
	 * @param benutzer Das Benutzer Objekt
	 * @return true wenn erfolg, false sonst
	 */
	public boolean speichereBenutzer(Benutzer benutzer) {
		ExecuteResult res;		if(benutzer.getBenutzerID() < 1)		{			res = executeUpdate("INSERT INTO user (passwort, email) "					+ "VALUES (" + benutzer.getPasswort() + "', '"					+ benutzer.getEmailAdresse() + "');");		}		else		{			res = executeUpdate("UPDATE user SET email = " + benutzer.getEmailAdresse() 					+ ", passwort = '" + benutzer.getPasswort()					+ " WHERE user_id = " + benutzer.getBenutzerID() + ";");		}		return res.isSuccess();
	}

	public List<String> ladeAngemeldeteClubs() {
		return new ArrayList<String>();
	}
}