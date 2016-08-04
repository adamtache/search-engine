package view;

import java.util.List;
import java.util.Map.Entry;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import parser.TokenizedData;
import search.ISearchResult;
import search.ResultsFactory;

public class SearchResults {
	
	private static final int PADDING = 10;
	private VBox myRoot;
	private VBox mySearchResults;
	private StackPane myResultPane;
	private Button didYouMeanButton;
	private ISearchResult data;

	public SearchResults(StackPane resultPane){
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
	
	private void didYouKnowButton(){
		TokenizedData tokenizedData = data.getTokenizedData();
		List<String> spellCorrected = tokenizedData.getSpellCorrected();
		this.didYouMeanButton = new Button("Did You Mean? " + spellCorrected);
		this.didYouMeanButton.setOnAction(event -> {
			this.display(ResultsFactory.getSpellCorrectedResult(tokenizedData));
		});
		mySearchResults.getChildren().add(didYouMeanButton);
	}

	public void display(ISearchResult data) {
		this.data = data;
		myResultPane.getChildren().clear();
		myResultPane.getChildren().add(myRoot);
		if(data.checkCorrectedSpelling()){
			this.addDidYouMean(data.getTokenizedData());
		}
		for(Entry<String,Double> dataEntry: data.getResults()){
			this.addResult(dataEntry.getKey());
		}
	}

	private void addResult(String resultUrl){
		Hyperlink resultLink = new Hyperlink(resultUrl);
		mySearchResults.getChildren().add(resultLink);
	}

	private void addDidYouMean(TokenizedData data){
		didYouKnowButton();
	}
	
}