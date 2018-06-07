package de.faoc.sijadictionary.gui.controls;

import de.faoc.sijadictionary.core.database.DatabaseHelper;
import de.faoc.sijadictionary.core.database.DatabaseStatements;
import de.faoc.sijadictionary.gui.GuiApplicationController;
import de.faoc.sijadictionary.gui.displays.UnitDisplay;
import de.faoc.sijadictionary.gui.displays.VocabDisplay;
import de.faoc.sijadictionary.gui.util.importer.SimpleFormatGuiImporter;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class UnitBox extends HBox {

	private UnitDisplay unitDisplay;

	private int unitId;
	private String name;

	private TextField nameField;
	private Button editButton;
	private Button deleteButton;
	private Button importButton;
	private Button exportButton;

	public UnitBox(int id, String name, UnitDisplay unitDisplay) {
		super();
		this.unitId = id;
		this.name = name;
		this.unitDisplay = unitDisplay;

		init();
	}

	private void init() {
		getStyleClass().addAll("unit-box", "clickable");
		setAlignment(Pos.CENTER_LEFT);
		
		this.setOnMouseClicked(event -> {
			VocabDisplay vocabDisplay = new VocabDisplay(unitId, name);
			vocabDisplay.setPreviousDisplay(unitDisplay);
			GuiApplicationController.getInstance().changeDisplay(vocabDisplay);
		});

		nameField = new TextField(name);
		nameField.getStyleClass().add("unit-name");
		nameField.setOnAction(event -> {
			if (nameField.getText() != name) {
				updateUnit();
			}
		});
		nameField.focusedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (!newValue && nameField.getText() != name)
				updateUnit();
		});
		
		exportButton = Icons.getIconButton(Icons.EXPORT_IMAGE_PATH, 4);
		exportButton.getStyleClass().addAll("export-button", "green-button");
		exportButton.setOnAction(event -> {
			exportUnit();
		});
		
		importButton = Icons.getIconButton(Icons.IMPORT_IMAGE_PATH, 4);
		importButton.getStyleClass().addAll("import-button", "green-button");
		importButton.setOnAction(event -> {
			importUnit();
		});

		editButton = Icons.getIconButton(Icons.EDIT_IMAGE_PATH, 4);
		editButton.getStyleClass().addAll("unit-edit-button", "blue-button");
		editButton.setOnAction(event -> {
			nameField.requestFocus();
			nameField.selectAll();
		});

		deleteButton = Icons.getIconButton(Icons.DELETE_IMAGE_PATH, 4);
		deleteButton.getStyleClass().addAll("unit-delete-button", "red-button");
		deleteButton.setOnAction(event -> {
			deleteUnit();
		});

		getChildren().addAll(nameField, Space.hBoxSpace(), exportButton, importButton, editButton, deleteButton);
	}

	private void importUnit() {
		SimpleFormatGuiImporter importer = new SimpleFormatGuiImporter(getScene().getWindow());
		importer.importFromFile(unitId);
	}

	private void exportUnit() {
		
	}

	private void deleteUnit() {
		DatabaseHelper.executeUpdate(DatabaseStatements.Delete.unit(unitId));
		unitDisplay.reload();
	}

	private void updateUnit() {
		DatabaseHelper.executeUpdate(DatabaseStatements.Update.unit(unitId, nameField.getText()));
		unitDisplay.reload();
	}

	public int getUnitId() {
		return unitId;
	}

	public String getName() {
		return name;
	}

}
