package de.faoc.sijadictionary.core.persistence;

public class Translation {
	
	int id;
	int unit;
	String origin;
	String translation;
	String picture;
	
	public Translation(int id, int unit, String origin, String translation, String picture) {
		super();
		this.id = id;
		this.unit = unit;
		this.origin = origin;
		this.translation = translation;
		this.picture = picture;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}
