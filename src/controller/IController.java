package controller;

import java.util.PriorityQueue;

import search.Document;
import view.IView;

public interface IController {

	public abstract void search(String term);
	
	public abstract void display();
	
	public abstract void go_to(int page);

	public abstract PriorityQueue<Document> getResults();

	public abstract String getResultUrl(int result);

	public abstract void setView(IView view);
	
}