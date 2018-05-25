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

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
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
import javafx.util.Duration;

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

		updateImage();

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

	private void updateImage() {
		imageView = new TranslationImageView(translationId);
		maxBinding = Bindings.max(widthProperty(), heightProperty());
		if (imageView.isPresent()) {
			maxBinding.addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
				fitImage();
			});

		} else {
			imageView.fitWidthProperty().bind(widthProperty().divide(SQRT_2));
			imageView.fitHeightProperty().bind(heightProperty().divide(SQRT_2));
		}

		setGraphic(imageView);
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
		Image image = null;
		Dragboard dragboard = event.getDragboard();

		// Extract from file list
		List<File> draggedFiles = dragboard.getFiles();
		image = ImageProcessor.getFirstImageFromFileList(draggedFiles);
		if (image != null)
			return image;

		// Extract from image
		if (dragboard.getImage() != null && !dragboard.getImage().isError())
			dragboard.getImage();

		// Extract from text
		// Check if String has image file-ending
		String draggedText = dragboard.getString();
		if (draggedText != null && !draggedText.isEmpty()) {
			if (draggedText.matches(IMAGE_ENDING_REGEX)) {
				// Check if valid URL
				try {
					FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), this);
					fadeTransition.setFromValue(0.8);
					fadeTransition.setToValue(0.2);
					fadeTransition.setAutoReverse(true);
					fadeTransition.setCycleCount(Animation.INDEFINITE);
					fadeTransition.play();
					URL draggedURL = new URL(draggedText);
					Image urlImage = new Image(draggedURL.toString(), true);
					urlImage.progressProperty()
							.addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
								System.out.println(newValue.doubleValue());
								if(newValue.doubleValue() >= 1) {
									fadeTransition.stop();
									setOpacity(1);
								}
							});
					return urlImage;
				} catch (MalformedURLException e) {
				}
				// Check String is a file-path containing an image
				File draggedFile = Paths.get(draggedText).toFile();
				image = ImageProcessor.getImageFromFile(draggedFile);
				if (image != null)
					return image;
			}
		}
		String draggedURLString = dragboard.getUrl();
		if (draggedURLString != null) {

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
