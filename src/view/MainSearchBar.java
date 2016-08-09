package view;

import java.util.concurrent.Callable;
import controller.Controller;
import javafx.scene.control.Button;
import search.ISearchResult;
import javafx.concurrent.Task;

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
		Task<Void> task = createTask(new Callable<Void>() {
			public Void call(){
				ISearchResult result = myController.getResults(getSearchQuery());
				if(isLucky) myController.goToLucky(result);
				else myController.display(result);
				return null;
			}
		}); 
		new Thread(task).start();
		task.setOnSucceeded(event -> {
			myMainScreen.updateStatus("Finished displaying. Done.");
			myRoot.getChildren().remove(bar);
		});
	}

}
