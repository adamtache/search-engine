package index;

import java.io.IOException;
import java.util.Map;

import org.jsoup.select.Elements;

import fetcher.Fetcher;
import view.IView;

public interface IIndex {

	public abstract Fetcher getFetcher();

	public Double tfIdf(String url, String term);

	public Double idf(String term);

	public abstract int getNumUrls();

	public abstract boolean isIndexed(String crawlURL);

	public abstract void indexPage(String crawlURL, Elements paragraphs) throws IOException;

	public abstract void clear();

	public abstract void incrUpdateCount();

	public abstract Map<String, Double> getTfIdfs(String term);
	
	public abstract IView getView();

}