package de.faoc.sijadictionary.gui.displays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.faoc.sijadictionary.core.database.DataSet;
import de.faoc.sijadictionary.core.database.DatabaseHelper;
import de.faoc.sijadictionary.core.database.DatabaseStatements;
import de.faoc.sijadictionary.core.database.DatabaseTables;
import de.faoc.sijadictionary.gui.GuiApplicationController;
import de.faoc.sijadictionary.gui.controls.Icons;
import de.faoc.sijadictionary.gui.controls.MainButton;
import de.faoc.sijadictionary.gui.controls.UnitBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class UnitDisplay extends Display<BorderPane> {
	
	private Button addButton;
	private Label title;
	private VBox center;

	public UnitDisplay() {
		super();
		init();
	}

	@Override
	protected BorderPane defaultRoot() {
		return new BorderPane();
	}
	
	@Override
	protected String styleClass() {
		return "unit-display";
	}

	@Override
	protected void init() {
		title = new Label("Units");
		title.getStyleClass().add("title");
		BorderPane.setAlignment(title, Pos.CENTER);
		BorderPane.setMargin(title, new Insets(10));
		
		addButton = Icons.getIconButton(Icons.ADD_IMAGE_PATH, 15);
		addButton.getStyleClass().addAll("add-unit-button", "green-button");
		addButton.setOnMouseClicked(event -> {
			addUnit();
		});
		BorderPane.setAlignment(addButton, Pos.CENTER);
		BorderPane.setMargin(addButton, new Insets(10));
		
		center = new VBox(10);
		center.setAlignment(Pos.TOP_CENTER);
		center.getChildren().addAll(populateUnits());
		BorderPane.setAlignment(center, Pos.TOP_CENTER);
		BorderPane.setMargin(center, new Insets(10));
		
		//Wrap the center into a Scrollpane
		ScrollPane centerWrapper = new ScrollPane(center);
		centerWrapper.setFitToHeight(true);
		centerWrapper.setFitToWidth(true);
		
		getRoot().setTop(title);
		getRoot().setCenter(centerWrapper);
		getRoot().setBottom(addButton);
	}

	private void addUnit() {
		GuiApplicationController controller = GuiApplicationController.getInstance();
		DatabaseHelper.executeUpdate(DatabaseStatements.Insert.unit(controller.getFromLang(), controller.getToLang(), "New Unit"));
		reload();
	}

	public void reload() {
		UnitDisplay unitDisplay = new UnitDisplay();
		unitDisplay.setPreviousDisplay(getPreviousDisplay());
		GuiApplicationController.getInstance().changeDisplay(unitDisplay);
	}

	private List<Node> populateUnits() {
		GuiApplicationController controller = GuiApplicationController.getInstance();
		DataSet units = DatabaseHelper.query(DatabaseStatements.Query.unit(controller.getFromLang(), controller.getToLang()));
		if(units.isEmpty()) {
			return Arrays.asList(new Label("No units"));
		} else {
			ArrayList<Node> unitBoxes = new ArrayList<>();
			for(HashMap<String, Object> unit : units) {
				int id = (int) unit.get(DatabaseTables.Unit.ID);
				String name = (String) unit.get(DatabaseTables.Unit.NAME);
				unitBoxes.add(new UnitBox(id, name, this));
			}
			return unitBoxes;
		}
	}
	
}
