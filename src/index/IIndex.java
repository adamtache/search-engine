package index;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.jsoup.select.Elements;

import fetcher.Fetcher;

public interface IIndex {

	public abstract Fetcher getFetcher();

	public Double tfIdf(String url, String term);

	public Double idf(String term);

	public abstract int getNumUrls();

	public abstract Map<String, Double> getValues(String term);

	public abstract boolean isIndexed(String crawlURL);

	public abstract void indexPage(String crawlURL, Elements paragraphs) throws IOException;

	public abstract void clear();

	public abstract void incrUpdateCount();

	public abstract PriorityQueue<Entry<String, Double>> getTfIdfs(String term);

}