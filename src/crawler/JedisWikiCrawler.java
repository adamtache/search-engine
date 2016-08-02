package crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import fetcher.WikiFetcher;
import index.IIndex;
import index.JedisIndex;
import view.IView;


public class JedisWikiCrawler extends WikiCrawler{
	// keeps track of where we started
	private final String source;

	// the index where the results go
	private IIndex index;

	// queue of URLs to be indexed
	private Queue<String> queue = new LinkedList<String>();

	// fetcher used to get pages from Wikipedia
	final static WikiFetcher wf = new WikiFetcher();
	
	private IView myView;

	/**
	 * Constructor.
	 * 
	 * @param source
	 * @param index2
	 */
	public JedisWikiCrawler(IIndex index2, IView view) {
		this.source = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		this.index = index2;
		this.myView = view;
		queue.offer(source);
		queue.offer("https://en.wikipedia.org/wiki/Claude_Shannon");
	}

	/**
	 * Constructor.
	 * 
	 * @param source
	 * @param index
	 */
	public JedisWikiCrawler(String source, JedisIndex index, IView view) {
		this.source = source;
		this.index = index;
		this.myView = view;
		queue.offer(source);
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
		} while (count < 10);
	}

	/**
	 * Gets a URL from the queue and indexes it.
	 * @param b 
	 * 
	 * @return Number of pages indexed.
	 * @throws IOException
	 */
	public String crawlPage() throws IOException {
		if (queue.isEmpty()) {
			return null;
		} else {
			String crawlURL = queue.poll();
			myView.updateStatus("Crawler reached " + crawlURL);
			Elements paragraphs;
			if (index.isIndexed(crawlURL)) {
				myView.updateStatus("Already indexed " + crawlURL);
				return null;
			} else {
				myView.updateStatus("Fetching " + crawlURL);
				paragraphs = wf.fetch(crawlURL);
			}
			index.indexPage(crawlURL, paragraphs);
			index.incrUpdateCount();
			queueInternalLinks(paragraphs);
			return crawlURL;
		}
	}

	/**
	 * Parses paragraphs and adds internal links to the queue.
	 * 
	 * @param paragraphs
	 */
	public void queueInternalLinks(Elements paragraphs) {
		for (Element para: paragraphs) {
			Elements links = para.select("a[href]");
			for (Element link: links) {
				String relUrl = link.attr("href");
				if (relUrl.startsWith("/wiki/")) {
					queue.offer("https://en.wikipedia.org" + relUrl);
				}
			}
		}
	}

}