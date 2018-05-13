package de.faoc.sijadictionary.gui.controls;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Space extends Pane {

	public static Pane hBoxSpace() {
		Pane space = new Pane();
		HBox.setHgrow(space, Priority.ALWAYS);
		space.setMaxWidth(Double.MAX_VALUE);
		return space;
	}

	public static Pane vBoxSpace() {
		Pane space = new Pane();
		VBox.setVgrow(space, Priority.ALWAYS);
		space.setMaxHeight(Double.MAX_VALUE);
		return space;
	}
}
