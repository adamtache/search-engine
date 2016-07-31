package search;

import java.util.Map;

public interface ISearchResult {

	public abstract String getUrl(int result);

	public abstract void print(); // For testing purposes.

	public abstract ISearchResult and(ISearchResult search2);
	
	public abstract ISearchResult or(ISearchResult search2);

	public abstract Map<String, Integer> getCounts();

	public abstract Integer getRelevance(String term);

	public abstract double tfIdf(String url, String term);

	public abstract double idf(String term);
	
}