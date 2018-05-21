package de.faoc.sijadictionary.gui.controls;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Circle;

public class TranslationImageButton extends Button {

	private double SQRT_2 = Math.sqrt(2);
	private static final String IMAGE_ENDING_REGEX = "^(.+?)\\.(gif|jpe?g|tiff|png)$";

	private int translationId;
	private boolean previewMode;

	private TranslationImageView imageView;

	private NumberBinding maxBinding;

	public TranslationImageButton(int translationId, boolean previewMode) {
		super();
		this.translationId = translationId;
		this.previewMode = previewMode;

		init();
	}

	private void init() {
		getStyleClass().addAll("translation-image-button", "round");

		imageView = new TranslationImageView(translationId);
		maxBinding = Bindings.max(widthProperty(), heightProperty());
		if (imageView.isPresent()) {
			// imageView.fitWidthProperty().bind(maxBinding);
			// imageView.fitHeightProperty().bind(maxBinding);

		} else {
			// imageView.fitWidthProperty().bind(widthProperty().divide(SQRT_2));
			// imageView.fitHeightProperty().bind(heightProperty().divide(SQRT_2));
		}

		maxBinding.addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
			fitImage();
		});

		setGraphic(imageView);

		Circle circle = new Circle();
		circle.radiusProperty().bind(widthProperty().divide(2));
		boundsInLocalProperty().addListener((ChangeListener<Bounds>) (observable, oldValue, newValue) -> {
			circle.setCenterX((newValue.getMaxX() - newValue.getMinX()) / 2);
			circle.setCenterY((newValue.getMaxY() - newValue.getMinY()) / 2);
		});
		setClip(circle);

		if (previewMode) {
			pseudoClassStateChanged(PseudoClass.getPseudoClass("preview-mode"), true);
		} else {
			initDragAndDrop();
		}
	}

	private void fitImage() {
		if (imageView.getImage().getWidth() > imageView.getImage().getHeight()) {
			imageView.setFitHeight(maxBinding.doubleValue());
			imageView.setFitWidth(-1);
		} else {
			imageView.setFitHeight(-1);
			imageView.setFitWidth(maxBinding.doubleValue());
		}
	}

	private void initDragAndDrop() {
		// Set Drop from File
		setOnDragOver(event -> {
			if (event.getGestureSource() != this && isValidDragData(event)) {
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			}
		});
		setOnDragEntered(event -> {
			if (event.getGestureSource() != this && isValidDragData(event)) {
				pseudoClassStateChanged(PseudoClass.getPseudoClass("drag"), true);
			}
			event.consume();
		});
		setOnDragExited(event -> {
			pseudoClassStateChanged(PseudoClass.getPseudoClass("drag"), false);
			event.consume();
		});
		setOnDragDropped(event -> {
			processDraggedImage(event);
		});
	}

	private void processDraggedImage(DragEvent event) {
		Image draggedImage = extractDraggedImage(event);
		if (draggedImage != null) {
			imageView.setImage(draggedImage);
			fitImage();
		}
	}

	private Image extractDraggedImage(DragEvent event) {
		Dragboard dragboard = event.getDragboard();
		List<File> draggedFiles = dragboard.getFiles();
		if (draggedFiles != null) {
			for (File draggedFile : dragboard.getFiles()) {
				// Try to parse file
				try {
					Image image = new Image(new BufferedInputStream(new FileInputStream(draggedFile)));
					if (!image.isError())
						return image;
				} catch (FileNotFoundException e) {
				}
			}
		}

		if (dragboard.getImage() != null && !dragboard.getImage().isError())
			dragboard.getImage();

		// Check if String has image file-ending
		String draggedText = dragboard.getString();
		if (draggedText != null && !draggedText.isEmpty()) {
			if (draggedText.matches(IMAGE_ENDING_REGEX)) {
				// Check if valid URL
				try {
					URL draggedURL = new URL(draggedText);
					return new Image(draggedURL.toString());
				} catch (MalformedURLException e) {
				}
				// Check if file
				File draggedFile = Paths.get(draggedText).toFile();
				if (draggedFile != null && !draggedFile.isDirectory() && draggedFile.exists())
					try {
						return new Image(new BufferedInputStream(new FileInputStream(draggedFile)));
					} catch (FileNotFoundException e) {
					}
			}
		}
		String draggedURLString = dragboard.getUrl();
		if (draggedURLString != null) {
			try {
				URL draggedURL = new URL(draggedURLString);
				if (draggedURLString.matches(IMAGE_ENDING_REGEX))
					return new Image(draggedURL.toString());
			} catch (MalformedURLException e) {
			}
		}

		return null;
	}

	private boolean isValidDragData(DragEvent event) {
		Dragboard dragboard = event.getDragboard();
		Set<DataFormat> dataFormats = dragboard.getContentTypes();
		for (DataFormat dataFormat : dataFormats) {
			// Check if files are valid
			if (dataFormat.equals(DataFormat.FILES)) {
				List<File> draggedFiles = dragboard.getFiles();
				if (draggedFiles != null) {
					for (File draggedFile : dragboard.getFiles()) {
						// Try to parse file
						try {
							Image image = new Image(new BufferedInputStream(new FileInputStream(draggedFile)));
							if (!image.isError())
								return true;
						} catch (FileNotFoundException e) {
						}
					}
				}
			}
			if (dataFormat.equals(DataFormat.IMAGE)) {
				if (dragboard.getImage() != null && !dragboard.getImage().isError())
					return true;
			}
			if (dataFormat.equals(DataFormat.PLAIN_TEXT)) {
				// Check if String has image file-ending
				String draggedText = dragboard.getString();
				if (draggedText != null && !draggedText.isEmpty()) {
					if (draggedText.matches(IMAGE_ENDING_REGEX)) {
						// Check if valid URL
						try {
							URL draggedURL = new URL(draggedText);
							return true;
						} catch (MalformedURLException e) {
						}
						// Check if file
						File draggedFile = Paths.get(draggedText).toFile();
						if (draggedFile != null && !draggedFile.isDirectory() && draggedFile.exists())
							return true;
					}
				}

			}
			if (dataFormat.equals(DataFormat.URL)) {
				String draggedURLString = dragboard.getUrl();
				if (draggedURLString != null) {
					try {
						URL draggedURL = new URL(draggedURLString);
						if (draggedURLString.matches(IMAGE_ENDING_REGEX))
							return true;
					} catch (MalformedURLException e) {
					}
				}
			}
		}
		return false;
	}

}
