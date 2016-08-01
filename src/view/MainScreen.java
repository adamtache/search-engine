package view;

import java.util.List;
import java.util.Map.Entry;

import controller.IController;
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
	private Thread myOutputThread;
	private StatusBar myStatusBar;
	
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
		setUpStatusBar();
		mySearchBar = new SearchBar(myController, this);
		myLuckyResult = new LuckyResult(myController, myResultPane);
		mySearchResult = new SearchResult();
		myResultPane = new StackPane();
		myResultPane.getChildren().add(mySearchResult.getNode());
		setBorderPaneSections();
		myScene = new Scene(myRoot, myWidth, myHeight);
	}
	
	private void setBorderPaneSections(){
		myRoot.setTop(mySearchBar.getNode());
		myRoot.setBottom(myStatusBar.getNode());
		myRoot.setCenter(myResultPane);
	}
	
	private void setUpStatusBar(){
		myStatusBar = new StatusBar();
		myOutputThread = new Thread(myStatusBar);
		myOutputThread.setPriority(Thread.MAX_PRIORITY);
		myOutputThread.start();
	}
	
	public void updateStatus(String status){
		myStatusBar.updateStatus(status);
		System.out.println(status);
		myOutputThread.run();
	}

	public Scene getScene() {
		return myScene;
	}
	
	public String getSearchTerm(){
		return mySearchBar.getSearchTerm();
	}

}