package view;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebBrowser {
	
	private WebEngine myWebEngine;
	private WebView myWebView;
	private StackPane myResultPane;
	
	public WebBrowser(StackPane resultPane){
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
	
	public void loadURL(String url){
		myWebEngine.load(url);
	}
	
	public void display(String url) {
		this.loadURL(url);
		myResultPane.getChildren().clear();
		myResultPane.getChildren().add(myWebView);
	}
	
}