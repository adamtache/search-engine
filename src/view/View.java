package view;

import java.io.IOException;
import controller.Controller;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import search.ISearchResult;

public class View implements IView {
	
	private Controller myController;
	private Screen myMainScreen;
	private Screen myYouTubeScreen;
	private int myWidth;
	private int myHeight;
	private Scene myScene;
	private TabPane myTabPane;
	
	public View(int windowWidth, int windowHeight) {
		this.myWidth = windowWidth;
		this.myHeight = windowHeight;
		initialize();
	}
	
	private void initialize(){
		try {
			myController = new Controller(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		myMainScreen = new MainScreen(myController, myWidth);
		myYouTubeScreen = new YouTubeScreen(myController, myWidth);
		myTabPane = new TabPane();
		myTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		myTabPane.getTabs().addAll(myMainScreen.getTab(), myYouTubeScreen.getTab());
		myScene = new Scene(myTabPane, myWidth, myHeight);
		myScene.getStylesheets().add("resources/searchengine.css");
		try {
			myController.initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void display(ISearchResult result) {
		myMainScreen.updateStatus("View telling MainScreen to display results.");
		myMainScreen.display(result);
	}
	
	@Override
	public void displayYT(ISearchResult result){
		myYouTubeScreen.updateStatus("View telling YouTubeScreen to display YT results.");
		myYouTubeScreen.display(result);
	}

	@Override
	public void display(String url) {
		myMainScreen.display(url);
	}

	@Override
	public String getSearchQuery() {
		return myMainScreen.getSearchQuery();
	}
	
	public Scene getScene(){
		return myScene;
	}
	
	public void updateStatus(String status){
		myMainScreen.updateStatus(status);
	}

}