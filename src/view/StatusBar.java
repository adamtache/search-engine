package view;


import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class StatusBar{
	
	private static final int WINDOW_WIDTH = 800;
	private TextArea myStatusOutput;
	private ScrollPane myStatusPane;
	
	public StatusBar(){
		initialize();
	}
	
	private void initialize(){
		myStatusPane = new ScrollPane();
		myStatusOutput = new TextArea("Awaiting user input.\n");
		myStatusOutput.setPrefWidth(WINDOW_WIDTH);
		myStatusOutput.setPrefHeight(150);
		myStatusPane.setContent(myStatusOutput);
	}
	
	public void updateStatus(String status){
		myStatusOutput.appendText(status + "\n");
	}
	
	public Node getNode(){
		return myStatusPane;
	}
	
}
