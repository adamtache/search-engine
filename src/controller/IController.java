package controller;

import java.io.IOException;

import search.ISearchResult;

public interface IController {

	public abstract void search(String term) throws IOException;
	
	public abstract void display();
	
	public abstract void goTo(int page);

	public abstract ISearchResult getResults();

	public abstract String getResultUrl(int result);
	
}