package de.faoc.sijadictionary.core.persistence;

import java.io.File;
import java.util.List;

public interface Importer {
	
	public boolean importFromFile(int unitId, File file);
	
	public List<Translation> getFromFile(File file);
	
	public String formatName();

}
