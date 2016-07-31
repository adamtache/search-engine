package controller;

import java.io.IOException;
import search.ISearchResult;
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
		searcher.search(term);
	}

	@Override
	public void display() {
		view.display(searcher.getResults(view.getSearchTerm()));
	}
	
	@Override
	public void go_to(int page){
		view.display(page);
	}

	@Override
	public ISearchResult getResults() {
		return searcher.getResults(view.getSearchTerm());
	}

	@Override
	public String getResultUrl(int result) {
		ISearchResult search_result = getResults();
		return search_result.getUrl(result);
	}

}