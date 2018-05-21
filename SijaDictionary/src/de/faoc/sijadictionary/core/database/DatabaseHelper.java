package de.faoc.sijadictionary.core.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.faoc.sijadictionary.Main;
import de.faoc.sijadictionary.gui.controls.LanguageChooser;

public class DatabaseHelper {

	public static final String DB_PREFIX = "jdbc:sqlite:";
	public static final String DB_PATH = "db/";
	public static final String DB_FILE = "sijadictionary.db";
	public static final String DB_URL = DB_PREFIX + DB_PATH + DB_FILE;

	private static final String CREATE_TABLES_RES = "/sql/create_tables.sql";

	private static Connection connection;

	public static Connection getConnection() {
		if (connection != null) {
			return connection;
		} else {
			connect();
			return connection;
		}
	}

	private static void connect() {
		try {
			// init the subfolder
			Files.createDirectories(Paths.get(DB_PATH));
			// create a connection to the database
			connection = DriverManager.getConnection(DB_URL);

			System.out.println("Connection to SQLite has been established.");

		} catch (SQLException | IOException e) {
			System.out.println("Connection to SQLite database failed.");
			System.out.println(e.getMessage());
		}
	}

	private static boolean executeResStatement(String statement) {
		BufferedReader input = null;
		try {
			URL resourceUrl = DatabaseHelper.class.getResource(statement);
			String resourceString = resourceUrl.getFile();
			input = new BufferedReader(new FileReader(resourceString));

			String statementStrings = "";
			String statementLine = null;
			while ((statementLine = input.readLine()) != null) {
				statementStrings += statementLine;
			}
			
			// Split statements and execute
			for(String statementString : statementStrings.split(";")) {
				Statement stmt = getConnection().createStatement();
				stmt.executeUpdate(statementStrings);
				stmt.close();
			}
			return true;
		} catch (IOException e) {
			System.out.println("Error reading: " + statement);
			e.printStackTrace();
			return false;

		} catch (SQLException e) {
			System.out.println("Error executing res statement: " + statement);
			e.printStackTrace();
			return false;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean execute(String sql) {
		try {
			Statement stmt = getConnection().createStatement();
			stmt.execute(sql);
			return true;
		} catch (SQLException e) {
			System.out.println("Error executing statement: " + sql);
			e.printStackTrace();
			return false;
		}
	}

	public static boolean executeUpdate(String sql) {
		try {
			Statement stmt = getConnection().createStatement();
			return stmt.executeUpdate(sql) > 0;
		} catch (SQLException e) {
			System.out.println("Error executing statement: " + sql);
			e.printStackTrace();
			return false;
		}
	}

	public static DataSet query(String sql) {
		try {
			Statement stmt = getConnection().createStatement();
			return DataSet.fromResulSet(stmt.executeQuery(sql));
		} catch (SQLException e) {
			System.out.println("Error executing statement: " + sql);
			e.printStackTrace();
			return null;
		}
	}

	public static void initDatabase() {
		System.out.println("Initializing DB");

		executeResStatement(CREATE_TABLES_RES);
		System.out.println("Created tables");

		System.out.println("Done!");
	}

}
