package de.faoc.sijadictionary;
	
import de.faoc.sijadictionary.core.database.DatabaseHelper;
import de.faoc.sijadictionary.gui.GuiApplication;
import javafx.application.Application;

public class Main {
	public static void main(String[] args) {
		DatabaseHelper.initDatabase();
		Application.launch(GuiApplication.class, args);
	}
}
