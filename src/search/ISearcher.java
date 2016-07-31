package search;

public interface ISearcher {

	public void search(String term);
	
	public ISearchResult getResults(String term);
	
}