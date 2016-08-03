package index;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.jsoup.select.Elements;
import search.ISearchResult;

public interface IIndex {

	void storeQuery(String query, ISearchResult result);

	void indexPage(String crawlURL, Elements paragraphs) throws IOException;

	boolean isIndexed(String crawlURL);

	void deleteQueryData();

	Set<String> getDocURLs();

	String getDocValue(String url, String docTerm);

	Map<String, Double> getValues(String myTerm);

	void reset();

	void addDocumentsToDB();

	Set<String> getDocTerms();

	boolean hasQueryData(String query);

	ISearchResult getQueryResult(String query);

}