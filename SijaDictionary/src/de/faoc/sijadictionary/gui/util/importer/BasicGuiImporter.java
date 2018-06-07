package de.faoc.sijadictionary.gui.util.importer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.faoc.sijadictionary.core.database.DatabaseHelper;
import de.faoc.sijadictionary.core.database.DatabaseStatements;
import de.faoc.sijadictionary.core.persistence.Translation;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public abstract class BasicGuiImporter implements GuiImporter {

	Window window;

	private FileChooser fileChooser;

	public BasicGuiImporter(Window window) {
		this.window = window;

		fileChooser = new FileChooser();
		fileChooser.setTitle("Choose Image");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	}

	@Override
	public boolean importFromFile(int unitId, File file) {
		List<Translation> translations = getFromFile(file);
		if (translations == null)
			return false;

		for (Translation translation : translations) {
			int id = DatabaseHelper.executeInsertSingle(DatabaseStatements.Insert.translation(unitId,
					translation.getOrigin(), translation.getTranslation()));
			if (id == -1)
				return false;
		}

		return true;
	}
	
	public boolean importFromFile(int unitId) {
		return importFromFile(unitId, getFile());
	}

	@Override
	public List<Translation> getFromFile(File file) {
		if (file == null)
			return null;

		List<Translation> translations = parseFile(file);

		return translations;
	}

	protected abstract List<Translation> parseFile(File file);

	@Override
	public File getFile() {
		File file = fileChooser.showOpenDialog(window);
		return file;
	}

}
