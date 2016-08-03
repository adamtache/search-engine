package view;

import javafx.scene.Scene;
import search.ISearchResult;

public interface IView {
	
	public abstract String getSearchQuery();

	public abstract Scene getScene();
	
	public abstract void updateStatus(String status);

	public abstract void display(ISearchResult result);

	public abstract void display(String url);
	
}