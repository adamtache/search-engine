package view;

import java.util.PriorityQueue;

import javafx.stage.Stage;
import search.Document;

public interface IView {
	
	public abstract void display(PriorityQueue<Document> results);
	
	public abstract void display(int page);

	public abstract void initialize(Stage stage);
	
	public abstract String getSearchTerm();
	
}