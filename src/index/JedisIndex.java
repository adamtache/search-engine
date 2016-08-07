package index;

import java.util.Map;
import java.util.Set;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.jsoup.select.Elements;

import fetcher.PageData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import search.Document;
import search.ISearchResult;
import search.SearchResult;
import view.IView;

/**
 * Represents a Redis-backed web search index.
 * 
 */
public class JedisIndex implements IIndex {

	private Jedis jedis;
	private IView myView;

	/**
	 * Constructor.
	 * 
	 * @param jedis
	 */
	public JedisIndex(Jedis jedis, IView view) {
		this.jedis = jedis;
		this.myView = view;
	}

	/**
	 * Returns the Redis key for a given search term.
	 * 
	 * @return Redis key.
	 */
	private String urlSetKey(String term) {
		return "URLSet:" + term;
	}

	/**
	 * Returns the Redis key for a given search term.
	 * 
	 * @return Redis key.
	 */
	private String getValueKey(String url, String term) {
		return "Value:" + url+term;
	}

	/**
	 * Returns the Redis key for a given document
	 * 
	 * @return Redis key.
	 */
	private String getDocKey(String url) {
		return "Doc:" + url;
	}

	/**
	 * Returns the Redis key for a given document's values
	 * 
	 * @return Redis key.
	 */
	private String getDocValuesKey(String url) {
		return "DocValues:" + url;
	}

	/**
	 * Returns the Redis key for query results.
	 * @param query 
	 * 
	 * @return Redis key.
	 */
	private String queryKey(String query) {
		return "Query"+query;
	}

	/**
	 * Returns the Redis key to access Set of URLs indexed.
	 * 
	 * @return Redis key.
	 */
	private String urlKey() {
		return "IndexedURLs";
	}

	public Long numberIndexedPages(){
		return jedis.scard(urlKey());
	}

	public Set<String> indexedPages(){
		return jedis.smembers(urlKey());
	}

	/**
	 * Returns the Redis key for a URL's TermCounter.
	 * 
	 * @return Redis key.
	 */
	private String termCounterKey(String url) {
		return "TermCounter:" + url;
	}
	
	/**
	 * Returns the Redis key for a URL's title.
	 * 
	 * @return Redis key.
	 */
	private String titleKey(String url) {
		return "Title:" + url;
	}
	
	/**
	 * Returns the Redis key for a URL's snippet.
	 * 
	 * @return Redis key.
	 */
	private String snippetKey(String url) {
		return "Snippet:" + url;
	}

	/**
	 * Checks whether we have a TermCounter for a given URL.
	 * 
	 * @param url
	 * @return
	 */
	public boolean isIndexed(String url) {
		String redisKey = termCounterKey(url);
		return jedis.exists(redisKey);
	}

	/**
	 * Adds a URL to the set associated with `term`.
	 * 
	 * @param term
	 * @param tc
	 */
	public void add(String term, TermCounter tc) {
		jedis.sadd(urlSetKey(term), tc.getLabel());
	}

	/**
	 * Looks up a search term and returns a set of URLs.
	 * 
	 * @param term
	 * @return Set of URLs.
	 */
	public Set<String> getURLs(String term) {
		return jedis.smembers(urlSetKey(term));
	}

	/**
	 * Returns the number of times the given term appears at the given URL.
	 * 
	 * @param url
	 * @param term
	 * @return
	 */
	public Double getCount(String url, String term) {
		String redisKey = termCounterKey(url);
		String count = jedis.hget(redisKey, term);
		if(count == null){
			return new Double(0);
		}
		return new Double(count);
	}

