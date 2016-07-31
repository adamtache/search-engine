package controller;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import search.Searcher;
import view.View;

public class Main extends Application{
	
	private View view;
	private Searcher sc;
	
	public static void main(String[] args) throws IOException{
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		sc = new Searcher();
		view = new View(sc);
		view.initialize(stage);
	}
	
}