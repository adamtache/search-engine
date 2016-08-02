package search;

import java.util.Map;

public interface ISearchResult {

	public abstract String getUrl(int result);

	public abstract void print(); // For testing purposes.

	public abstract Map<String, Double> getValues();

	public abstract Double getRelevance(String term);
	
}