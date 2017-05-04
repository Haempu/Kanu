package ch.bfh.project1.kanu.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.project1.kanu.model.Benutzer;
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

	private DBController() {
		dbHost = "mysql22.webland.ch"; // TODO User und Passwort richtig setzen
		db = "brave_res_tool";
		dbUser = "brave_res_tool";
		dbUserPassword = "SoED-purple1";

		try {
			connect();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO: log exceptions
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
			// TODO: log exceptions
			System.out.println("SQlException: " + e.getMessage());
		} finally {
			try {
				// Release resources
				if (updateStmt != null)
					updateStmt.close();
			} catch (SQLException e) {
				// TODO: log exceptions
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
			// TODO: log exceptions
		} finally {
			try {
				// Release resources
				if (selectStmt != null)
					selectStmt.close();
				if (result != null)
					result.close();
			} catch (SQLException e) {
				// TODO: log exceptions
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

	public List<String> ladeFahreranmeldungslisteClub(int clubID) {
		return new ArrayList();
	}

	public void fahrerAnmelden(Integer fahrerID, Integer RennenID, Integer bootsKlasseID, Integer alterskategorieID) {

	}

	public void fahrerAbmelden(Integer fahrerID, Integer RennenID, Integer bootsKlasseID, Integer alterskategorieID) {

	}

	public Fahrer ladeFahrer(Integer fahrerID) {
		return new Fahrer();
	}

	public void speichereFahrer(Fahrer fahrer) {

	}

	public Benutzer ladeBenutzer(Integer benutzerID){
		return new Benutzer();
	}
	/*
	public List<Benutzer> ladeBenutzer(Integer benutzerID) {
		String selectStmt = "SELECT * from Benutzer where benutzerID = " + benutzerID;
		List<Benutzer> benutzer = new ArrayList<Benutzer>();
		for (Row row : (executeSelect(selectStmt))) {
			Integer benutzerId = (Integer) row.getRow().get(1).getKey();
			String email = (String) row.getRow().get(2).getKey();
			String passwort = (String) row.getRow().get(3).getKey();
			BenutzerRolle benutzerRolle = (BenutzerRolle) row.getRow().get(4).getKey();
			benutzer.add(new Benutzer(benutzerId, email, passwort, benutzerRolle));
		}
		return benutzer;
	}*/

	public void speichereBenutzer(Benutzer benutzerID) {

	}

	public List<String> ladeAngemeldeteClubs() {
		return new ArrayList();
	}
	
	public List<String> ladeFehlererfassung(Integer rennenID){
		return new ArrayList<String>();
	}
	
	public void fehlerErfassen(Integer fahrerID, Integer rennenID, int tornummer){
		
	}
	
	public List<Fahrer> ladeFahrermutationslisteAlle(){
		return new ArrayList<Fahrer>();
	}
	
	public List<Fahrer> ladeFahrermutationslisteClub(Integer clubID){
		return new ArrayList<Fahrer>();
	}
	
	public FahrerResultat ladeFahrerresultat(Integer fahrerID){
		return new FahrerResultat();
	}
	
	public void speichereFahrerBearbeitenAlle(Fahrer fahrer, FahrerResultat fahrerResultat){
		
	}
	
	public void speichereFahrerBearbeitenClub(Fahrer fahrer){
		
	}
	
	public Rangliste ladeRanglisteRennen(Rennen rennen){
		return new Rangliste();
	}
	
	public Rangliste ladeRanglisteBootsKlasseID(Integer bootsKlasseID){
		return new Rangliste();
	}
	
	public Rangliste ladeRanglisteAltersKategorie(Integer altersKategorieID){
		return new Rangliste();
	}
	
	
	
}