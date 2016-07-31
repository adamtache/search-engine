package view;

import controller.IController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SearchBar {
	
	private static final int HBOX_SPACING = 10;
	private TextField textField;
	private Button searchButton;
	private Button feelingLuckyButton;
	private IController myController;
	private HBox myRoot;
//	private MainScreen myMainScreen;
	
	public SearchBar(IController controller){
		this.myController = controller;
		initialize();
	}
	
	private void initialize(){
		myRoot = this.makeHbox();
		this.textField = new TextField();
		setupSearchButton(searchButton);
		setupFeelingLuckyButton(feelingLuckyButton);
		myRoot.getChildren().addAll(textField, searchButton, feelingLuckyButton);
	}
	
	private HBox makeHbox() {
		HBox hbox = new HBox(HBOX_SPACING);
		hbox.setAlignment(Pos.CENTER);
		return hbox;
	}
	
	private void setupSearchButton(Button searchButton) {
		this.searchButton = new Button("Search");
		this.searchButton.setOnAction(event -> {
			System.out.println("Search button pressed.");
			myController.search(getSearchTerm());
			System.out.println("Finished search.");
			myController.display();
			System.out.println("Finished displaying. Done.");
		});
	}

	private void setupFeelingLuckyButton(Button feelingLuckyButton) {
		this.feelingLuckyButton = new Button("Feeling lucky?");
		this.feelingLuckyButton.setOnAction(event -> {
			myController.search(getSearchTerm());
			myController.display();
			myController.goTo(0);
		});
	}
	
	public String getSearchTerm() {
		return this.textField.getText();
	}
	
	public Node getNode(){
		return this.myRoot;
	}
}
