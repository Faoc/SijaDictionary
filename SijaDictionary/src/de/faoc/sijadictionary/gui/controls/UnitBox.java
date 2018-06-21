package de.faoc.sijadictionary.gui.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.faoc.sijadictionary.core.database.DatabaseHelper;
import de.faoc.sijadictionary.core.database.DatabaseStatements;
import de.faoc.sijadictionary.gui.GuiApplicationController;
import de.faoc.sijadictionary.gui.displays.UnitDisplay;
import de.faoc.sijadictionary.gui.displays.VocabDisplay;
import de.faoc.sijadictionary.gui.util.exporter.CsvGuiExporter;
import de.faoc.sijadictionary.gui.util.exporter.GuiExporter;
import de.faoc.sijadictionary.gui.util.exporter.SimpleFormatGuiExporter;
import de.faoc.sijadictionary.gui.util.importer.CsvGuiImporter;
import de.faoc.sijadictionary.gui.util.importer.GuiImporter;
import de.faoc.sijadictionary.gui.util.importer.SimpleFormatGuiImporter;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class UnitBox extends HBox {

	private UnitDisplay unitDisplay;

	private int unitId;
	private String name;

	private TextField nameField;
	private Button editButton;
	private Button deleteButton;
	private Button importButton;
	private Button exportButton;

	private Popup exportPopup;

	private VBox exportPopupBox;

	private Popup importPopup;

	private VBox importPopupBox;

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

		// Export
		exportPopup = new Popup();
		exportPopup.setAutoHide(true);
		exportPopup.setAutoFix(true);

		exportButton = Icons.getIconButton(Icons.EXPORT_IMAGE_PATH, 4);
		exportButton.getStyleClass().addAll("export-button", "green-button");
		exportButton.setOnAction(event -> {
			exportUnit();
		});

		// Import
		importPopup = new Popup();
		importPopup.setAutoHide(true);
		importPopup.setAutoFix(true);

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

	private void addExportFormatButtons(VBox exportPopupBox) {
		List<GuiExporter> exporters = Arrays.asList(
				new SimpleFormatGuiExporter(getScene().getWindow()),
				new CsvGuiExporter(getScene().getWindow())
		);
		for (GuiExporter exporter : exporters) {
			Button button = new Button(exporter.formatName());
			button.getStyleClass().addAll("export-format-button", "white-button");
			button.setOnAction(event -> {
				exporter.exportToFile(unitId);
			});
			exportPopupBox.getChildren().add(button);
		}
	}

	private void importUnit() {
		if (importPopupBox == null) {
			importPopupBox = new VBox(5, new Label("Choose format:"));
			importPopupBox.getStyleClass().addAll("import-popup-box", "popup-box");
			addImportFormatButtons(importPopupBox);
			importPopup.getContent().addAll(importPopupBox);
		}
		importPopup.show(getScene().getWindow());
		Bounds boundsInScreen = importButton.localToScreen(importButton.getBoundsInLocal());
		importPopup.setX(boundsInScreen.getMinX());
		importPopup.setY(boundsInScreen.getMinY());
		importPopup.show(getScene().getWindow());
	}

	private void addImportFormatButtons(VBox importPopupBox) {
		List<GuiImporter> importers = Arrays.asList(
				new SimpleFormatGuiImporter(getScene().getWindow()),
				new CsvGuiImporter(getScene().getWindow())
		);
		for (GuiImporter importer : importers) {
			Button button = new Button(importer.formatName());
			button.getStyleClass().addAll("import-format-button", "white-button");
			button.setOnAction(event -> {
				importer.importFromFile(unitId);
			});
			importPopupBox.getChildren().add(button);
		}
	}

	private void exportUnit() {
		if (exportPopupBox == null) {
			exportPopupBox = new VBox(5, new Label("Choose format:"));
			exportPopupBox.getStyleClass().addAll("export-popup-box", "popup-box");
			addExportFormatButtons(exportPopupBox);
			exportPopup.getContent().addAll(exportPopupBox);
		}
		exportPopup.show(getScene().getWindow());
		Bounds boundsInScreen = exportButton.localToScreen(exportButton.getBoundsInLocal());
		exportPopup.setX(boundsInScreen.getMinX());
		exportPopup.setY(boundsInScreen.getMinY());
		exportPopup.show(getScene().getWindow());
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
