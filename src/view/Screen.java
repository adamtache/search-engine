package view;

import controller.Controller;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import search.ISearchResult;

public abstract class Screen implements IScreen{
	
	protected Controller myController;
	private int myWidth;
	private BorderPane myRoot;
	protected SearchBar mySearchBar;
	private WebBrowser myWebBrowser;
	private StackPane myResultPane;
	private SearchResults mySearchResult;
	private StatusBar myStatusBar;
	protected Tab myTab;
	
	public Screen(Controller controller, int windowWidth){
		this.myController = controller;
		this.myWidth = windowWidth;
		initialize();
	}
	
	private void initialize() {
		myRoot = new BorderPane();
		myTab = new Tab();
		myStatusBar = new StatusBar(myWidth);
		setUpSearchBar();
		myResultPane = new StackPane();
		myWebBrowser = new WebBrowser(myResultPane);
		mySearchResult = new SearchResults(myResultPane, myController);
		myResultPane.getChildren().add(mySearchResult.getNode());
		setBorderPaneSections();
		myTab.setContent(myRoot);
		setTabText();
	}
	
	abstract void setUpSearchBar();
	
	abstract void setTabText();

	@Override
	public void display(ISearchResult data) {
		updateStatus("Displaying results.");
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
				mySearchResult.display(data);
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
	
	public Tab getTab(){
		return myTab;
	}
	
	public String getSearchQuery(){
		return mySearchBar.getSearchQuery();
	}
	
}
