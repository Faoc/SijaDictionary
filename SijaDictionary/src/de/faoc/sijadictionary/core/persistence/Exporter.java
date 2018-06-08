package de.faoc.sijadictionary.core.persistence;

import java.io.File;
import java.util.List;

public interface Exporter {
	
	public boolean exportToFile(int unitId, File file);
	
	public boolean exportToFile(List<Translation> translations, File file);
	
	public String formatName();

}
