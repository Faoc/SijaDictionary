package de.faoc.sijadictionary.gui.displays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.faoc.sijadictionary.core.database.DataSet;
import de.faoc.sijadictionary.core.database.DatabaseHelper;
import de.faoc.sijadictionary.core.database.DatabaseStatements;
import de.faoc.sijadictionary.core.database.DatabaseTables;
import de.faoc.sijadictionary.gui.GuiApplicationController;
import de.faoc.sijadictionary.gui.controls.Icons;
import de.faoc.sijadictionary.gui.controls.TranslationBox;
import de.faoc.sijadictionary.gui.controls.UnitBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class VocabDisplay extends Display<BorderPane> {

	private int unitId;
	private String unitName;

	private Button addButton;
	private Label title;
	private VBox center;

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
		center.getChildren().addAll(populate());
		BorderPane.setAlignment(center, Pos.TOP_CENTER);
		BorderPane.setMargin(center, new Insets(10));

		getRoot().setTop(title);
		getRoot().setCenter(center);
		getRoot().setBottom(addButton);
	}

	private void addTranslation() {
		GuiApplicationController controller = GuiApplicationController.getInstance();
		DatabaseHelper.executeUpdate(DatabaseStatements.Insert.translation(unitId, "Origin", "Translation"));
		reload();
	}

	public void reload() {
		VocabDisplay vocabDisplay = new VocabDisplay(unitId, unitName);
		vocabDisplay.setPreviousDisplay(getPreviousDisplay());
		GuiApplicationController.getInstance().changeDisplay(vocabDisplay);
	}

	private List<Node> populate() {
		DataSet translations = DatabaseHelper.query(DatabaseStatements.Query.translation(unitId));
		if (translations.isEmpty()) {
			return Arrays.asList(new Label("No translations added"));
		} else {
			ArrayList<Node> translationBoxes = new ArrayList<>();
			for (HashMap<String, Object> translation : translations) {
				int id = (int) translation.get(DatabaseTables.Translation.ID);
				String origin = (String) translation.get(DatabaseTables.Translation.ORIGIN);
				String toTranslation = (String) translation.get(DatabaseTables.Translation.TRANSLATION);
				translationBoxes.add(new TranslationBox(this, id, origin, toTranslation));
			}
			return translationBoxes;
		}
	}

}
