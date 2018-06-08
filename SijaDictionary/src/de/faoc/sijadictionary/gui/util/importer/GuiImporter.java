package de.faoc.sijadictionary.gui.util.importer;

import java.io.File;

import de.faoc.sijadictionary.core.persistence.Importer;

public interface GuiImporter extends Importer {
	
	public File getFile();
	
	public boolean importFromFile(int unitId);
	
}
