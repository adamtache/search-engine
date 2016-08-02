package view;

import java.util.List;
import java.util.Map.Entry;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SearchResult {

	private VBox myRoot;
	private VBox mySearchResults;
	private StackPane myResultPane;
	
	public SearchResult(StackPane resultPane){
		this.myResultPane = resultPane;
		initialize();
	}
	
	private void initialize(){
		myRoot = new VBox();
		mySearchResults = new VBox();
		Label results = new Label("Search Results:");
		mySearchResults.getChildren().add(results);
		myRoot.getChildren().add(mySearchResults);
	}
	
	public Node getNode(){
		return myRoot;
	}
	
	public void addResult(String resultUrl){
		Hyperlink resultLink = new Hyperlink(resultUrl);
		mySearchResults.getChildren().add(resultLink);
	}

	public void display(List<Entry<String, Double>> dataEntries) {
		myResultPane.getChildren().clear();
		myResultPane.getChildren().add(myRoot);
		mySearchResults.getChildren().clear();
		for(Entry<String,Double> dataEntry: dataEntries){
			addResult(dataEntry.getKey());
		}
	}
}