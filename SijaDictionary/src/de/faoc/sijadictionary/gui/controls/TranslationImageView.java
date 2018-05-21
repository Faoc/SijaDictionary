package de.faoc.sijadictionary.gui.controls;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TranslationImageView extends ImageView {
	
	private static final String IMAGE_ROOT = "img/";
	
	private int translationId;
	private boolean previewMode;
	
	private Image image;
	
	public TranslationImageView(int translationId, boolean previewMode) {
		super();
		this.translationId = translationId;
		this.previewMode = previewMode;
		
		init();
	}

	private void init() {
		getStyleClass().addAll("translation-imageview");
		
		initImage();
		
		setImage(image);
		
		setPreserveRatio(true);
		setSmooth(true);
	}

	private void initImage()  {
		//See if image is present / create path if not present
		String imagePathString = IMAGE_ROOT + translationId + ".png";
		Path imagePath = Paths.get(imagePathString);
		File imageFile = imagePath.toFile();
		if(imageFile.exists()) {
			try {
				image = new Image(new BufferedInputStream(new FileInputStream(imageFile)));
			} catch (FileNotFoundException e) {
				System.out.println("Couln't load image " + imagePath + "!");
				e.printStackTrace();
				image = Icons.getImage(Icons.IMAGE_IMAGE_PATH);
			}
		} else {
			//Load default image
			image = Icons.getImage(Icons.IMAGE_IMAGE_PATH);
		}
	}
	
	
}
