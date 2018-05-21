package de.faoc.sijadictionary.gui.controls;


import de.faoc.sijadictionary.core.database.DatabaseHelper;
import de.faoc.sijadictionary.core.database.DatabaseStatements;
import de.faoc.sijadictionary.gui.displays.VocabDisplay;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class TranslationBox extends HBox {

	private VocabDisplay vocabDisplay;

	private int translationId;
	private String fromOrigin;
	private String toTranslation;

	private TextField fromTextField;
	private TextField toTextField;
	private Button deleteButton;

	public TranslationBox(VocabDisplay vocabDisplay, int translationId, String fromOrigin, String toTranslation) {
		super();
		this.vocabDisplay = vocabDisplay;
		this.translationId = translationId;
		this.fromOrigin = fromOrigin;
		this.toTranslation = toTranslation;

		init();
	}

	private void init() {
		getStyleClass().addAll("translation-box");
		setAlignment(Pos.CENTER_LEFT);

		fromTextField = new TextField(fromOrigin);
		fromTextField.getStyleClass().add("translation-text");
		fromTextField.setOnAction(event -> {
			if (fromTextField.getText() != fromOrigin) {
				updateTranslation();
			}
		});
		fromTextField.focusedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (!newValue && fromTextField.getText() != fromOrigin)
				updateTranslation();
		});

		toTextField = new TextField(toTranslation);
		toTextField.getStyleClass().add("translation-text");
		toTextField.setOnAction(event -> {
			if (toTextField.getText() != toTranslation) {
				updateTranslation();
			}
		});
		toTextField.focusedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (!newValue && toTextField.getText() != toTranslation)
				updateTranslation();
		});

		deleteButton = Icons.getIconButton(Icons.DELETE_IMAGE_PATH, 4);
		deleteButton.getStyleClass().addAll("unit-delete-button", "red-button");
		deleteButton.setOnAction(event -> {
			delete();
		});
		
		Label seperator = new Label("\u2014");
		seperator.getStyleClass().addAll("seperator");

		getChildren().addAll(Space.hBoxSpace(), fromTextField, Space.hBoxSpace(), seperator, Space.hBoxSpace(), toTextField, Space.hBoxSpace(), deleteButton);
	}

	private void delete() {
		DatabaseHelper.executeUpdate(DatabaseStatements.Delete.translation(translationId));
		vocabDisplay.reload();
	}

	private void updateTranslation() {
		DatabaseHelper.executeUpdate(DatabaseStatements.Update.translation(translationId, fromTextField.getText(), toTextField.getText()));
		vocabDisplay.reload();
	}

}
