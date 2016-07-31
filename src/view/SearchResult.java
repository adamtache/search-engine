package view;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SearchResult {

	private VBox myRoot;
	
	public SearchResult(){
		initialize();
	}
	
	private void initialize(){
		myRoot = new VBox();
		Label results = new Label("Search Results:");
		myRoot.getChildren().add(results);
	}
	
	public Node getNode(){
		return myRoot;
	}
	
	public void addResult(String resultUrl){
		Hyperlink resultLink = new Hyperlink(resultUrl);
		myRoot.getChildren().add(resultLink);
	}
}
