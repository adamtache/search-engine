package view;

import java.util.Map.Entry;
import controller.Controller;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import parser.TokenizedData;
import search.Document;
import search.ISearchResult;

public class SearchResults {
	
	private static final int PADDING = 10;
	private VBox myRoot;
	private VBox mySearchResults;
	private StackPane myResultPane;
	private Button didYouMeanButton;
	private Controller myController;

	public SearchResults(StackPane resultPane, Controller controller){
		this.myController = controller;
		this.myResultPane = resultPane;
		initialize();
	}
	
	public Node getNode(){
		return myRoot;
	}

	private void initialize(){
		myRoot = new VBox();
		myRoot.setPadding(new Insets(PADDING,PADDING,PADDING,PADDING));
		mySearchResults = new VBox();
		Label results = new Label("Search Results:");
		mySearchResults.getChildren().add(results);
		myRoot.getChildren().add(mySearchResults);
	}
	
	private void didYouMeanButton(){
		this.didYouMeanButton = new Button("Did You Mean? " + myController.getSpellCorrected());
		this.didYouMeanButton.setOnAction(event -> {
			myController.displaySpellCorrected();
		});
		mySearchResults.getChildren().add(didYouMeanButton);
	}

	public void display(ISearchResult data) {
		myResultPane.getChildren().clear();
		myResultPane.getChildren().add(myRoot);
		if(data.checkCorrectedSpelling()){
			this.addDidYouMean(data.getTokenizedData());
		}
		System.out.println(data.getValues());
		for(Entry<Document,Double> dataEntry: data.getResults()){
			this.addResult(dataEntry.getKey());
		}
	}

	private void addResult(Document doc){
		Hyperlink resultLink = new Hyperlink(doc.getURL());
		mySearchResults.getChildren().add(resultLink);
		// also add doc.getTitle() and doc.getSnippet() to search results
	}

	private void addDidYouMean(TokenizedData data){
		didYouMeanButton();
	}
	
}