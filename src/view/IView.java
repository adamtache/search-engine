package view;

import javafx.stage.Stage;

public interface IView {
	
	public abstract void display(int page);

	public abstract void initialize(Stage stage);
	
	public abstract String getSearchTerm();
	
	public void display();
	
}