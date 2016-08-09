package view;

import controller.Controller;
import javafx.scene.control.Button;
import search.ISearchResult;
import javafx.application.Platform;

public class MainSearchBar extends SearchBar{

	public MainSearchBar(Controller controller, Screen mainScreen) {
		super(controller, mainScreen);
	}

	@Override
	void setupSearchButton(Button searchButton) {
		this.searchButton = new Button("Search");
		this.searchButton.setOnAction(event -> {
			myMainScreen.updateStatus("Search started.");
			runDisplay(false);
		});
	}
	
	@Override
	protected void runDisplay(boolean isLucky){
		ISearchResult result = myController.getResults(getSearchQuery());
		if(isLucky) myController.goToLucky(result);
		else myController.display(result);
		Platform.runLater( () -> {
//			ISearchResult result = myController.getResults(getSearchQuery());
//			if(isLucky) myController.goToLucky(result);
//			else myController.display(result);
			myMainScreen.updateStatus("Finished displaying. Done.");
			myRoot.getChildren().remove(bar);
		});
	}

}
