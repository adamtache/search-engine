package view;

import search.ISearchData;

public interface IScreen {

	public abstract void display(int result);

	public abstract void display(ISearchData data);
	
}