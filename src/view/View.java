package view;

import java.io.IOException;
import java.util.List;

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
import search.Document;
import search.Searcher;

public class View {
	
	private Searcher sc;

	public View(Searcher sc) throws IOException {
		this.sc = sc;
	}

	public void initialize(Stage stage) throws IOException{
		VBox vbox = this.makeVbox();
		HBox hbox = this.makeHbox();
		TextField textField = new TextField();
		Button searchButton = new Button("Search");
		Button feelingLucky = new Button("Feeling lucky?");
		hbox.getChildren().addAll(textField, searchButton, feelingLucky);
		vbox.getChildren().add(hbox);
		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();
		this.setupScrollPane(webView);
		vbox.getChildren().add(webView);
		searchButton.setOnAction(searchButtonAction(textField, webEngine));
		feelingLucky.setOnAction(feelingLuckyAction(textField, webEngine));
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		stage.show();
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
				webEngine.load(results.get(0).getUrl());
			}
		};
	}

}