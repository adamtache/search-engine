package controller;

import java.io.IOException;
import java.util.List;

import model.WikiCrawler;

public class WikiPhilosophy {
	
	
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		List<String> crawled = new WikiCrawler().getCrawled();
		System.out.println("There were " + crawled.size() + " pages visited in total.");
	}
	
}