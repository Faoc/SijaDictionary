package de.faoc.sijadictionary.gui.controls;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Icons {

	public static final String DICT_IMAGE_PATH = "/images/book.png";
	public static final String TRAINER_IMAGE_PATH = "/images/checklist.png";
	public static final String ADD_IMAGE_PATH = "/images/add.png";
	public static final String DELETE_IMAGE_PATH = "/images/delete.png";
	public static final String EDIT_IMAGE_PATH = "/images/edit.png";
	public static final String BACK_IMAGE_PATH = "/images/back.png";
	public static final String IMAGE_IMAGE_PATH = "/images/image.png";
	
	public static Image getImage(String path) {
		return new Image(Icons.class.getResourceAsStream(path));
	}

	public static ImageView get(String path, Integer fitWidth, Integer fitHeight) {
		Image image = new Image(Icons.class.getResourceAsStream(path));
		ImageView imageView = new ImageView(image);

		imageView.setPreserveRatio(true);
		imageView.setSmooth(true);

		if (fitHeight != null)
			imageView.setFitHeight(fitWidth);
		if (fitWidth != null)
			imageView.setFitWidth(fitHeight);

		return imageView;
	}

	public static ImageView get(String path) {
		return get(path, null, null);
	}

	public static ImageView get(String path, int size) {
		return get(path, size, size);
	}

	public static Button getIconButton(String path, int margin) {
		Button button = new Button();
		ImageView icon = Icons.get(path);

		icon.fitWidthProperty().bind(button.widthProperty().subtract(margin * 2));
		icon.fitHeightProperty().bind(button.heightProperty().subtract(margin * 2));

		button.setGraphic(icon);
		return button;
	}

}
