package controller;

import java.io.IOException;
import java.util.PriorityQueue;

import search.Document;
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
	public PriorityQueue<Document> getResults() {
		return searcher.getResults(view.getSearchTerm());
	}

	@Override
	public String getResultUrl(int result) {
		PriorityQueue<Document> results = getResults();
		for(int x=0; x<result; x++){
			results.poll();
		}
		return results.poll().getUrl();
	}

}