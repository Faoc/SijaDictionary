package de.faoc.sijadictionary.gui.controls;

import de.faoc.sijadictionary.core.database.DatabaseHelper;
import de.faoc.sijadictionary.core.database.DatabaseStatements;
import de.faoc.sijadictionary.gui.GuiApplicationController;
import de.faoc.sijadictionary.gui.displays.UnitDisplay;
import de.faoc.sijadictionary.gui.displays.VocabDisplay;
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

		getChildren().addAll(nameField, Space.hBoxSpace(), editButton, deleteButton);
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
