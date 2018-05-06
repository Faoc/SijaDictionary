package de.faoc.sijadictionary.core.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

	public static final String DB_PREFIX = "jdbc:sqlite:";
	public static final String DB_PATH = "db/";
	public static final String DB_FILE = "sijadictionary.db";
	public static final String DB_URL = DB_PREFIX + DB_PATH + DB_FILE;
	
	private static final String CREATE_TABLES_RES = "sql/create_tables.sql";

	private static Connection connection;

	public static Connection getConnection() {
		if (connection == null) {
			return connection;
		} else {
			connect();
			return connection;
		}
	}

	private static boolean executeResStatement(String statement) {
		BufferedReader input = null;
		try {
			URL resourceUrl = DatabaseHelper.class.getResource(statement);
			String resourceString = resourceUrl.getFile();
			input = new BufferedReader(new FileReader(resourceString));

			String statementString = "";
			String statementLine = null;
			while ((statementLine = input.readLine()) != null) {
				statementString += statementLine;
			}

			Statement stmt;
			stmt = getConnection().createStatement();
			stmt.execute(statementString);
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

	public static void initDatabase() {
		executeResStatement(CREATE_TABLES_RES);
	}

	private static void connect() {
		try {
			// create a connection to the database
			connection = DriverManager.getConnection(DB_URL);

			System.out.println("Connection to SQLite has been established.");

		} catch (SQLException e) {
			System.out.println("Connection to SQLite database failed.");
			System.out.println(e.getMessage());
		}
	}

}
