package de.faoc.sijadictionary.gui.util.exporter;

import de.faoc.sijadictionary.core.persistence.Translation;
import javafx.stage.Window;

public class SimpleFormatGuiExporter extends BasicGuiExporter {

	public SimpleFormatGuiExporter(Window window) {
		super(window);
	}

	@Override
	public String formatName() {
		return "Simple Format";
	}

	@Override
	protected String formatTranslation(Translation translation) {
		if(translation == null) return null;
		
		return String.format("%s;;%s", translation.getOrigin(), translation.getTranslation());
	}

	@Override
	protected String getFileExtension() {
		return "sija";
	}

}
