package view;

import java.util.List;
import java.util.Map.Entry;

import controller.IController;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
	private SearchResult mySearchResult;
	private StatusBar myStatusBar;
	
	public MainScreen(IController controller, int windowWidth, int windowHeight) {
		this.myController = controller;
		this.myWidth = windowWidth;
		this.myHeight = windowHeight;
		initialize();
	}
	
	private void initialize() {
		myRoot = new BorderPane();
		myStatusBar = new StatusBar();
		mySearchBar = new SearchBar(myController, this);
		myLuckyResult = new LuckyResult(myController, myResultPane);
		mySearchResult = new SearchResult();
		myResultPane = new StackPane();
		myResultPane.getChildren().add(mySearchResult.getNode());
		setBorderPaneSections();
		myScene = new Scene(myRoot, myWidth, myHeight);
	}

	@Override
	public void display(ISearchData data) {
		updateStatus("MainScreen printing data");
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	myResultPane.getChildren().clear();
				myResultPane.getChildren().add(mySearchResult.getNode());
				List<Entry<String,Double>> dataEntries = data.getEntries();
				for(Entry<String,Double> dataEntry: dataEntries){
					mySearchResult.addResult(dataEntry.getKey());
				}
		    }
		});
	}

	@Override
	public void display(int result) {
		myLuckyResult.display(result);
	}
	
	private void setBorderPaneSections(){
		myRoot.setTop(mySearchBar.getNode());
		myRoot.setBottom(myStatusBar.getNode());
		myRoot.setCenter(myResultPane);
	}
	
	public void updateStatus(String status){
		myStatusBar.updateStatus(status);
	}

	public Scene getScene() {
		return myScene;
	}
	
	public String getSearchTerm(){
		return mySearchBar.getSearchTerm();
	}

}