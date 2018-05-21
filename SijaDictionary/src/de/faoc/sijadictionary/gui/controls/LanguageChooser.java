package de.faoc.sijadictionary.gui.controls;

import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LanguageChooser extends ComboBox<String> {

	public static final String[] LANGUAGES = { "de", "es", "fr", "it", "pl", "se", "uk" };
	public static final String DEFAULT_FROM_LANG = "de";
	public static final String DEFAULT_TO_LANG = "uk";

	private String[] disabledItems = {};

	public LanguageChooser() {
		super();

		init();
	}

	public LanguageChooser(String[] languages) {
		this();

		setItems(FXCollections.observableArrayList(languages));
	}

	private void init() {
		getStyleClass().addAll("lang-chooser", "clickable");
		updateCells();
		checkIfDisabled();
	}

	protected void checkIfDisabled() {
		if (Arrays.asList(disabledItems).contains(getValue())) {
			for (int i = 0; i < getItems().size(); i++) {
				String item = getItems().get(i);
				System.out.println(item);
				if (!Arrays.asList(disabledItems).contains(item)) {
					setValue(item);
					break;
				}
			}
		}
	}

	private void updateCells() {
		setCellFactory(listview -> new FlagCell(disabledItems));
		setButtonCell(new FlagCell(disabledItems));
	}

	public String[] getDisabledItems() {
		return disabledItems;
	}

	public void setDisabledItems(String[] disabledItems) {
		this.disabledItems = disabledItems;
		updateCells();
		checkIfDisabled();
	}

	static class FlagCell extends ListCell<String> {

		public static final int FLAG_WIDTH = 20;

		private String[] disabledItems = {};

		public FlagCell(String[] disabledItems) {
			super();
			this.disabledItems = disabledItems;
		}

		@Override
		protected void updateItem(String language, boolean empty) {
			super.updateItem(language, empty);
			if (language == null || empty) {
				setItem(null);
				setGraphic(null);
			} else {
				setText(language.toUpperCase());
				ImageView flag = getFlag(language);

				Label label = new Label(null, flag);
				label.setContentDisplay(ContentDisplay.LEFT);
				setGraphic(label);

				setDisable(Arrays.asList(disabledItems).contains(language));
			}
		}

		private ImageView getFlag(String language) {
			String imagePath = String.format("/images/flags/%s.png", language);
			Image image = new Image(FlagCell.class.getResourceAsStream(imagePath));
			ImageView imageView = new ImageView(image);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			imageView.setFitWidth(FLAG_WIDTH);

			return imageView;
		}

	}
}
