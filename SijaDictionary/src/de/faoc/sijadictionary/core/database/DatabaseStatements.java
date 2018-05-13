package de.faoc.sijadictionary.core.database;

import de.faoc.sijadictionary.core.database.DatabaseTables.Translation;
import de.faoc.sijadictionary.core.database.DatabaseTables.Unit;

public class DatabaseStatements {

	public static class Query {
		public static String unit() {
			return String.format("SELECT * FROM %s;", DatabaseTables.Unit.TABLE_NAME);
		}

		public static String unit(String fromLang, String toLang) {
			return String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s';", Unit.TABLE_NAME, Unit.FROM_LANG,
					fromLang, Unit.TO_LANG, toLang);
		}

		public static String translation(int unitId) {
			return String.format("SELECT * FROM %s WHERE %s = %d;", Translation.TABLE_NAME, Translation.UNIT, unitId);
		}
	}

	public static class Insert {
		public static String unit(String fromLang, String toLang, String name) {
			return String.format("INSERT INTO %s (%s, %s, %s) VALUES ('%s', '%s', '%s');", Unit.TABLE_NAME,
					Unit.FROM_LANG, Unit.TO_LANG, Unit.NAME, fromLang, toLang, name);
		}

		/*
		 * UNFINISHED
		 */
		public static String unit() {
			return String.format("INSERT INTO ", DatabaseTables.Unit.TABLE_NAME);
		}

		public static String translation(int unitId, String origin, String translation) {
			return String.format("INSERT INTO %s (%s, %s, %s) VALUES (%d, '%s', '%s');",
					Translation.TABLE_NAME, Translation.UNIT, Translation.ORIGIN, Translation.TRANSLATION,
					unitId, origin, translation);
		}
	}

	public static class Update {
		public static String unit(int id, String name) {
			return String.format("UPDATE %s SET %s = '%s' WHERE %s = %d;", Unit.TABLE_NAME, Unit.NAME, name, Unit.ID,
					id);
		}

		public static String translation(int id, String fromOrigin, String toTranslation) {
			return String.format("UPDATE %s SET %s = '%s', %s = '%s' WHERE %s = %d;", Translation.TABLE_NAME,
					Translation.ORIGIN, fromOrigin, Translation.TRANSLATION, toTranslation, Translation.ID, id);
		}
	}

	public static class Delete {
		public static String unit(int id) {
			return String.format("DELETE FROM %s WHERE %s = %d;", Unit.TABLE_NAME, Unit.ID, id);
		}

		public static String translation(int id) {
			return String.format("DELETE FROM %s WHERE %s = %d;", Translation.TABLE_NAME, Translation.ID, id);
		}
	}

}
