package de.faoc.sijadictionary.gui.controls;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class UnitBox extends HBox {
	
	private int unitId;
	private String name;
	
	private Label nameLabel;
	private Button editButton;
	private Button deleteButton;
	
	public UnitBox(int id, String name) {
		super();
		this.unitId = id;
		this.name = name;
		
		init();
	}

	private void init() {
		getStyleClass().addAll("unit-box", "clickable");
		setAlignment(Pos.CENTER_LEFT);
		
		nameLabel = new Label(name);
		nameLabel.getStyleClass().add("unit-name-label");
		
		editButton = Icons.getIconButton(Icons.EDIT_IMAGE_PATH, 4);
		editButton.getStyleClass().addAll("unit-edit-button", "blue-button");
		
		deleteButton = Icons.getIconButton(Icons.DELETE_IMAGE_PATH, 4);
		deleteButton.getStyleClass().addAll("unit-delete-button", "red-button");
		
		getChildren().addAll(nameLabel, Space.hBoxSpace(), editButton, deleteButton);
	}

	public int getUnitId() {
		return unitId;
	}

	public String getName() {
		return name;
	}

}
