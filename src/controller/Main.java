package controller;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import view.IView;
import view.View;

public class Main extends Application {
	
	private IView view;
	private IController controller;
	
	public static void main(String[] args) throws IOException{
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		controller = new Controller();
		view = new View(controller);
		controller.setView(view);
		view.initialize(stage);
	}
	
}