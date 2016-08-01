package view;


import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class StatusBar implements Runnable{
	
	private String myStatus;
	private TextArea myStatusOutput;
	private ScrollPane myStatusPane;
	
	public StatusBar(){
		initialize();
	}
	
	private void initialize(){
		this.myStatus = "Awaiting user input.";
		myStatusPane = new ScrollPane();
		myStatusOutput = new TextArea();
		myStatusOutput.setPrefWidth(1300);
		myStatusOutput.setPrefHeight(100);
		myStatusPane.setContent(myStatusOutput);
	}
	
	@Override
	public void run() {
		myStatusOutput.appendText(myStatus + "\n");
	}
	
	public void updateStatus(String status){
		this.myStatus = status;
	}
	
	public Node getNode(){
		return myStatusPane;
	}
	
}
