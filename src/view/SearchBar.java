package view;

import java.io.IOException;
import java.util.concurrent.Callable;

import controller.IController;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SearchBar {
	
	private static final String SEARCH_PROMPT = "Enter search query here.";
	private static final int HBOX_SPACING = 10;
	private static final double SEARCH_BAR_WIDTH = 300;
	private static final int HBOX_PADDING = 20;
	private TextField textField;
	private Button searchButton;
	private Button feelingLuckyButton;
	private IController myController;
	private HBox myRoot;
	private MainScreen myMainScreen;
	private ProgressBar bar;
	
	public SearchBar(IController controller, MainScreen mainScreen){
		this.myController = controller;
		this.myMainScreen = mainScreen;
		initialize();
	}
	
	private void initialize(){
		myRoot = this.makeHbox();
		this.textField = new TextField();
		this.textField.setPromptText(SEARCH_PROMPT);
		this.textField.setPrefWidth(SEARCH_BAR_WIDTH);
		this.bar = new ProgressBar();
		setupSearchButton(searchButton);
		setupFeelingLuckyButton(feelingLuckyButton);
		myRoot.getChildren().addAll(textField, searchButton, feelingLuckyButton);
	}
	
	private HBox makeHbox() {
		HBox hbox = new HBox(HBOX_SPACING);
		hbox.setPadding(new Insets(HBOX_PADDING,HBOX_PADDING,HBOX_PADDING,HBOX_PADDING));
		hbox.setAlignment(Pos.CENTER);
		return hbox;
	}
	
	private void setupSearchButton(Button searchButton) {
		this.searchButton = new Button("Search");
		this.searchButton.setOnAction(event -> {
			myMainScreen.updateStatus("Search started.");
			runSearch(false);
		});
	}
	
	private void runSearch(boolean isLucky){
		Task<Void> task = createTask(new Callable<Void>() {
			public Void call() throws IOException{
				myController.search(getSearchTerm());
				return null;
			}
		});
		new Thread(task).start();
		task.setOnSucceeded(event -> {
		myMainScreen.updateStatus("Finished search.");
		runDisplay(isLucky);
		});
	}
	
	private void runDisplay(boolean isLucky){
		Task<Void> task = createTask(new Callable<Void>() {
			public Void call(){
				if(isLucky) myController.goTo(0);
				else myController.display();
				return null;
			}
		}); 
		new Thread(task).start();
		task.setOnSucceeded(event -> {
			myMainScreen.updateStatus("Finished displaying. Done.");
			myRoot.getChildren().remove(bar);
		});
	}

	private void setupFeelingLuckyButton(Button feelingLuckyButton) {
		this.feelingLuckyButton = new Button("Feeling lucky?");
		this.feelingLuckyButton.setOnAction(event -> {
			myMainScreen.updateStatus("Lucky search started.");
			runSearch(true);
		});
	}
	
	
	private Task<Void> createTask(Callable<Void> myFunc){
		Task<Void> task = new Task<Void> () {
			@Override
			public Void call(){
				try {
					myFunc.call();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		bar.progressProperty().bind(task.progressProperty());
		if(!myRoot.getChildren().contains(bar)){
			myRoot.getChildren().add(bar);
		}
		return task;
	}
	
	public String getSearchTerm() {
		return this.textField.getText();
	}
	
	public Node getNode(){
		return this.myRoot;
	}
	
}