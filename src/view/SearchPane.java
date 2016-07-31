package view;

import java.io.IOException;

import controller.SearchController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SearchPane {
	private static final int SEARCH_BAR_WIDTH = 600;
	private static final int BUTTON_WIDTH = 50;
	private static final String SEARCH_PROMPT = "Enter search query here.";
	private static final String BUTTON_TEXT = "Go!";
	private static final int TOP_PADDING = 10;
	private static final int RIGHT_PADDING = 50;
	private static final int BOTTOM_PADDING = 10;
	private static final int LEFT_PADDING = 50;
	private static final int SPACING = 30;
	private StackPane myStackPane;
	private String mySearchTerm;
	private TextField mySearchBar;
	private Button mySearchButton;
	private SearchController mySearchController;
	
	public SearchPane(){
		initTopPane();
	}
	private void initTopPane(){
		myStackPane = new StackPane();
		try{
			mySearchController = new SearchController();
		}catch(IOException e){
			e.printStackTrace();
		}
		HBox hbox = new HBox(SPACING);
		hbox.setPadding(new Insets(TOP_PADDING,RIGHT_PADDING,BOTTOM_PADDING,LEFT_PADDING));
		createSearchBar(hbox);
		createSearchButton(hbox);
		myStackPane.getChildren().add(hbox);
	}
	
	private void createSearchBar(HBox container){
		mySearchBar = new TextField();
		mySearchBar.setPrefWidth(SEARCH_BAR_WIDTH);
		mySearchBar.setPromptText(SEARCH_PROMPT);
		container.getChildren().add(mySearchBar);
	}
	
	private void createSearchButton(HBox container){
		mySearchButton = new Button(BUTTON_TEXT);
		mySearchButton.setPrefWidth(BUTTON_WIDTH);
		container.getChildren().add(mySearchButton);
		mySearchButton.setOnAction(e -> {
		try {  
			mySearchController.search(getSearchTerm());
		} catch (Exception e1) {
			e1.printStackTrace();
		}});
	}
	
	public Pane getSearchPane(){
		return myStackPane;
	}
	
	private String getSearchTerm(){
		return mySearchTerm;
	}
}
