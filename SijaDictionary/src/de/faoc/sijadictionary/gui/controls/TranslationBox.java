package de.faoc.sijadictionary.gui.controls;

import de.faoc.sijadictionary.core.database.DatabaseHelper;
import de.faoc.sijadictionary.core.database.DatabaseStatements;
import de.faoc.sijadictionary.gui.displays.VocabDisplay;
import javafx.application.Platform;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TranslationBox extends StackPane {

	private VocabDisplay vocabDisplay;

	private int translationId;
	private String fromOrigin;
	private String toTranslation;

	private TextField fromTextField;
	private TextField toTextField;
	private Button deleteButton;
	private TranslationImageStack imageStack;

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

		initMainBox();
		initTopBox();

		getChildren().addAll(mainBox, deleteButton);
	}

	private void initMainBox() {
		mainBox.setAlignment(Pos.CENTER_LEFT);

		fromTextField = new TextField(fromOrigin);
		fromTextField.getStyleClass().add("translation-text");
		fromTextField.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(fromTextField, Priority.ALWAYS);
		fromTextField.setOnAction(event -> {
			if (fromTextField.getText() != fromOrigin) {
				updateTranslation();
			}
			toTextField.requestFocus();
		});
		fromTextField.focusedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (!newValue && fromTextField.getText() != fromOrigin)
				updateTranslation();
			else if (newValue) {
				Platform.runLater(() -> fromTextField.selectAll());
			}
		});

		toTextField = new TextField(toTranslation);
		toTextField.getStyleClass().add("translation-text");
		toTextField.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(toTextField, Priority.ALWAYS);
		toTextField.setOnAction(event -> {
			if (toTextField.getText() != toTranslation) {
				updateTranslation();
			}
			clearFocus();
		});
		toTextField.focusedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (!newValue && toTextField.getText() != toTranslation)
				updateTranslation();
			else if (newValue) {
				Platform.runLater(() -> toTextField.selectAll());
			}
		});

		imageStack = new TranslationImageStack(translationId);

		Label seperator = new Label("\u2014");
		seperator.getStyleClass().addAll("seperator");

		mainBox.getChildren().addAll(imageStack, fromTextField, seperator, toTextField);

	}

	private void initTopBox() {
		deleteButton = Icons.getIconButton(Icons.DELETE_IMAGE_PATH, 0);
		deleteButton.getStyleClass().addAll("translation-delete-button");
		deleteButton.setOnAction(event -> {
			delete();
		});

		StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);

	}

	private void delete() {
		DatabaseHelper.executeUpdate(DatabaseStatements.Delete.translation(translationId));
		vocabDisplay.reload();
	}

	private void updateTranslation() {
		DatabaseHelper.executeUpdate(
				DatabaseStatements.Update.translation(translationId, fromTextField.getText(), toTextField.getText()));
		//vocabDisplay.reload();
	}
	
	private void clearFocus() {
		this.requestFocus();
	}
	
	public void editTranslations() {
		fromTextField.requestFocus();
	}

}
