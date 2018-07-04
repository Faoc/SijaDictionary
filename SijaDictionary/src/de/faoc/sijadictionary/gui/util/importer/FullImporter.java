package de.faoc.sijadictionary.gui.util.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import sun.security.x509.IssuingDistributionPointExtension;

public class FullImporter {

    private static final String IMG_DIR = "img/";
    private static final String DB_DIR = "db/";

    private static FileChooser fileChooser;

    private static FileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new FileChooser();
            fileChooser.setTitle("Choose import File");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIP Archive (*.zip)", "*.zip");
            fileChooser.getExtensionFilters().add(extFilter);
        }
        return fileChooser;
    }

    public static boolean importData(Window window) {
        File selectedFile = getFileChooser().showOpenDialog(window);
        if (selectedFile != null && selectedFile.exists()) {
            byte[] buffer = new byte[1024];
            ZipInputStream zis = null;
            try {
                zis = new ZipInputStream(new FileInputStream(selectedFile));
                ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    File newFile = new File(zipEntry.getName());

                    if (zipEntry.isDirectory() && !newFile.exists())
                        Files.createDirectories(Paths.get(newFile.getPath()));
                    else if (!zipEntry.isDirectory()) {
                        if (newFile.getParentFile() != null && !newFile.getParentFile().exists())
                            Files.createDirectories(Paths.get(newFile.getParentFile().getPath()));
                        //Write file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(newFile, false);
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                            fos.close();
                        } finally {
                            if (fos != null)
                                fos.close();
                        }
                    }
                    zis.closeEntry();
                    
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (zis != null)
                    try {
                        zis.close();
                    } catch (IOException e) {
                    }
            }

        }
        return false;
    }

}
