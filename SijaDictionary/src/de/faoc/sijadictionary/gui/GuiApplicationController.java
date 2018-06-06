package de.faoc.sijadictionary.gui;

import de.faoc.sijadictionary.gui.controls.HeaderBar;
import de.faoc.sijadictionary.gui.displays.Display;
import de.faoc.sijadictionary.gui.displays.MainDisplay;
import de.faoc.sijadictionary.gui.displays.UnitDisplay;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class GuiApplicationController {
	
	private static GuiApplicationController instance;
	
	private Display currentDiplay;
	private HeaderBar headerBar;
	private MainDisplay mainDisplay;
	
	@FXML
	private BorderPane mainContainer;
	
    @FXML
    private HBox top;
    
    @FXML
    public void initialize() {
    	instance = this;
    	
    	headerToolbar();
    	mainDisplay = new MainDisplay();
    	changeDisplay(mainDisplay);
    }

	private void headerToolbar() {
		headerBar = new HeaderBar();
		HBox.setHgrow(headerBar, Priority.ALWAYS);
		
		headerBar.setBackPressedListener(event -> {
			backPressed();
		});
		headerBar.setLangChangedListener(event -> {
			if(!(currentDiplay instanceof MainDisplay)) {
				UnitDisplay newUnitDisplay = new UnitDisplay();
				newUnitDisplay.setPreviousDisplay(mainDisplay);
				changeDisplay(newUnitDisplay);
			}
		});
		
		top.getChildren().add(headerBar);
	}
	
	private void backPressed() {
		if(currentDiplay.getPreviousDisplay() != null)
			changeDisplay(currentDiplay.getPreviousDisplay());
	}

	public void changeDisplay(Display display) {
		mainContainer.setCenter(display.getRoot());
		currentDiplay = display;
	}

	/***********
	 * Getter & Setters
	 ***********/
	
	public static GuiApplicationController getInstance() {
		return instance;
	}

	public Display getCurrentDiplay() {
		return currentDiplay;
	}
	
	public String getFromLang() {
		return headerBar.fromLangProperty().get();
	}
	
	public String getToLang() {
		return headerBar.toLangProperty().get();
	}
	
}
