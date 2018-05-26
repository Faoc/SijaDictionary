package de.faoc.sijadictionary.gui.controls;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import com.sun.net.httpserver.Authenticator.Success;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;

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

	public static URLImage getImageFromUrl(URL url, long timeout, boolean resetOnProgress) {
		try {
			if (url.toString().matches(IMAGE_ENDING_REGEX))
				return new URLImage(url, timeout, resetOnProgress);
		} catch (NullPointerException | IllegalArgumentException e) {
		}
		return null;
	}

	public static URLImage getImageFromUrl(URL url, long timeout) {
		return getImageFromUrl(url, timeout, false);
	}

	public static URLImage getImageFromUrl(URL url) {
		return getImageFromUrl(url, -1, false);
	}

	public static URLImage getImageFromUrl(String urlString, long timeout, boolean resetOnProgress) {
		try {
			return getImageFromUrl(new URL(urlString), timeout, resetOnProgress);
		} catch (MalformedURLException e) {
		}

		return null;
	}

	public static URLImage getImageFromUrl(String urlString, long timeout) {
		return getImageFromUrl(urlString, timeout, false);
	}

	public static URLImage getImageFromUrl(String urlString) {
		return getImageFromUrl(urlString, -1, false);
	}

	public static boolean isValidImageUrl(String urlString) {
		if (!urlString.matches(IMAGE_ENDING_REGEX))
			return false;

		try {
			URL url = new URL(urlString);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}

	public static boolean isValidImageFile(String path) {
		File file = Paths.get(path).toFile();
		Image image = ImageProcessor.getImageFromFile(file);
		return image != null;
	}

	public static boolean saveImageToFile(Image image, File file, int targetSize, String format) {
		if (image == null || file == null)
			return false;

		// Fit image
		ImageView imageView = new ImageView(image);
		imageView.setSmooth(true);
		imageView.setPreserveRatio(true);
		if (image.getWidth() < image.getHeight()) {
			if (image.getWidth() > targetSize)
				imageView.setFitWidth(targetSize);
		} else {
			if (image.getHeight() > targetSize) {
				imageView.setFitHeight(targetSize);
			}
		}
		Image resizedImage = imageView.snapshot(null, null);

		// Save resized image
		boolean success;
		try {
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			success = ImageIO.write(SwingFXUtils.fromFXImage(resizedImage, null), format, file);
		} catch (IOException e) {
			success = false;
			e.printStackTrace();
		}

		return success;
	}

}
