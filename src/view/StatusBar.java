package view;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class StatusBar{
	
	private static final int MY_HEIGHT = 100;
	private TextArea myStatusOutput;
	private ScrollPane myStatusPane;
	private int myWidth;
	
	public StatusBar(int windowWidth){
		this.myWidth = windowWidth;
		initialize();
	}
	
	private void initialize(){
		myStatusPane = new ScrollPane();
		myStatusOutput = new TextArea("Awaiting user input.\n");
		myStatusOutput.setPrefWidth(myWidth);
		myStatusOutput.setPrefHeight(MY_HEIGHT);
		myStatusPane.setContent(myStatusOutput);
	}
	
	public void updateStatus(String status){
		Platform.runLater(new Runnable() {
		    public void run() {
		    	myStatusOutput.appendText(status + "\n");
		    }
		});
	}
	
	public Node getNode(){
		return myStatusPane;
	}
	
}
