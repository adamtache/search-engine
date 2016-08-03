package view;

import controller.IController;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class LuckyResult {
	
	private WebEngine myWebEngine;
	private WebView myWebView;
	private IController myController;
	private StackPane myResultPane;
	
	public LuckyResult(IController controller, StackPane resultPane){
		this.myController = controller;
		this.myResultPane = resultPane;
		initialize();
	}
	
	private void initialize(){
		myWebView = new WebView();
		myWebEngine = myWebView.getEngine();
	}
	
	public Node getNode(){
		return myWebView;
	}
	
	public void display(int result) {
		myWebEngine.load(myController.getResultUrl(result));
		myResultPane.getChildren().clear();
		myResultPane.getChildren().add(myWebView);
	}
}
