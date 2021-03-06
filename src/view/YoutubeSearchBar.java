package view;

import controller.Controller;
import javafx.scene.control.Button;
import search.ISearchResult;
import javafx.application.Platform;

public class YoutubeSearchBar extends SearchBar {

	public YoutubeSearchBar(Controller controller, Screen mainScreen) {
		super(controller, mainScreen);
	}

	@Override
	void setupSearchButton(Button searchButton, Button booleanButton) {
		this.searchButton = new Button("YouTube Search");
		this.booleanButton = new Button("Boolean Search");
		this.searchButton.setOnAction(event -> {
			myMainScreen.updateStatus("YouTube Search started.");
			runDisplay(false, false);
		});
		this.booleanButton.setOnAction(event -> {
			myMainScreen.updateStatus("Search started.");
			runDisplay(true, false);
		});
	}
	
	@Override
	protected void runDisplay(boolean mainSearch, boolean isLucky){
		ISearchResult result = null;
		if(mainSearch){
			result = myController.getResults(getSearchQuery());
		}
		else{
			result = myController.getBooleanResults(getSearchQuery());
		}
		if(isLucky) myController.goToLucky(result);
		else myController.displayYT(result);
		Platform.runLater( () -> {
			myMainScreen.updateStatus("Finished displaying. Done.");
			myRoot.getChildren().remove(bar);
		});
	}
	
}
