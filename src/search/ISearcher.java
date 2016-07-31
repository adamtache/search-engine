package search;

import java.util.Map.Entry;
import java.util.PriorityQueue;

public interface ISearcher {

	public void search(String term);
	
	public PriorityQueue<Entry<String, Double>> getResults(String term);
	
}