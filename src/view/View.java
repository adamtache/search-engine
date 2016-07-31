package view;

import java.util.PriorityQueue;
import controller.IController;
import javafx.stage.Stage;
import search.Document;

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
	public void display(PriorityQueue<Document> results) {
		
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