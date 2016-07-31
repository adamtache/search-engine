package controller;

import java.io.IOException;
import search.ISearchData;
import search.ISearcher;
import search.Searcher;
import view.IView;

public class Controller implements IController {
	
	private ISearcher searcher;
	private IView view;
	
	public Controller() throws IOException {
		searcher = new Searcher();
	}
	
	public void setView(IView view){
		this.view = view;
	}

	@Override
	public void search(String term) {
		System.out.println("Controller telling searcher to search.");
		searcher.search(term);
	}

	@Override
	public void display() {
		System.out.println("Controller telling view to display data.");
		view.display();
	}
	
	@Override
	public void go_to(int page){
		view.display(page);
	}

	@Override
	public ISearchData getResults() {
		System.out.println("View getting results from searcher to send to MainScreen.");
		return searcher.getResults(view.getSearchTerm());
	}

	@Override
	public String getResultUrl(int result) {
		return getResults().get(result);
	}

}