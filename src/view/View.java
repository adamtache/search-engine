package view;

import java.io.IOException;
import controller.Controller;
import javafx.scene.Scene;
import search.ISearchResult;

public class View implements IView {
	
	private Controller myController;
	private MainScreen myMainScreen;
	private int myWidth;
	private int myHeight;
	
	public View(int windowWidth, int windowHeight) {
		this.myWidth = windowWidth;
		this.myHeight = windowHeight;
		initialize();
	}
	
	private void initialize(){
		try {
			myController = new Controller(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		myMainScreen = new MainScreen(myController, myWidth, myHeight);
		myController.initialize();
	}
	
	@Override
	public void display(ISearchResult result) {
		myMainScreen.updateStatus("View telling MainScreen to display results.");
		myMainScreen.display(result);
	}

	@Override
	public void display(String url) {
		myMainScreen.display(url);
	}

	@Override
	public String getSearchQuery() {
		return myMainScreen.getSearchQuery();
	}
	
	public Scene getScene(){
		return myMainScreen.getScene();
	}
	
	public void updateStatus(String status){
		myMainScreen.updateStatus(status);
	}

}