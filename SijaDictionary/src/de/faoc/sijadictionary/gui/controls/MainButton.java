package de.faoc.sijadictionary.gui.controls;

import de.faoc.sijadictionary.gui.GuiApplicationController;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainButton extends Button {
	
	private static final String DICT_IMAGE_PATH = "/images/book.png";
	private static final String TRAINER_IMAGE_PATH = "/images/checklist.png";
	private static final String DICT_STRING = "Dictionary";
	private static final String TRAINER_STRING = "Trainer";
	
	private Image image;
	private ImageView imageView;
	private String text;

	public MainButton(Image image, String text) {
		super();
		this.image = image;
		this.imageView = new ImageView(image);
		this.text = text;
		
		init();
	}

	public MainButton(String imageResPath, String text) {
		this(new Image(MainButton.class.getResourceAsStream(imageResPath)), text);
	}
	
	private void init() {
		getStyleClass().add("main-button");
		
		imageView.setPreserveRatio(true);
		imageView.setSmooth(true);
		imageView.setCache(true);
		imageView.setFitWidth(300);
		imageView.setFitHeight(300);
		
		setGraphic(imageView);
		setText(text);
		
		setContentDisplay(ContentDisplay.TOP);
		
		setMinSize(400, 400);
		setPrefSize(400, 400);
		setMaxSize(400, 400);
	}
	
	public static MainButton getDictButton() {
		return new MainButton(DICT_IMAGE_PATH, DICT_STRING);
	}
	public static MainButton getTrainerButton() {
		return new MainButton(TRAINER_IMAGE_PATH, TRAINER_STRING);
	}
}
