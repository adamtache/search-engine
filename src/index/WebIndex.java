package index;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import view.IView;

public class WebIndex extends JedisIndex {

	public WebIndex(Jedis jedis, IView view) {
		super(jedis, view);
	}

	/**
	 * Returns the Redis key for a given search term.
	 * 
	 * @return Redis key.
	 */
	public String urlSetKey(String term) {
		return "URLSet:" + term;
	}

	/**
	 * Returns the Redis key for a given search term.
	 * 
	 * @return Redis key.
	 */
	public String getValueKey(String url, String term) {
		return "Value:" + url+term;
	}

	/**
	 * Returns the Redis key for a given document
	 * 
	 * @return Redis key.
	 */
	public String getDocKey(String url) {
		return "Doc:" + url;
	}

	/**
	 * Returns the Redis key for a given document's values
	 * 
	 * @return Redis key.
	 */
	public String getDocValuesKey(String url) {
		return "DocValues:" + url;
	}

	/**
	 * Returns the Redis key for query results.
	 * @param query 
	 * 
	 * @return Redis key.
	 */
	public String queryKey(String query) {
		return "Query"+query;
	}

	/**
	 * Returns the Redis key to access Set of URLs indexed.
	 * 
	 * @return Redis key.
	 */
	public String urlKey() {
		return "IndexedURLs";
	}
	
	/**
	 * Returns the Redis key for a URL's TermCounter.
	 * 
	 * @return Redis key.
	 */
	public String termCounterKey(String url) {
		return "TermCounter:" + url;
	}
	
	/**
	 * Deletes all query data from the database.
	 * 
	 * Called by crawler when new pages are crawled to delete old data.
	 * 
	 * @return
	 */
	public void deleteQueryData() {
		Set<String> keys = jedis.keys("Query*");
		Transaction t = jedis.multi();
		for (String key: keys) {
			t.del(key);
		}
		t.exec();
	}

	/**
	 * Deletes all query data from the database.
	 * 
	 * Called by controller when new pages are crawled to delete old data.
	 * 
	 * @return
	 */
	public void deleteDocData() {
		Set<String> keys = jedis.keys("Doc:*");
		keys.addAll(jedis.keys("DocValues:*"));
		Transaction t = jedis.multi();
		for (String key: keys) {
			t.del(key);
		}
		t.exec();
	}

	/**
	 * Returns URLSet keys for the terms that have been indexed.
	 * 
	 * Should be used for development and testing, not production.
	 * 
	 * @return
	 */
	public Set<String> urlSetKeys() {
		return jedis.keys("URLSet:*");
	}

	/**
	 * Returns doc terms keys for the set of terms that have been indexed.
	 * 
	 * 
	 * @return
	 */
	public String docTermsKey() {
		return "DocTerms";
	}

	/**
	 * Returns TermCounter keys for the URLS that have been indexed.
	 * 
	 * Should be used for development and testing, not production.
	 * 
	 * @return
	 */
	public Set<String> termCounterKeys() {
		return jedis.keys("TermCounter:*");
	}
	
}
