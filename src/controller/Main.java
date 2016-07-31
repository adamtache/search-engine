package controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import index.Index;
import javafx.application.Application;
import javafx.stage.Stage;
import view.GUI;

public class Main extends Application {
	
	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 800;	
	private Stage s;
	GUI myGui;
	
    @Override
    public void start(Stage myStage) throws Exception {
    	s = new Stage();
    	myGui = new GUI(SCREEN_WIDTH,SCREEN_HEIGHT);
    	s.setScene(myGui.getScene());
    	s.show();
    }

	public static void main(String[] args) throws IOException{
		launch(args);
	}

	public static void printTFIDF(IndexController ic, Index indexer, List<String> urls) throws IOException{
		Set<String> terms = indexer.keySet();
		for(String term : terms){
			for(String url : urls){
				System.out.println("TF-IDF of term: " + term + " for URL: " + url +" is: " + ic.tfIdf(term, url));
				System.out.println("Normalized TF-IDF of term: " + term + " for URL: " + url +" is: " + ic.normalizedTfIdf(term, url));
			}
		}
	}

}