package crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fetcher.WikiFetcher;
import index.JedisIndex;
import index.JedisMaker;
import redis.clients.jedis.Jedis;


public class JedisWikiCrawler extends WikiCrawler{
	// keeps track of where we started
	private final String source;

	// the index where the results go
	private JedisIndex index;

	// queue of URLs to be indexed
	private Queue<String> queue = new LinkedList<String>();

	// fetcher used to get pages from Wikipedia
	final static WikiFetcher wf = new WikiFetcher();

	/**
	 * Constructor.
	 * 
	 * @param source
	 * @param index
	 */
	public JedisWikiCrawler(JedisIndex index) {
		this.source = super.getRandomURL();
		this.index = index;
		queue.offer(source);
	}

	/**
	 * Constructor.
	 * 
	 * @param source
	 * @param index
	 */
	public JedisWikiCrawler(String source, JedisIndex index) {
		this.source = source;
		this.index = index;
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

	/**
	 * Gets a URL from the queue and indexes it.
	 * @param b 
	 * 
	 * @return Number of pages indexed.
	 * @throws IOException
	 */
	public String crawl() throws IOException {
		if (queue.isEmpty()) {
			return null;
		} else {
			String crawlURL = queue.poll();
			System.out.println("CRAWLED: " + crawlURL);
			Elements paragraphs;
			if (index.isIndexed(crawlURL)) {
				return null;
			} else {
				paragraphs = wf.fetch(crawlURL);
			}
			index.indexPage(crawlURL, paragraphs);
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

	public static void main(String[] args) throws IOException {
		// make a WikiCrawler
		Jedis jedis = JedisMaker.make();
		JedisIndex index = new JedisIndex(jedis); 
		String source = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		JedisWikiCrawler wc = new JedisWikiCrawler(source, index);

		// for testing purposes, load up the queue
		Elements paragraphs = wf.fetch(source);
		wc.queueInternalLinks(paragraphs);

		// loop until we index a new page
		String res;
		do {
			res = wc.crawl();
		} while (res == null);

		Map<String, Integer> map = index.getCounts("the");
		for (Entry<String, Integer> entry: map.entrySet()) {
			System.out.println(entry);
		}
	}

}