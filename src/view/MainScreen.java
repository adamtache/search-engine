package view;

import java.util.List;
import java.util.Map.Entry;

import controller.IController;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import search.ISearchData;

public class MainScreen implements IScreen {

	private IController myController;
	private int myWidth;
	private int myHeight;
	private BorderPane myRoot;
	private SearchBar mySearchBar;
	private Scene myScene;
	private LuckyResult myLuckyResult;
	private StackPane myResultPane;
	private VBox myStatusPane;
	private SearchResult mySearchResult;
	
	public MainScreen(IController controller, int windowWidth, int windowHeight) {
		this.myController = controller;
		this.myWidth = windowWidth;
		this.myHeight = windowHeight;
		initialize();
	}

	@Override
	public void display(ISearchData data) {
		updateStatus("MainScreen printing data");
		myResultPane.getChildren().clear();
		myResultPane.getChildren().add(mySearchResult.getNode());
		List<Entry<String,Double>> dataEntries = data.getEntries();
		for(Entry<String,Double> dataEntry: dataEntries){
			mySearchResult.addResult(dataEntry.getKey());
		}
	}

	@Override
	public void display(int result) {
		myLuckyResult.display(result);
	}

	private void initialize() {
		myRoot = new BorderPane();
		myResultPane = new StackPane();
		createStatusPane();
		mySearchBar = new SearchBar(myController);
		myLuckyResult = new LuckyResult(myController, myResultPane);
		mySearchResult = new SearchResult();
		myRoot.setTop(mySearchBar.getNode());
		myResultPane.getChildren().add(mySearchResult.getNode());
		myRoot.setCenter(myResultPane);
		myRoot.setBottom(myStatusPane);
		myScene = new Scene(myRoot, myWidth, myHeight);
	}
	
	private void createStatusPane(){
		myStatusPane = new VBox();
		Label myStatus = new Label("Awaiting user input.");
		myStatusPane.getChildren().add(myStatus);
		//TODO : make a scrollpane
	}
	
	public void updateStatus(String status){
		myStatusPane.getChildren().add(new Label(status));
	}

	public Scene getScene() {
		return myScene;
	}
	
	public String getSearchTerm(){
		return mySearchBar.getSearchTerm();
	}

}