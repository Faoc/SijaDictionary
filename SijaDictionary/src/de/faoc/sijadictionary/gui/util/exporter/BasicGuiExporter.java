package de.faoc.sijadictionary.gui.util.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.faoc.sijadictionary.core.database.DataSet;
import de.faoc.sijadictionary.core.database.DatabaseHelper;
import de.faoc.sijadictionary.core.database.DatabaseStatements;
import de.faoc.sijadictionary.core.database.DatabaseTables;
import de.faoc.sijadictionary.core.persistence.Translation;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public abstract class BasicGuiExporter implements GuiExporter {

	Window window;

	private FileChooser fileChooser;

	public BasicGuiExporter(Window window) {
		super();
		this.window = window;

		fileChooser = new FileChooser();
		fileChooser.setTitle("Choose Image");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.setInitialFileName("export.sija");
	}

	@Override
	public boolean exportToFile(int unitId, File file) {
		DataSet queryResult = DatabaseHelper.query(DatabaseStatements.Query.translation(unitId));
		List<Translation> translations = new ArrayList<>();
		if (!queryResult.isEmpty()) {
			for (HashMap<String, Object> translation : queryResult) {
				int id = (int) translation.get(DatabaseTables.Translation.ID);
				String origin = (String) translation.get(DatabaseTables.Translation.ORIGIN);
				String toTranslation = (String) translation.get(DatabaseTables.Translation.TRANSLATION);
				translations.add(new Translation(id, unitId, origin, toTranslation, null));
			}
		}
		return exportToFile(translations, file);
	}
	
	@Override
	public boolean exportToFile(int unitId) {
		return exportToFile(unitId, getFile());
	}

	@Override
	public boolean exportToFile(List<Translation> translations, File file) {
		if (file == null || translations == null)
			return false;

		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs())
			return false;

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			for (Translation translation : translations) {
				String formattedTranslation = formatTranslation(translation);
				if (formattedTranslation == null)
					return false;
				writer.write(formattedTranslation);
				writer.newLine();
			}
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}

		return true;
	}

	protected abstract String formatTranslation(Translation translation);

	@Override
	public File getFile() {
		File file = fileChooser.showSaveDialog(window);
		return file;
	}

	public Window getWindow() {
		return window;
	}

	public FileChooser getFileChooser() {
		return fileChooser;
	}

}
