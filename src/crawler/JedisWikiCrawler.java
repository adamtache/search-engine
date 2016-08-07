package crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fetcher.PageData;
import fetcher.WikiFetcher;
import index.IIndex;
import view.IView;


public class JedisWikiCrawler implements Crawler{
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
	public JedisWikiCrawler(IIndex index, IView view) {
		source = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		this.index = index;
		this.myView = view;
		queue.offer(source);
		queue.offer("https://en.wikipedia.org/wiki/Claude_Shannon");
		queue.offer("https://en.wikipedia.org/wiki/Google");
		queue.offer("https://en.wikipedia.org/wiki/Quantum_mechanics");
		queue.offer("https://en.wikipedia.org/wiki/Cypress_Bay_High_School");
		queue.offer("https://en.wikipedia.org/wiki/Burger");
		queue.offer("https://en.wikipedia.org/wiki/Ice_cream_sandwich");
		queue.offer("https://en.wikipedia.org/wiki/Rubik%27s_Cube");
		queue.offer("https://en.wikipedia.org/wiki/Duke_University");
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
	 * @return True of indexed a new page
	 * @throws IOException
	 */
	public boolean crawlPage() throws IOException {
		if (queue.isEmpty())
			return false;
		String crawlURL = queue.poll();
		System.out.println("Crawling " + crawlURL);
		PageData pageData;
		if (index.isIndexed(crawlURL)) {
			myView.updateStatus("Already indexed " + crawlURL);
			return false;
		} else {
			myView.updateStatus("Fetching " + crawlURL);
			this.updateDB();
			pageData = wf.fetch(crawlURL);
		}
		index.indexPage(pageData);
		//queueInternalLinks(paragraphs);
		return true;
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

	@Override
	public IIndex getIndex() {
		return this.index;
	}

}