package view;

import search.ISearchResult;

public interface IScreen {

	public abstract void display(int result);

	public abstract void display(ISearchResult data);
	
}