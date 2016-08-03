package view;

import javafx.scene.Scene;

public interface IView {
	
	public abstract void display(int page);
	
	public abstract String getSearchQuery();
	
	public void display();

	public abstract Scene getScene();
	
	public void updateStatus(String status);
	
}