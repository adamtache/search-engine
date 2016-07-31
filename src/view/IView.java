package view;

import javafx.stage.Stage;
import search.ISearchResult;

public interface IView {
	
	public abstract void display(int page);

	public abstract void initialize(Stage stage);
	
	public abstract String getSearchTerm();

	public abstract void display(ISearchResult results);
	
}