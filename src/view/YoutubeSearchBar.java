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
	void setupSearchButton(Button searchButton) {
		this.searchButton = new Button("YouTube Search");
		this.searchButton.setOnAction(event -> {
			myMainScreen.updateStatus("YouTube Search started.");
			runDisplay(false);
		});
	}
	
	@Override
	protected void runDisplay(boolean isLucky){
		Platform.runLater( () -> {
			ISearchResult result = myController.getYTResults(getSearchQuery());
			if(isLucky) myController.goToLucky(result);
			else myController.display(result);
			myMainScreen.updateStatus("Finished displaying. Done.");
			myRoot.getChildren().remove(bar);
		});
	}
	
}
