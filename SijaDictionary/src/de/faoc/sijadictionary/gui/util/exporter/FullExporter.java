package de.faoc.sijadictionary.gui.util.exporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javafx.stage.FileChooser;
import javafx.stage.Window;

public class FullExporter {

	private static final String IMG_DIR = "img/";
	private static final String DB_DIR = "db/";

	private static FileChooser fileChooser;

	private static FileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new FileChooser();
			fileChooser.setTitle("Choose export File");
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
			fileChooser.setInitialFileName("sijaExport.zip");
		}
		return fileChooser;
	}

	public static boolean export(Window window) {
		File targetFile = getFileChooser().showSaveDialog(window);
		if (targetFile != null) {
			ZipOutputStream out = null;
			try {
				if (!targetFile.getName().endsWith(".zip"))
					targetFile.renameTo(new File(targetFile.getParentFile(), targetFile.getName() + ".zip"));
				out = new ZipOutputStream(new FileOutputStream(targetFile));
				// Add dirs
				out.putNextEntry(new ZipEntry(IMG_DIR));
				out.closeEntry();
				out.putNextEntry(new ZipEntry(DB_DIR));
				out.closeEntry();
				
				return addDirToZip(out, IMG_DIR) && addDirToZip(out, DB_DIR);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {}
				}
			}

		}
		
		return false;
	}

	private static boolean addDirToZip(ZipOutputStream out, String dirPath) {
		byte[] buffer = new byte[1024];
		// Add images
		File dir = new File(dirPath);
		if (dir != null && dir.exists() && dir.isDirectory()) {

			File[] imageFiles = dir.listFiles();
			if (imageFiles == null)
				return false;

			for (File image : imageFiles) {
				try {
					FileInputStream imageStream = null;
					try {
						imageStream = new FileInputStream(image);
						out.putNextEntry(new ZipEntry(dir.getName() + "/" + image.getName()));
						int length;
						while ((length = imageStream.read(buffer)) > 0) {
							out.write(buffer, 0, length);
						}
						out.closeEntry();
					} finally {
						if (imageStream != null)
							imageStream.close();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return false;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}

		return true;
	}
}
