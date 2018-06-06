package de.faoc.sijadictionary.gui.controls;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.faoc.sijadictionary.core.database.DatabaseHelper;
import de.faoc.sijadictionary.core.database.DatabaseStatements;
import de.faoc.sijadictionary.core.persistence.Synonym;
import de.faoc.sijadictionary.gui.displays.VocabDisplay;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class TranslationBox extends StackPane {

	private VocabDisplay vocabDisplay;

	private int translationId;
	private String fromOrigin;
	private String toTranslation;
	private ObservableList<Synonym> fromSynonyms = FXCollections.observableArrayList();
	private ObservableList<Synonym> toSynonyms = FXCollections.observableArrayList();

	private TextField fromTextField;
	private TextField toTextField;
	private Button deleteButton;
	private TranslationImageStack imageStack;

	private HBox mainBox;

	private Label fromSynonymLabel;

	private Label toSynonymLabel;

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

		mainBox = new HBox();

		initMainBox();
		initTopBox();

		getChildren().addAll(mainBox, deleteButton);
	}

	private void initMainBox() {
		mainBox.setAlignment(Pos.CENTER_LEFT);

		// From origin
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
		
		fromSynonymLabel = new Label("+ Add Synoym");
		fromSynonymLabel.getStyleClass().addAll("synonym-label", "clickable");
		fromSynonyms.addListener((ListChangeListener<Synonym>) c -> {
			if(fromSynonyms.isEmpty()) fromSynonymLabel.setText("+ Add Synoym");
			else fromSynonymLabel.setText(String.join(", ", fromSynonyms.stream().map(synonym -> synonym.getName()).collect(Collectors.toList())));
		});
		
		VBox fromBox = new VBox(5, fromTextField, fromSynonymLabel);
		fromBox.setAlignment(Pos.CENTER_RIGHT);

		// To origin
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
		
		toSynonymLabel = new Label("+ Add Synoym");
		toSynonymLabel.getStyleClass().addAll("synonym-label", "clickable");
		toSynonymLabel.setAlignment(Pos.CENTER_LEFT);
		toSynonyms.addListener((ListChangeListener<Synonym>) c -> {
			if(toSynonyms.isEmpty()) toSynonymLabel.setText("+ Add Synoym");
			else toSynonymLabel.setText(String.join(", ", toSynonyms.stream().map(synonym -> synonym.getName()).collect(Collectors.toList())));
		});
		
		VBox toBox = new VBox(5, toTextField, toSynonymLabel);
		toBox.setAlignment(Pos.CENTER_LEFT);

		// Image
		imageStack = new TranslationImageStack(translationId);

		// Seperator
		Label seperator = new Label("\u2014");
		seperator.getStyleClass().addAll("seperator");
		

		mainBox.getChildren().addAll(imageStack, fromBox, seperator, toBox);

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
		if (DatabaseHelper.executeUpdate(DatabaseStatements.Delete.translation(translationId))) {
			vocabDisplay.removeBox(this);
		}

	}

	private void updateTranslation() {
		DatabaseHelper.executeUpdate(
				DatabaseStatements.Update.translation(translationId, fromTextField.getText(), toTextField.getText()));
	}

	private void clearFocus() {
		this.requestFocus();
	}

	public void editTranslations() {
		fromTextField.requestFocus();
	}

	public ObservableList<Synonym> getFromSynonyms() {
		return fromSynonyms;
	}

	public ObservableList<Synonym> getToSynonyms() {
		return toSynonyms;
	}

}
