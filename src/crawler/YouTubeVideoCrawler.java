package crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fetcher.PageData;
import fetcher.YouTubeData;
import fetcher.YouTubeVideoFetcher;
import index.IIndex;

/**
 * Web crawler for YouTube videos. 
 * Uses YouTubeVideoFetcher to allows access to video specific data and closed captioning via an external website.
 * 
 */
public class YouTubeVideoCrawler implements Crawler{

	// the index where the results go
	private IIndex index;

	// queue of URLs to be indexed
	private Queue<String> queue = new LinkedList<String>();

	// fetcher used to get pages from Wikipedia
	final static YouTubeVideoFetcher yf = new YouTubeVideoFetcher();

	/**
	 * Constructor.
	 * 
	 * @param source
	 * @param index
	 */
	public YouTubeVideoCrawler(IIndex index) {
		this.index = index;
		queue.offer("https://www.youtube.com/watch?v=KOwKOT79uiA");
	}

	/**
	 * Returns the number of URLs in the queue.
	 * 
	 * @return
	 */
	public int queueSize() {
		return queue.size();	
	}

	public void crawl() throws IOException{
		int count = 0;
		do {
			this.crawlPage();
			count ++;
		} while (count < 50);
	}

	/**
	 * Gets a URL from the queue and indexes it.
	 * 
	 * @return True of indexed a new page
	 * @throws IOException
	 */
	private boolean crawlPage() throws IOException {
		if (queue.isEmpty())
			return false;
		String crawlURL = queue.poll();
		PageData pageData;
		if (index.isIndexed(crawlURL)) {
			return false;
		} else {
			this.updateDB();
			pageData = yf.fetch(crawlURL);
		}
		index.indexPage(pageData);
		queueInternalLinks(((YouTubeData) pageData).getYouTubeParas());
		return true;
	}

	/**
	 * Parses paragraphs and adds internal links to the queue.
	 * 
	 * @param paragraphs
	 */
	public void queueInternalLinks(Elements paragraphs) {
		if(paragraphs != null){
			for (Element para: paragraphs) {
				Elements links = para.select("a[href]");
				for (Element link: links) {
					String relUrl = link.attr("href");
					if (relUrl.startsWith("/watch?v=")) {
						queue.offer("https://youtube.com" + relUrl);
					}
				}
			}
		}
	}
	
	@Override
	public IIndex getIndex() {
		return this.index;
	}

}