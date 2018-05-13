package de.faoc.sijadictionary.core.database;

import de.faoc.sijadictionary.core.database.DatabaseTables.Unit;

public class DatabaseStatements {

	public static class Query {
		public static String unit() {
			return String.format("SELECT * FROM %s", DatabaseTables.Unit.TABLE_NAME);
		}
	}
	
	public static class Insert {
		public static String unit(String fromLang, String toLang, String name) {
			return String.format("INSERT INTO %s (%s, %s, %s) VALUES ('%s', '%s', '%s');", 
					Unit.TABLE_NAME, Unit.FROM_LANG, Unit.TO_LANG, Unit.NAME,
					fromLang, toLang, name);
		}
		public static String unit() {
			return String.format("INSERT INTO ", DatabaseTables.Unit.TABLE_NAME);
		}
	}

}
