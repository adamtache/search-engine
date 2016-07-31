package index;

import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.jsoup.select.Elements;

import fetcher.Fetcher;

public interface IIndex {
	
	public abstract PriorityQueue<Entry<String, Double>> getTfIds(String term);

	public abstract Fetcher getFetcher();

	public Double tfIdf(String url, String term);

	public Double idf(String term);

	public abstract int getNumUrls();

	public abstract Map<String, Integer> getCounts(String term);

	public abstract boolean isIndexed(String crawlURL);

	public abstract void indexPage(String crawlURL, Elements paragraphs);

	public abstract void deleteAllKeys();

	public abstract void deleteTermCounters();

	public abstract void deleteURLSets();

}