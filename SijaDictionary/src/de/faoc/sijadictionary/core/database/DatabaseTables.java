package de.faoc.sijadictionary.core.database;

public class DatabaseTables {
	
	public class Translation{
		public static final String TABLE_NAME = "translation";
		
		public static final String ID = "id";
		public static final String UNIT = "unit";
		public static final String ORIGIN = "origin";
		public static final String TRANSLATION = "translation";
	}
	
	public class Unit{
		public static final String TABLE_NAME = "unit";
		
		public static final String ID = "id";
		public static final String FROM_LANG = "fromLang";
		public static final String TO_LANG = "toLang";
		public static final String NAME = "name";
		public static final String RESULT = "result";
		public static final String TRIALS = "trials";
		public static final String DEF_UNIT = "defUnit";
	}
	
	public class Synonym{
		public static final String TABLE_NAME = "synonym";
		
		public static final String ID = "id";
		public static final String TRANSLATION = "translation";
		public static final String NAME = "name";
	}

}
