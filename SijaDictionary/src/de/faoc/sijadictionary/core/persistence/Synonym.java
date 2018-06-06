package de.faoc.sijadictionary.core.persistence;

public class Synonym {
	
	private int id;
	private int translationId;
	private String name;
	
	public Synonym(int id, int translationId, String name) {
		super();
		this.id = id;
		this.translationId = translationId;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getTranslationId() {
		return translationId;
	}

	public void setTranslationId(int translationId) {
		this.translationId = translationId;
	}
}
