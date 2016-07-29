package controller.tests;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.util.Set;

import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import model.fetcher.WikiFetcher;
import model.index.Index;
import model.index.TermCounter;
import model.index.WikiIndex;

public class IndexTest {

	private Index index;
	private WikiFetcher wf;

	@Before
	public void setUp() {
		wf = new WikiFetcher();
		index = new WikiIndex();
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		WikiFetcher wf = new WikiFetcher();
		Index indexer = new WikiIndex();

		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		Elements paragraphs = wf.fetch(url);
		indexer.indexPage(url, paragraphs);
		
		url = "https://en.wikipedia.org/wiki/Programming_language";
		paragraphs = wf.fetch(url);
		indexer.indexPage(url, paragraphs);
		
		indexer.printIndex();
	}

	@Test
	public void testIndexPage() throws IOException {
		// add two pages to the index
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		Elements paragraphs = wf.read(url);
		index.indexPage(url, paragraphs);
		
		url = "https://en.wikipedia.org/wiki/Programming_language";
		paragraphs = wf.read(url);
		index.indexPage(url, paragraphs);
		
		// check the results: the word "occur" only appears on one page, twice
		Set<TermCounter> set = index.get("occur");
		assertThat(set.size(), is(1));
		
		for (TermCounter tc: set) {
			// this loop only happens once
			assertThat(tc.size(), is(4798));
			assertThat(tc.get("occur"), is(2));
			assertThat(tc.get("not there"), is(0));
		}
	}

}
