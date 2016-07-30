package controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import index.Document;
import index.Index;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class Main extends Application{
	
	private SearchController sc;
	
	public static void main(String[] args) throws IOException{
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		setupGUI(stage);
		sc = new SearchController();
	}
	
	private void setupGUI(Stage stage) {
		VBox vbox = new VBox(20);
		vbox.setAlignment(Pos.CENTER);
		
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.CENTER);
		
		TextField textField = new TextField();
		Button searchButton = new Button("Search");
		Button feelingLucky = new Button("Feeling lucky?");
		hbox.getChildren().addAll(textField, searchButton, feelingLucky);
		vbox.getChildren().add(hbox);
		
		ScrollPane scrollPane = new ScrollPane();
		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();
		
		scrollPane.setContent(webView);
	    scrollPane.getStyleClass().add("noborder-scroll-pane");
	    scrollPane.setStyle("-fx-background-color: white");
	    scrollPane.setFitToWidth(true);
	    scrollPane.setFitToHeight(true);
	    
	    vbox.getChildren().add(webView);
	    
	    searchButton.setOnAction(searchButtonAction(textField, webEngine));
	    feelingLucky.setOnAction(feelingLuckyAction(textField, webEngine));
		
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		stage.show();
	}
	
	private EventHandler<ActionEvent> searchButtonAction(final TextField textField, final WebEngine webEngine) {
		return new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				String searchPhrase = textField.getText();
				List<Document> results = null;
				try {
					results = sc.search(searchPhrase);
				} catch (IOException e) {
					e.printStackTrace();
				}
				for(Document doc : results){
					System.out.println(doc.getUrl()+" " + doc.get(searchPhrase));
				}
			}
		};
	}
	
	private EventHandler<ActionEvent> feelingLuckyAction(final TextField textField, final WebEngine webEngine){
		return new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				String searchPhrase = textField.getText();
				List<Document> results = null;
				try {
					results = sc.search(searchPhrase);
				} catch (IOException e) {
					e.printStackTrace();
				}
				webEngine.load("https://en.wikipedia.org" + results.get(0).getUrl());
			}
		};
	}

	public static void printTFIDF(IndexController ic, Index indexer, List<String> urls) throws IOException{
		Set<String> terms = indexer.keySet();
		for(String term : terms){
			for(String url : urls){
				System.out.println("TF-IDF of term: " + term + " for URL: " + url +" is: " + ic.tfIdf(term, url));
				System.out.println("Normalized TF-IDF of term: " + term + " for URL: " + url +" is: " + ic.normalizedTfIdf(term, url));
			}
		}
	}
}