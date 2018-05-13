package de.faoc.sijadictionary.gui.displays;

import javafx.scene.layout.Pane;

public abstract class Display<T extends Pane>{
	
	private Display<?> previousDisplay;
	private T root;
	private String styleClass;
	
	public Display() {
		this(null);
	}

	public Display(Display<?> previousDisplay) {
		super();
		this.previousDisplay = previousDisplay;
		this.root = defaultRoot();
		
		getRoot().getStyleClass().add("display");
		if(styleClass() != null) {
			getRoot().getStyleClass().add(styleClass());
		}
		init();
	}
	
	protected abstract void init();

	protected abstract T defaultRoot();
	
	protected abstract String styleClass();
	
	/***********
	 * Getter & Setters
	 ***********/

	public T getRoot() {
		return root;
	}

	public Display<?> getPreviousDisplay() {
		return previousDisplay;
	}

	public void setPreviousDisplay(Display<?> previousDisplay) {
		this.previousDisplay = previousDisplay;
	}

	public String getStyleClass() {
		return styleClass;
	}
	
}
