package controller;

import java.util.Map.Entry;
import java.util.PriorityQueue;
import view.IView;

public interface IController {

	public abstract void search(String term);
	
	public abstract void display();
	
	public abstract void go_to(int page);

	public abstract PriorityQueue<Entry<String, Double>> getResults();

	public abstract String getResultUrl(int result);

	public abstract void setView(IView view);
	
}