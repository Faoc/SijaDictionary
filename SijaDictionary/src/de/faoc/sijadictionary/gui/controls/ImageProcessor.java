package de.faoc.sijadictionary.gui.controls;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javafx.scene.image.Image;

public class ImageProcessor {

	private static final String IMAGE_ENDING_REGEX = "^(.+?)\\.(gif|jpe?g|tiff|png)$";

	private ImageProcessor() {
	};

	public static Image getImageFromFile(File file) {
		if (file != null && !file.isDirectory() && file.exists())
			try {
				return new Image(new BufferedInputStream(new FileInputStream(file)));
			} catch (FileNotFoundException e) {
			}
		return null;
	}

	public static Image getFirstImageFromFileList(List<File> files) {
		for (File file : files) {
			Image image = getImageFromFile(file);
			if (image != null)
				return image;
		}
		return null;
	}

	public static Image getImageFromUrl(URL url) {
		try {
			if (url.toString().matches(IMAGE_ENDING_REGEX))
				return new Image(url.toString());
		} catch (NullPointerException | IllegalArgumentException e) {
		}
		return null;
	}

}
