package controller;

import search.ISearchResult;

public interface IController {

	public abstract void display();
	
	public abstract void goTo(int page);

	public abstract ISearchResult getResults(String query);

	public abstract ISearchResult getCurrentResult();

	public abstract void initialize();
	
}