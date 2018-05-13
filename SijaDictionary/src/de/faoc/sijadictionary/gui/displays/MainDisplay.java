package de.faoc.sijadictionary.gui.displays;

import de.faoc.sijadictionary.gui.GuiApplicationController;
import de.faoc.sijadictionary.gui.controls.MainButton;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class MainDisplay extends Display<HBox> {
	
	@Override
	protected HBox defaultRoot() {
		return new HBox();
	}
	
	@Override
	protected String styleClass() {
		return "main-display";
	}

	@Override
	protected void init() {
		getRoot().spacingProperty().bind(getRoot().widthProperty().multiply(0.05));
		
		getRoot().setAlignment(Pos.CENTER);
		
		MainButton dictButton = MainButton.getDictButton();
		dictButton.setOnMouseClicked(event -> {
			UnitDisplay unitDisplay = new UnitDisplay();
			unitDisplay.setPreviousDisplay(this);
			GuiApplicationController.getInstance().changeDisplay(unitDisplay);
		});
		MainButton trainerButton = MainButton.getTrainerButton();
		
		getRoot().getChildren().addAll(dictButton, trainerButton);
	}




}
