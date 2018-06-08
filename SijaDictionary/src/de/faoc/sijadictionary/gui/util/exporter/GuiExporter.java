package de.faoc.sijadictionary.gui.util.exporter;

import java.io.File;

import de.faoc.sijadictionary.core.persistence.Exporter;

public interface GuiExporter extends Exporter {
	
	public boolean exportToFile(int unitId);
	
	public File getFile();
	
}
