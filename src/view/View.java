package view;

import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import controller.IController;
import javafx.stage.Stage;

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
	public void display() {
		PriorityQueue<Entry<String, Double>> results = controller.getResults();
		List<Entry<String, Double>> temp = new ArrayList<>();
		for(int x=0; x<results.size(); x++){
			Entry<String, Double> result = results.poll();
			System.out.println(result.getKey()+" "+result.getValue());
			temp.add(result);
		}
		results.addAll(temp);
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