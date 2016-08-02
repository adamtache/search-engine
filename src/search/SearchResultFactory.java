package search;

import java.util.Map;

import index.IIndex;

public class SearchResultFactory {

	/**
	 * Performs a search and makes a WikiSearch object.
	 * 
	 * @param term
	 * @param index
	 * @return
	 */
	public static ISearchResult search(String term, IIndex index) {
		Map<String, Double> map = index.getValues(term);
		return new SearchResult(map);
	}
	
}