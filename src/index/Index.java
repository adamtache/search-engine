package index;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.jsoup.select.Elements;

import fetcher.Fetcher;

/**
 * Encapsulates a map from search term to set of TermCounter.
 * 
 * @author downey
 * modified adamtache
 *
 */
public abstract class Index {

	private Map<String, Set<TermCounter>> index = new HashMap<String, Set<TermCounter>>();
	private Set<String> indexed = new HashSet<>();
	
	public abstract Fetcher getFetcher();
	
	/**
	 * Adds a TermCounter to the set associated with `term`.
	 * 
	 * @param term
	 * @param tc
	 */
	public void add(String term, TermCounter tc) {
		Set<TermCounter> set = get(term);

		// if we're seeing a term for the first time, make a new Set
		if (set == null) {
			set = new HashSet<TermCounter>();
			index.put(term, set);
		}
		// otherwise we can modify an existing Set
		set.add(tc);
	}

	/**
	 * Looks up a search term and returns a set of TermCounters.
	 * 
	 * @param term
	 * @return
	 */
	public Set<TermCounter> get(String term) {
		return index.get(term);
	}
	
	/**
	 * Returns set of URLs that have been indexed.
	 * 
	 * @return
	 */
	public Set<String> getIndexed(){
		return indexed;
	}

	/**
	 * Add a page to the index.
	 * 
	 * @param url         URL of the page.
	 * @param paragraphs  Collection of elements that should be indexed.
	 * @throws IOException 
	 */
	public void indexPage(String url, Elements paragraphs) throws IOException {
		indexed.add(url);
		TermCounter tc = new TermCounter(url);
		tc.processElements(paragraphs);
		for(String term : tc.keySet()){
			this.add(term, tc);
		}
	}

	/**
	 * Prints the contents of the index.
	 */
	public void printIndex() {
		// loop through the search terms
		for (String term: keySet()) {
			System.out.println(term);
			
			// for each term, print the pages where it appears
			Set<TermCounter> tcs = get(term);
			for (TermCounter tc: tcs) {
				Integer count = tc.get(term);
				System.out.println("    " + tc.getLabel() + " " + count);
			}
		}
	}

	/**
	 * Returns the set of terms that have been indexed.
	 * 
	 * @return
	 */
	public Set<String> keySet() {
		return index.keySet();
	}

}
