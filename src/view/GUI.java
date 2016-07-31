package view;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class GUI {
	private int windowHeight;
	private int windowWidth;
	private BorderPane myRoot;
	private Scene myScene;
	private SearchPane mySearchPane;
	
	public GUI(int width, int height){
		this.windowWidth = width;
		this.windowHeight = height;
		initScene();
	}
	
	private void initScene(){
		myRoot = new BorderPane();
		mySearchPane = new SearchPane();
		myRoot.setTop(mySearchPane.getSearchPane());
		myScene = new Scene(myRoot, windowWidth, windowHeight, Color.WHITE);
	}
	
	public Scene getScene(){
		return myScene;
	}
}
