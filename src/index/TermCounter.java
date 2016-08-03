package index;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import crawler.NodeIterable;
import loaders.StopWordLoader;


/**
 * Encapsulates a map from search term to frequency (count).
 * 
 * @author downey
 * modified by adamtache to include stop words
 *
 */
public class TermCounter {

	private Map<String, Integer> map;
	private String label;
	private Set<String> stopWords;

	public TermCounter(String label) throws IOException {
		this.label = label;
		this.map = new HashMap<String, Integer>();
		this.stopWords = new HashSet<>();
		setupStopWords();
	}
	
	private void setupStopWords() throws IOException {
		stopWords = new StopWordLoader().getLines();
	}

	public String getLabel() {
		return label;
	}

	/**
	 * Returns the total of all counts.
	 * 
	 * @return
	 */
	public int size() {
		int size = 0;
		for(String key : this.map.keySet()){
			size += this.get(key);
		}
		return size;
	}

	/**
	 * Takes a collection of Elements and counts their words.
	 * 
	 * @param paragraphs
	 */
	public void processElements(Elements paragraphs) {
		for (Node node: paragraphs) {
			processTree(node);
		}
	}

	/**
	 * Finds TextNodes in a DOM tree and counts their words.
	 * 
	 * @param root
	 */
	public void processTree(Node root) {
		// NOTE: we could use select to find the TextNodes, but since
		// we already have a tree iterator, let's use it.
		for (Node node: new NodeIterable(root)) {
			if (node instanceof TextNode) {
				processText(((TextNode) node).text());
			}
		}
	}

	/**
	 * Splits `text` into words and counts them.
	 * 
	 * @param text  The text to process.
	 */
	public void processText(String text) {
		// replace punctuation with spaces, convert to lower case, and split on whitespace
		String[] array = text.replaceAll("\\pP", " ").toLowerCase().split("\\s+");

		for (int i=0; i<array.length; i++) {
			String term = array[i];
			incrementTermCount(term);
		}
	}

	/**
	 * Increments the counter associated with `term`.
	 * 
	 * @param term
	 */
	public void incrementTermCount(String term) {
		if(!isStopWord(term))
			put(term, get(term) + 1);
	}
	
	private boolean isStopWord(String term){
		return stopWords.contains(term);
	}

	/**
	 * Adds a term to the map with a given count.
	 * 
	 * @param term
	 * @param count
	 */
	public void put(String term, int count) {
		map.put(term, count);
	}

	/**
	 * Returns the count associated with this term, or 0 if it is unseen.
	 * 
	 * @param term
	 * @return
	 */
	public Integer get(String term) {
		Integer count = map.get(term);
		return count == null ? 0 : count;
	}

	/**
	 * Returns the set of terms that have been counted.
	 * 
	 * @return
	 */
	public Set<String> keySet() {
		return map.keySet();
	}
	
}