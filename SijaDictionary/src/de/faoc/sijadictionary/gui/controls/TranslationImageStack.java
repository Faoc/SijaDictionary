package de.faoc.sijadictionary.gui.controls;

import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;

public class TranslationImageStack extends StackPane {

	private int translationId;

	private TranslationImageButton translationButton;
	private Button urlButton;
	private Button deleteButton;
	private Popup urlPopup;
	private TextField urlTextField;

	public TranslationImageStack(int translationId) {
		super();
		this.translationId = translationId;

		init();
	}

	private void init() {
		getStyleClass().addAll("translation-image-stack");

		translationButton = new TranslationImageButton(translationId, false);

		deleteButton = Icons.getIconButton(Icons.DELETE_IMAGE_PATH, 5);
		deleteButton.getStyleClass().addAll("translation-image-delete-button", "red-button", "round");
		StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);

		deleteButton.setOnAction(event -> {
			translationButton.deleteImage();
		});

		urlTextField = new TextField();
		urlTextField.setPromptText("Enter URL");
		urlTextField.setOnAction(event -> {
			if (!urlTextField.getText().isEmpty())
				translationButton.loadImageFromUrl(urlTextField.getText());
			urlPopup.hide();
		});

		Label urlLabel = new Label("Enter URL:");

		HBox popupBox = new HBox(5, urlLabel, urlTextField);
		popupBox.setAlignment(Pos.CENTER_LEFT);
		popupBox.getStyleClass().addAll("popup-box");

		urlPopup = new Popup();
		urlPopup.setAutoHide(true);
		urlPopup.setAutoFix(true);
		urlPopup.getContent().addAll(popupBox);

		urlButton = Icons.getIconButton(Icons.LINK_IMAGE_PATH, 2);
		urlButton.getStyleClass().addAll("translation-image-url-button", "blue-button", "round");
		StackPane.setAlignment(urlButton, Pos.TOP_LEFT);
		urlButton.setOnAction(event -> {
			showUrlPopup();
		});

		getChildren().addAll(translationButton, deleteButton, urlButton);
	}

	private void showUrlPopup() {
		urlTextField.clear();

		Bounds boundsInScreen = urlButton.localToScreen(urlButton.getBoundsInLocal());
		urlPopup.setX(boundsInScreen.getMinX());
		urlPopup.setY(boundsInScreen.getMinY());
		urlPopup.show(getScene().getWindow());

		urlTextField.selectHome();
	}

}
