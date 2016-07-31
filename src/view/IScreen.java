package view;

import java.util.PriorityQueue;

import search.Document;

public interface IScreen {

	
	public abstract void display(int result);

	public abstract void displayResults(PriorityQueue<Document> results);
	
}