package de.faoc.sijadictionary.gui.displays;

import de.faoc.sijadictionary.gui.controls.MainButton;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class MainDisplay extends HBox {

	public MainDisplay() {
		super();
		init();
	}

	private void init() {
		getStyleClass().add("main-display");
		
		spacingProperty().bind(widthProperty().multiply(0.05));
		
		setAlignment(Pos.CENTER);
		
		getChildren().addAll(MainButton.getDictButton(), MainButton.getTrainerButton());
	}

}