	/**
	 * Add a page to the index.
	 * 
	 * @param url         URL of the page.
	 * @param paragraphs  Collection of elements that should be indexed.
	 * @throws IOException 
	 */
	public void indexPage(PageData pageData) {
		String url = pageData.getURL();
		myView.updateStatus("Indexing " + url);

		// make a TermCounter and count the terms in the paragraphs
		TermCounter tc = null;
		try {
			tc = new TermCounter(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		tc.processElements(pageData.getParas());

		pushDataRedis(pageData.getTitle(), tc);
	}

	/**
	 * Pushes the contents of the TermCounter and corresponding data to Redis.
	 * 
	 * @param tc
	 * @return List of return values from Redis.
	 */
	public List<Object> pushDataRedis(String title, TermCounter tc) {
		Transaction t = jedis.multi();

		String url = tc.getLabel();
		t.sadd(urlKey(), url); // Add URL to list of URLs indexed.
		String hashname = termCounterKey(url);
		
		String snippet = tc.getSnippet();
		t.set(titleKey(url), title); // Add title of URL to DB.
		t.set(snippetKey(url), snippet); // Add snippet of URL to DB.

		t.del(hashname);

		for (String term: tc.keySet()) {
			Integer count = tc.get(term);
			t.hset(hashname, term, count.toString()); // Keeps track of set of documents the term appears with corresponding term count (TF).
			t.sadd(urlSetKey(term), url); // Add URL to set of documents the word appears (IDF).
			t.sadd(docTermsKey(), term); // Add term to set of corpus terms.
		}
		List<Object> res = t.exec();
		return res;
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
	private void deleteDocData() {
		Set<String> keys = jedis.keys("Doc:*");
		keys.remove("Doc:https://en.wikipedia.org/wiki/Claude_Shannon");
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

	@Override
	public Set<String> getDocURLs(){
		return jedis.smembers(this.urlKey());
	}

	@Override
	public Set<String> getDocTerms(){
		return jedis.smembers(this.docTermsKey());
	}

	@Override
	public void addDocumentsToDB() {
		this.deleteDocData();
		Set<String> docURLs = this.getDocURLs();
		docURLs.remove("https://en.wikipedia.org/wiki/Claude_Shannon");
		Set<String> terms = this.getDocTerms();
		for(String url : docURLs){
			System.out.println("Adding document: " + url);
			for(String term : terms){
				double tfIdf = this.tfIdf(url, term);
				String tfIdfStr = tfIdf + "";
				String dbLookup = jedis.get(getValueKey(url, term));
				if(dbLookup != null){
					tfIdf = Double.parseDouble(dbLookup);
				}
				else{
					jedis.set(getValueKey(url, term), tfIdfStr);
				}
				Transaction t = jedis.multi();
				t.hset(getDocKey(url), term, tfIdfStr); // Adds term and TF-IDF to URL.
				t.rpush(getDocValuesKey(url), tfIdfStr); // Adds TF-IDF to document value list.
				t.exec();
			}
		}
	}

	@Override
	public Map<Document, Double> getValues(String term) {
		Long numURLs = jedis.scard(urlSetKey(term));
		myView.updateStatus("Number URLs for term: " + numURLs);
		if(numURLs == 0){
			return new HashMap<Document, Double>();
		}
		Map<Document, Double> results = new HashMap<Document, Double>();
		for(String url : getURLs(term)){
			myView.updateStatus("Calculating TFIDF for " + term+" for URL: " + url);
			double tfIdf = this.tfIdf(url, term);
			myView.updateStatus("TFIDF: " + tfIdf);
			results.put(getDocument(url), tfIdf);
		}
		return results;
	}

	private Document getDocument(String url){
		return new Document(url, this.getDocTitle(url), this.getDocSnippet(url));
	}

	private Double tfIdf(String url, String term){
		double tf = getCount(url, term);
		myView.updateStatus("Index determined TF to be: "+tf+".");
		return tf * this.idf(term);
	}

	private Double idf(String term){
		Long numDocuments = this.numberIndexedPages();
		Long documentFrequency = jedis.scard(urlSetKey(term));
		return 1 + Math.log((double) numDocuments/documentFrequency);
	}

	@Override
	public String getDocValue(String url, String term) {
		return jedis.hget(getDocKey(url), term);
	}

	@Override
	public void storeQuery(String query, ISearchResult result) {
		if(result == null) return;
		Map<Document, Double> results = result.getValues();
		for(Document doc : results.keySet()){
			String url = doc.getURL();
			jedis.hset(this.queryKey(query), url, results.get(url)+"");
		}
	}

	/**
	 * Deletes all URLSet objects from the database.
	 * 
	 * Should be used for development and testing, not production.
	 * 
	 * @return
	 */
	private void deleteURLSets() {
		Set<String> keys = urlSetKeys();
		Transaction t = jedis.multi();
		for (String key: keys) {
			t.del(key);
		}
		t.exec();
	}

	/**
	 * Deletes all term counters from the database.
	 * 
	 * Should be used for development and testing, not production.
	 * 
	 * @return
	 */
	private void deleteTermCounters() {
		Set<String> keys = termCounterKeys();
		Transaction t = jedis.multi();
		for (String key: keys) {
			t.del(key);
		}
		t.exec();
	}

	/**
	 * Deletes all keys from the database.
	 * 
	 * Should be used for development and testing, not production.
	 * 
	 * @return
	 */
	private void deleteAllKeys() {
		Set<String> keys = jedis.keys("*");
		Transaction t = jedis.multi();
		for (String key: keys) {
			t.del(key);
		}
		t.exec();
	}

	public void reset() {
		this.deleteAllKeys();
		this.deleteTermCounters();
		this.deleteURLSets();
		this.deleteQueryData();
		this.deleteDocData();
	}

	@Override
	public boolean hasQueryData(String query) {
		return jedis.exists(queryKey(query));
	}

	@Override
	public ISearchResult getQueryResult(String query) {
		Map<Document, Double> queryMap = new HashMap<>();
		Set<String> URLs = jedis.hkeys(queryKey(query));
		for(String url : URLs){
			String value = jedis.hget(queryKey(query), url);
			queryMap.put(getDocument(url), Double.parseDouble(value));
		}
		return new SearchResult(queryMap);
	}

	@Override
	public List<Double> getDoc(String url, Set<String> docTerms) {
		List<String> docValues = jedis.lrange(getDocValuesKey(url), 0, jedis.llen(getDocValuesKey(url)));
		List<Double> doc = new ArrayList<>();
		for(String value : docValues){
			doc.add(Double.parseDouble(value));
		}
		return doc;
	}

	@Override
	public Set<String> getMatchingDocURLs(List<String> tokens) {
		Set<String> docURLs = jedis.smembers(this.urlKey());
		Set<String> matches = new HashSet<>();
		for(String url : docURLs){
			boolean match = false;
			for(String token : tokens){
				double count = getCount(url, token);
				if(count > 0){
					match = true;
					break;
				}
			}
			if(match){
				matches.add(url);
				continue;
			}
		}
		return matches;
	}

	@Override
	public String getDocTitle(String url) {
		return jedis.get(titleKey(url));
	}

	@Override
	public String getDocSnippet(String url) {
		return jedis.get(snippetKey(url));
	}

}