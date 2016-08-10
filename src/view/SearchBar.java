package view;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public abstract class SearchBar {
	
	private static final String SEARCH_PROMPT = "Enter search query here.";
	private static final int HBOX_SPACING = 10;
	private static final double SEARCH_BAR_WIDTH = 500;
	private static final int HBOX_PADDING = 20;
	private TextField textField;
	public Button searchButton;
	public Button booleanButton;
	private Button feelingLuckyButton;
	protected Controller myController;
	protected HBox myRoot;
	protected Screen myMainScreen;
	protected ProgressBar bar;
	
	public SearchBar(Controller controller, Screen mainScreen){
		this.myController = controller;
		this.myMainScreen = mainScreen;
		initialize();
	}
	
	private void initialize(){
		myRoot = this.makeHbox();
		this.textField = new TextField();
		this.textField.setPromptText(SEARCH_PROMPT);
		this.textField.setPrefWidth(SEARCH_BAR_WIDTH);
		this.bar = new ProgressBar();
		setupSearchButton(searchButton, booleanButton);
		setupFeelingLuckyButton(feelingLuckyButton);
		myRoot.getChildren().addAll(textField, searchButton, booleanButton, feelingLuckyButton);
	}
	
	private HBox makeHbox() {
		HBox hbox = new HBox(HBOX_SPACING);
		hbox.setPadding(new Insets(HBOX_PADDING,HBOX_PADDING,HBOX_PADDING,HBOX_PADDING));
		hbox.setAlignment(Pos.CENTER);
		return hbox;
	}
	
	abstract void setupSearchButton(Button searchButton, Button booleanButton);
	
	abstract void runDisplay(boolean mainSearch, boolean isLucky);

	private void setupFeelingLuckyButton(Button feelingLuckyButton) {
		this.feelingLuckyButton = new Button("Feeling lucky?");
		this.feelingLuckyButton.setOnAction(event -> {
			myMainScreen.updateStatus("Lucky search started.");
			runDisplay(true, true);
		});
	}
	
	public String getSearchQuery() {
		return this.textField.getText();
	}
	
	public Node getNode(){
		return this.myRoot;
	}
	
}