package controller;

import search.ISearchData;

public interface IController {

	public abstract void search(String term);
	
	public abstract void display();
	
	public abstract void goTo(int page);

	public abstract ISearchData getResults();

	public abstract String getResultUrl(int result);
	
}