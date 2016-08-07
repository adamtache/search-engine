package index;

import java.util.List;
import java.util.Map;
import java.util.Set;

import fetcher.PageData;
import search.Document;
import search.ISearchResult;

public interface IIndex {

	public abstract void storeQuery(String query, ISearchResult result);

	public abstract boolean isIndexed(String crawlURL);

	public abstract void deleteQueryData();

	public abstract Set<String> getDocURLs();

	public abstract String getDocValue(String url, String docTerm);

	public abstract Map<Document, Double> getValues(String myTerm);

	public abstract void reset();

	public abstract void addDocumentsToDB();

	public abstract Set<String> getDocTerms();

	public abstract boolean hasQueryData(String query);

	public abstract ISearchResult getQueryResult(String query);

	public abstract List<Double> getDoc(String url, Set<String> docTerms);

	public abstract Set<String> getMatchingDocURLs(List<String> tokens);

	public abstract void indexPage(PageData pageData);

	public abstract String getDocTitle(String url);

	public abstract String getDocSnippet(String url);

	String urlSetKey(String term);

}