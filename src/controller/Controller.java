package controller;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
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
		view.display();
	}
	
	@Override
	public void go_to(int page){
		view.display(page);
	}

	@Override
	public PriorityQueue<Entry<String, Double>> getResults() {
		return searcher.getResults(view.getSearchTerm());
	}

	@Override
	public String getResultUrl(int result) {
		PriorityQueue<Entry<String, Double>> search_result = getResults();
		if(search_result.size() == 0){
			return "http://www.google.com";
		}
		List<Entry<String, Double>> temp = new ArrayList<>();
		for(int x=0; x<result; x++){
			temp.add(search_result.poll());
		}
		Entry<String, Double> resultEntry = search_result.poll();
		temp.add(resultEntry);
		String resultUrl = resultEntry.getKey();
		search_result.addAll(temp);
		return resultUrl;
	}

}