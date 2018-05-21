package de.faoc.sijadictionary.core.persistence;

public class Language {
	
	private int id;
	private String fullName;
	private String shortName;
	
	public Language(int id, String fullName, String shortName) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.shortName = shortName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
