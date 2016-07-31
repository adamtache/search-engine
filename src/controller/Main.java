package controller;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import view.IView;
import view.View;

public class Main extends Application {
	
	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 800;	
	private Stage s;
	IView view;
	
    @Override
    public void start(Stage myStage) throws Exception {
    	s = new Stage();
    	view = new View(SCREEN_WIDTH, SCREEN_HEIGHT);
    	s.setScene(view.getScene());
    	s.show();
    }
	
	public static void main(String[] args) throws IOException{
		launch(args);
	}
	
}