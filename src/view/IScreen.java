package view;

import search.ISearchResult;

public interface IScreen {

	public abstract void display(ISearchResult data);

	public abstract void display(String url);
	
}