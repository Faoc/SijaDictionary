package de.faoc.sijadictionary.gui.controls;

import de.faoc.sijadictionary.core.database.DatabaseHelper;
import de.faoc.sijadictionary.core.database.DatabaseStatements;
import de.faoc.sijadictionary.gui.displays.VocabDisplay;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TranslationBox extends BorderPane {

	private VocabDisplay vocabDisplay;

	private int translationId;
	private String fromOrigin;
	private String toTranslation;

	private TextField fromTextField;
	private TextField toTextField;
	private Button deleteButton;
	private TranslationImageButton imageButton;

	private HBox topBox;
	private HBox mainBox;

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

		topBox = new HBox();
		mainBox = new HBox();

		setTop(topBox);
		setCenter(mainBox);

		initMainBox();
		initTopBox();

	}

	private void initMainBox() {
		mainBox.setAlignment(Pos.CENTER_LEFT);

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

		imageButton = new TranslationImageButton(translationId, false);

		Label seperator = new Label("\u2014");
		seperator.getStyleClass().addAll("seperator");

		mainBox.getChildren().addAll(Space.hBoxSpace(), fromTextField, Space.hBoxSpace(), seperator, Space.hBoxSpace(),
				toTextField, Space.hBoxSpace(), imageButton);

	}

	private void initTopBox() {
		deleteButton = Icons.getIconButton(Icons.DELETE_IMAGE_PATH, 0);
		deleteButton.getStyleClass().addAll("translation-delete-button");
		deleteButton.setOnAction(event -> {
			delete();
		});
		
		topBox.getChildren().addAll(Space.hBoxSpace(), deleteButton);

	}

	private void delete() {
		DatabaseHelper.executeUpdate(DatabaseStatements.Delete.translation(translationId));
		vocabDisplay.reload();
	}

	private void updateTranslation() {
		DatabaseHelper.executeUpdate(
				DatabaseStatements.Update.translation(translationId, fromTextField.getText(), toTextField.getText()));
		vocabDisplay.reload();
	}

}
