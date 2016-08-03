package search;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import parser.TokenizedData;

public interface ISearchResult {

	public abstract String getUrl(int result);

	public abstract Double getRelevance(String term);
	
	public abstract Map<String, Double> getValues();

	public abstract List<Entry<String, Double>> getResults();

	public abstract ISearchResult and(ISearchResult that);

	public abstract ISearchResult or(ISearchResult that);

	public abstract ISearchResult minus(ISearchResult that);

	public abstract void setTokenizedData(TokenizedData data);
	
	public abstract TokenizedData getTokenizedData();
	
	public abstract boolean checkCorrectedSpelling();
	
}