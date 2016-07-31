package view;
import controller.IController;
import javafx.stage.Stage;

public class View implements IView {
	
	private IController controller;
	private MainScreen mainScreen;
	
	public View(IController controller) {
		this.controller = controller;
	}
	
	public void initialize(Stage stage){
		mainScreen = new MainScreen(controller);
		mainScreen.initialize(stage);
	}
	
	@Override
	public void display() {
		System.out.println("View telling MainScreen to display results.");
		mainScreen.display(controller.getResults());
	}

	@Override
	public void display(int result) {
		mainScreen.display(result);
	}

	@Override
	public String getSearchTerm() {
		return mainScreen.getSearchTerm();
	}

}