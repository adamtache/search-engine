package view;

import controller.IController;
import javafx.stage.Stage;
import search.ISearchResult;

public class View implements IView {
	
	private IController controller;
	private MainScreen mainScreen;
	
	public View(IController controller) {
		this.controller = controller;
	}
	
	public void initialize(Stage stage){
		mainScreen = new MainScreen(controller);
		mainScreen.initialize(stage);
	}

	@Override
	public void display(ISearchResult results) {
		results.print();
	}

	@Override
	public void display(int result) {
		mainScreen.display(result);
	}

	@Override
	public String getSearchTerm() {
		return mainScreen.getSearchTerm();
	}

}