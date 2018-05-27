package de.faoc.sijadictionary.gui.controls;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import de.faoc.sijadictionary.gui.controls.URLImage.Status;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class TranslationImageButton extends Button {

	private static final double SQRT_2 = Math.sqrt(2);
	private static final String IMAGE_ROOT = "img/";
	private static final String IMAGE_SUFFIX = ".png";
	private static final String IMAGE_FORMAT = "png";
	private static final int IMAGE_SIZE = 400;
	private static final long IMAGE_LOADING_TIMEOUT = 7000;

	private int translationId;
	private boolean previewMode;

	private TranslationImageView imageView;

	private NumberBinding maxBinding;

	private final FileChooser fileChooser = new FileChooser();

	public TranslationImageButton(int translationId, boolean previewMode) {
		super();
		this.translationId = translationId;
		this.previewMode = previewMode;

		init();
	}

	private void init() {
		getStyleClass().addAll("translation-image-button", "round");

		fileChooser.setTitle("Choose Image");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
		setOnAction(event -> {
			openFile();
		});

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

	private void openFile() {
		File file = fileChooser.showOpenDialog(getScene().getWindow());
		if (file != null) {
			Image selectedImage = ImageProcessor.getImageFromFile(file);
			if (selectedImage != null) {
				saveTranslationImage(selectedImage);
				updateImage();
			}
		}
	}

	private void updateImage() {
		imageView = new TranslationImageView(translationId);
		if (maxBinding == null)
			maxBinding = Bindings.max(widthProperty(), heightProperty());
		if (imageView.isPresent()) {
			maxBinding.addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
				fitImage();
			});
			fitImage();
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
		Image image = null;
		Dragboard dragboard = event.getDragboard();

		// Extract from file list
		List<File> draggedFiles = dragboard.getFiles();
		image = ImageProcessor.getFirstImageFromFileList(draggedFiles);
		if (image != null) {
			saveTranslationImage(image);
			updateImage();
			return;
		}

		// Extract from image
		if (dragboard.getImage() != null && !dragboard.getImage().isError()) {
			saveTranslationImage(dragboard.getImage());
			updateImage();
			return;
		}

		// Extract from URL
		String draggedURLString = dragboard.getUrl();
		if (draggedURLString != null && ImageProcessor.isValidImageUrl(draggedURLString)) {
			loadImageFromUrl(draggedURLString);
			return;
		}

		// Extract from text
		String draggedText = dragboard.getString();
		if (draggedText != null && !draggedText.isEmpty()) {
			// Try to get Image if String is treated as URL
			if (ImageProcessor.isValidImageUrl(draggedText)) {
				loadImageFromUrl(draggedText);
				return;
			}
			// Try to get Image if String is treated as file path
			File draggedFile = Paths.get(draggedText).toFile();
			image = ImageProcessor.getImageFromFile(draggedFile);
			if (image != null) {
				saveTranslationImage(image);
				updateImage();
				return;
			}
		}
	}

	private void saveTranslationImage(Image image) {
		File targetFile = Paths.get(IMAGE_ROOT + translationId + IMAGE_SUFFIX).toFile();
		ImageProcessor.saveImageToFile(image, targetFile, IMAGE_SIZE, IMAGE_FORMAT);
	}

	public void loadImageFromUrl(String urlString) {
		URLImage urlImage = ImageProcessor.getImageFromUrl(urlString, IMAGE_LOADING_TIMEOUT, true);
		if (urlImage != null) {
			FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), this);
			fadeTransition.setFromValue(0.8);
			fadeTransition.setToValue(0.2);
			fadeTransition.setAutoReverse(true);
			fadeTransition.setCycleCount(Animation.INDEFINITE);
			fadeTransition.play();

			ProgressIndicator progressIndicator = new ProgressIndicator(-1);

			setGraphic(progressIndicator);

			urlImage.statusProperty().addListener((ChangeListener<Status>) (observable, oldValue, status) -> {
				if (status == URLImage.Status.SUCCESSFUL) {
					saveTranslationImage(urlImage);
				}
				Platform.runLater(() -> {
					fadeTransition.stop();
					setOpacity(1);
					updateImage();
				});
			});
		}
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
						if (ImageProcessor.isValidImageFile(draggedFile))
							return true;
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
					if (ImageProcessor.isValidImageUrl(draggedText) || ImageProcessor.isValidImageFile(draggedText))
						return true;
				}

			}
			if (dataFormat.equals(DataFormat.URL)) {
				String draggedURLString = dragboard.getUrl();
				if (draggedURLString != null) {
					if (ImageProcessor.isValidImageUrl(draggedURLString))
						return true;
				}
			}
		}
		return false;
	}

	public void deleteImage() {
		File file = new File(IMAGE_ROOT + translationId + IMAGE_SUFFIX);
		if (file.exists())
			file.delete();
		updateImage();
	}

}
