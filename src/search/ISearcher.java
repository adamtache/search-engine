package search;

import java.io.IOException;

public interface ISearcher {

	public void search(String term) throws IOException;
	
	public ISearchResult getResults(String term);
	
}