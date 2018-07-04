package de.faoc.sijadictionary.gui.controls;

import de.faoc.sijadictionary.gui.GuiApplication;
import de.faoc.sijadictionary.gui.GuiApplicationController;
import de.faoc.sijadictionary.gui.util.exporter.FullExporter;
import de.faoc.sijadictionary.gui.util.importer.FullImporter;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class HeaderBar extends HBox {

	private EventHandler<ActionEvent> backPressedListener;
	private EventHandler<ActionEvent> langChangedListener;

	private ReadOnlyStringWrapper fromLang;
	private ReadOnlyStringWrapper toLang;

	private Button backButton;
	private LanguageChooser fromLangChooser;
	private LanguageChooser toLangChooser;
	
	private Button exportButton;
	private Button importButton;

	public HeaderBar() {
		super();

		init();
	}

	private void init() {
		getStyleClass().addAll("header-box");
		setAlignment(Pos.CENTER_LEFT);

		backButton = Icons.getIconButton(Icons.BACK_IMAGE_PATH, 2);
		backButton.getStyleClass().addAll("back-button");
		backButton.setOnAction(event -> {
			if (backPressedListener != null)
				backPressedListener.handle(event);
		});

		fromLangChooser = new LanguageChooser(LanguageChooser.LANGUAGES);
		fromLangChooser.getStyleClass().addAll("from-lang-chooser");
		toLangChooser = new LanguageChooser(LanguageChooser.LANGUAGES);
		fromLangChooser.getStyleClass().addAll("to-lang-chooser");

		initProperties();
		
		fromLangChooser.valueProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
			toLangChooser.setDisabledItems(new String[]{newValue});
		});
		fromLangChooser.setValue(LanguageChooser.DEFAULT_FROM_LANG);
		toLangChooser.setValue(LanguageChooser.DEFAULT_TO_LANG);
		
		importButton = Icons.getIconButton(Icons.IMPORT_IMAGE_PATH, 4);
		importButton.getStyleClass().addAll("import-button", "green-button");
		importButton.setOnAction(event -> {
			importClicked();
		});
		
		exportButton = Icons.getIconButton(Icons.EXPORT_IMAGE_PATH, 4);
		exportButton.getStyleClass().addAll("export-button", "green-button");
		exportButton.setOnAction(event -> {
			exportClicked();
		});
		
		
		getChildren().addAll(backButton, Space.hBoxSpace(), importButton, exportButton, fromLangChooser, toLangChooser);
	}

	private void exportClicked() {
		FullExporter.exportData(getScene().getWindow());
	}

	private void importClicked() {
	    FullImporter.importData(getScene().getWindow());
	    GuiApplicationController.getInstance().setMainDisplay();
	}

	private void initProperties() {
		fromLang = new ReadOnlyStringWrapper();
		fromLang.bind(fromLangChooser.valueProperty());
		toLang = new ReadOnlyStringWrapper();
		toLang.bind(toLangChooser.valueProperty());

		HeaderBar headerBar = this;
		fromLang.addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
			ActionEvent event = new ActionEvent(headerBar, headerBar);
			if (langChangedListener != null)
				langChangedListener.handle(event);
		});
		toLang.addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
			ActionEvent event = new ActionEvent(headerBar, headerBar);
			if (langChangedListener != null)
				langChangedListener.handle(event);
		});
	}

	public ReadOnlyStringProperty fromLangProperty() {
		return fromLang.getReadOnlyProperty();
	}

	public ReadOnlyStringProperty toLangProperty() {
		return toLang.getReadOnlyProperty();
	}

	public EventHandler<ActionEvent> getBackPressedListener() {
		return backPressedListener;
	}

	public void setBackPressedListener(EventHandler<ActionEvent> backPressedListener) {
		this.backPressedListener = backPressedListener;
	}

	public EventHandler<ActionEvent> getLangChangedListener() {
		return langChangedListener;
	}

	public void setLangChangedListener(EventHandler<ActionEvent> langChangedListener) {
		this.langChangedListener = langChangedListener;
	}
}
