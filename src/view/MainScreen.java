package view;

import controller.Controller;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import search.ISearchResult;

public class MainScreen implements IScreen {

	private Controller myController;
	private int myWidth;
	private int myHeight;
	private BorderPane myRoot;
	private SearchBar mySearchBar;
	private Scene myScene;
	private WebBrowser myWebBrowser;
	private StackPane myResultPane;
	private SearchResults mySearchResult;
	private StatusBar myStatusBar;
	
	public MainScreen(Controller controller, int windowWidth, int windowHeight) {
		this.myController = controller;
		this.myWidth = windowWidth;
		this.myHeight = windowHeight;
		initialize();
	}
	
	private void initialize() {
		myRoot = new BorderPane();
		myStatusBar = new StatusBar(myWidth);
		mySearchBar = new SearchBar(myController, this);
		myResultPane = new StackPane();
		myWebBrowser = new WebBrowser(myResultPane);
		mySearchResult = new SearchResults(myResultPane, myController);
		myResultPane.getChildren().add(mySearchResult.getNode());
		setBorderPaneSections();
		myScene = new Scene(myRoot, myWidth, myHeight);
		myScene.getStylesheets().add("resources/searchengine.css");
	}

	@Override
	public void display(ISearchResult result) {
		updateStatus("Displaying results.");
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
				mySearchResult.display(result);
		    }
		});
	}

	@Override
	public void display(String url) {
		updateStatus("MainScreen loading webpage.");
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	myWebBrowser.display(url);
		    }
		});
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
	
	public String getSearchQuery(){
		return mySearchBar.getSearchQuery();
	}

}