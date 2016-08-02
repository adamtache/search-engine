package controller;

import java.io.IOException;
import search.ISearchResult;
import search.ISearcher;
import search.Searcher;
import view.IView;

public class Controller implements IController {
	
	private ISearcher mySearcher;
	private IView myView;
	
	public Controller(IView view) throws IOException {
		this.myView = view;
		this.mySearcher = new Searcher(view);
	}

	@Override
	public void search(String term) throws IOException {
		myView.updateStatus("Controller telling searcher to search.");
		mySearcher.search(term);
	}

	@Override
	public void display() {
		myView.updateStatus("Controller telling view to display data.");
		myView.display();
	}
	
	@Override
	public void goTo(int page){
		myView.updateStatus("Controller telling view to display page.");
		myView.display(page);
	}

	@Override
	public ISearchResult getResults() {
		myView.updateStatus("View getting results from searcher to send to MainScreen.");
		return mySearcher.getResults(myView.getSearchTerm());
	}

	@Override
	public String getResultUrl(int result) {
		return getResults().getUrl(result);
	}

}