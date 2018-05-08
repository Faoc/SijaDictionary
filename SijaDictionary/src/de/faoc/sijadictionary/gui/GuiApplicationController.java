package de.faoc.sijadictionary.gui;

import de.faoc.sijadictionary.gui.displays.MainDisplay;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

public class GuiApplicationController {

    @FXML
    private HBox top;

    @FXML
    private ScrollPane center;
    
    @FXML
    public void initialize() {
    	initToolbar();
    	center.setContent(new MainDisplay());
    }

	private void initToolbar() {
		
	}

}
