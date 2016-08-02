package view;

import java.io.IOException;
import controller.Controller;
import controller.IController;
import javafx.scene.Scene;

public class View implements IView {
	
	private IController myController;
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
	}
	
	@Override
	public void display() {
		myMainScreen.updateStatus("View telling MainScreen to display results.");
		myMainScreen.display(myController.getResults());
	}

	@Override
	public void display(int result) {
		myMainScreen.display(result);
	}

	@Override
	public String getSearchTerm() {
		return myMainScreen.getSearchTerm();
	}
	
	public Scene getScene(){
		return myMainScreen.getScene();
	}
	
	public void updateStatus(String status){
		myMainScreen.updateStatus(status);
	}

}