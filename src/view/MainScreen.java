package view;

import controller.IController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import search.ISearchResult;

public class MainScreen implements IScreen {
	
	private final TextField textField;
	private WebEngine webEngine;
	private WebView webView;
	private Button searchButton;
	private Button feelingLuckyButton;
	private IController controller;
	
	public MainScreen(IController controller){
		this.controller = controller;
		this.textField = new TextField();
		this.webView = new WebView();
		this.webEngine = webView.getEngine();
	}
	
	@Override
	public void displayResults(ISearchResult results){
		
	}
	
	@Override
	public void display(int result) {
		this.webEngine.load(controller.getResultUrl(result));
	}

	public void initialize(Stage stage){
		VBox vbox = this.makeVbox();
		HBox hbox = this.makeHbox();
		this.searchButton = new Button("Search");
		this.feelingLuckyButton = new Button("Feeling lucky?");
		this.setupButtons(searchButton, feelingLuckyButton);
		hbox.getChildren().addAll(textField, searchButton, feelingLuckyButton);
		vbox.getChildren().add(hbox);
		this.setupScrollPane(webView);
		vbox.getChildren().add(webView);
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		stage.show();
	}
	
	private void setupButtons(Button searchButton, Button feelingLuckyButton){
		this.setupSearchButton(searchButton);
		this.setupFeelingLuckyButton(feelingLuckyButton);
	}
	
	private void setupSearchButton(Button searchButton){
		this.searchButton.setOnAction(ButtonSetup.searchButtonAction(this.getSearchTerm(), controller));
	}
	
	private void setupFeelingLuckyButton(Button feelingLuckyButton){
		this.feelingLuckyButton.setOnAction(ButtonSetup.feelingLuckyAction(this.getSearchTerm(), controller));
	}
	
	private void setupScrollPane(WebView webView){
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(webView);
		scrollPane.getStyleClass().add("noborder-scroll-pane");
		scrollPane.setStyle("-fx-background-color: white");
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
	}
	
	private HBox makeHbox(){
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.CENTER);
		return hbox;
	}
	
	private VBox makeVbox(){
		VBox vbox = new VBox(20);
		vbox.setAlignment(Pos.CENTER);
		return vbox;
	}

	public String getSearchTerm(){
		return this.textField.getText();
	}
	
}