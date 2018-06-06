package de.faoc.sijadictionary.gui.displays;

import java.util.HashMap;

import de.faoc.sijadictionary.core.database.DataSet;
import de.faoc.sijadictionary.core.database.DatabaseHelper;
import de.faoc.sijadictionary.core.database.DatabaseStatements;
import de.faoc.sijadictionary.core.database.DatabaseTables;
import de.faoc.sijadictionary.gui.GuiApplicationController;
import de.faoc.sijadictionary.gui.controls.Icons;
import de.faoc.sijadictionary.gui.controls.TranslationBox;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class VocabDisplay extends Display<BorderPane> {

	private int unitId;
	private String unitName;

	private Button addButton;
	private Label title;
	private VBox center;
	private ScrollPane centerWrapper;
	private boolean addedNewBox = false;

	public VocabDisplay(int unitId, String unitName) {
		super();
		this.unitId = unitId;
		this.unitName = unitName;
		
		init();
	}

	@Override
	protected BorderPane defaultRoot() {
		return new BorderPane();
	}

	@Override
	protected String styleClass() {
		return "unit-display";
	}

	@Override
	protected void init() {
		title = new Label(unitName);
		title.getStyleClass().add("title");
		BorderPane.setAlignment(title, Pos.CENTER);
		BorderPane.setMargin(title, new Insets(10));

		addButton = Icons.getIconButton(Icons.ADD_IMAGE_PATH, 15);
		addButton.getStyleClass().addAll("add-vocab-button", "green-button");
		addButton.setOnMouseClicked(event -> {
			addTranslation();
		});
		BorderPane.setAlignment(addButton, Pos.CENTER);
		BorderPane.setMargin(addButton, new Insets(10));

		center = new VBox(10);
		center.setAlignment(Pos.TOP_CENTER);
		populate();
		BorderPane.setAlignment(center, Pos.TOP_CENTER);
		BorderPane.setMargin(center, new Insets(10));
		center.heightProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
			//If a new element was added scroll to bottom
			if(addedNewBox) {
				centerWrapper.setVvalue(1.0);
				addedNewBox = false;
			}
		});
		
		//Wrap the center into a Scrollpane
		centerWrapper = new ScrollPane(center);
		centerWrapper.setFitToHeight(true);
		centerWrapper.setFitToWidth(true);

		getRoot().setTop(title);
		getRoot().setCenter(centerWrapper);
		getRoot().setBottom(addButton);
	}

	private void addTranslation() {
		int id = DatabaseHelper.executeInsertSingle(DatabaseStatements.Insert.translation(unitId, "Origin", "Translation"));
		if(id != -1) {
			addedNewBox = true;
			TranslationBox newTranslationBox = addTranslationBox(id);
			if(newTranslationBox != null) {
				newTranslationBox.editTranslations();
			}
		}
	}

	public void reload() {
		VocabDisplay vocabDisplay = new VocabDisplay(unitId, unitName);
		vocabDisplay.setPreviousDisplay(getPreviousDisplay());
		GuiApplicationController.getInstance().changeDisplay(vocabDisplay);
	}

	private void populate() {
		DataSet translations = DatabaseHelper.query(DatabaseStatements.Query.translation(unitId));
		if (!translations.isEmpty()) {
			for (HashMap<String, Object> translation : translations) {
				int id = (int) translation.get(DatabaseTables.Translation.ID);
				String origin = (String) translation.get(DatabaseTables.Translation.ORIGIN);
				String toTranslation = (String) translation.get(DatabaseTables.Translation.TRANSLATION);
				addTranslationBox(id, origin, toTranslation);
			}
		}
	}
	
	private TranslationBox addTranslationBox(int id, String origin, String toTranslation) {
		TranslationBox translationBox = new TranslationBox(this, id, origin, toTranslation);
		center.getChildren().add(translationBox);
		return translationBox;
	}
	
	private TranslationBox addTranslationBox(int translationId) {
		//Get data from id
		DataSet translationData = DatabaseHelper.query(DatabaseStatements.Query.translationById(translationId));
		if(!translationData.isEmpty()) {
			HashMap<String, Object> translation = translationData.get(0);
			String origin = (String) translation.get(DatabaseTables.Translation.ORIGIN);
			String toTranslation = (String) translation.get(DatabaseTables.Translation.TRANSLATION);
			return addTranslationBox(translationId, origin, toTranslation);
		}
		return null;
	}

	public void removeBox(TranslationBox translationBox) {
		center.getChildren().remove(translationBox);
	}

}
