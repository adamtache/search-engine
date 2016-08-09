package crawler;

import java.io.IOException;

import org.jsoup.select.Elements;

import index.IIndex;

/**
 * Represents a web crawler that can crawl, queue internal links from pages it crawls, and is associated with an index with a DB.
 * 
 */
public interface Crawler {
	
	/**
	 * Gets a URL from the queue and indexes it.
	 *
	 * @throws IOException
	 */
	public void crawl() throws IOException;
	
	public void queueInternalLinks(Elements paragraphs);
	
	public default void updateDB(){
		getIndex().deleteQueryData();
	}

	public IIndex getIndex();
	
}