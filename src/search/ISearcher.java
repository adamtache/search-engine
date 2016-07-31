package search;

import java.util.PriorityQueue;

public interface ISearcher {

	public void search(String term);
	
	public PriorityQueue<Document> getResults(String term);
	
}