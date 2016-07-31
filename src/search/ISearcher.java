package search;

public interface ISearcher {

	public void search(String term);
	
	public ISearchData getResults(String term);
	
}