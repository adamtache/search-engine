package index;

import java.util.Map;
import java.util.Set;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import fetcher.PageData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import search.Document;
import search.ISearchResult;
import search.SearchResult;
import view.IView;

/**
 * Represents a Redis-backed web search index.
 * Has the ability to store multiple "types" of search results due to abstract key methods.
 * i.e. "WebIndex" and "YouTubeIndex" both extend JedisIndex and offer custom key lookups to separate data.
 * 
 */
public abstract class JedisIndex implements IIndex {

	protected Jedis jedis;
	private IView myView;

	/**
	 * Constructor.
	 * 
	 * @param jedis
	 */
	public JedisIndex(Jedis jedis, IView view) {
		this.jedis = jedis;
		myView = view;
	}

	public abstract String urlSetKey(String term);
	public abstract String getValueKey(String url, String term);
	public abstract String getDocKey(String url);
	public abstract String getDocValuesKey(String url);
	public abstract String queryKey(String query);
	public abstract String urlKey();
	public abstract String termCounterKey(String url);
	public abstract void deleteQueryData();
	public abstract void deleteDocData();
	public abstract Set<String> urlSetKeys();
	public abstract String corpusTermsKey();
	public abstract Set<String> termCounterKeys();

	public Long numberIndexedPages(){
		return jedis.scard(urlKey());
	}

	public Set<String> indexedPages(){
		return jedis.smembers(urlKey());
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

	private Response<String> getCount(String url, String term, Transaction t) {
		String redisKey = termCounterKey(url);
		return t.hget(redisKey, term);
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
			t.sadd(corpusTermsKey(), term); // Add term to set of corpus terms.
		}
		List<Object> res = t.exec();
		return res;
	}

	@Override
	public Set<String> getDocURLs(){
		return jedis.smembers(urlKey());
	}

	@Override
	public Set<String> getCorpusTerms(){
		return jedis.smembers(corpusTermsKey());
	}

	@Override
	public void addDocumentsToDB() {
		deleteDocData();
		Set<String> docURLs = getDocURLs();
		Set<String> corpusTerms = getCorpusTerms();
		for(String url : docURLs){
			System.out.println("URL: " + url);
			Map<String, Response<String>> tfResponseMap = new HashMap<>();
			Map<String, Response<Long>> docFreqResponseMap = new HashMap<>();
			Long numDocuments = numberIndexedPages();
			Transaction t = jedis.multi();
			for(String term : corpusTerms){
				Response<String> tf = getCount(url, term, t);
				tfResponseMap.put(term, tf);
				Response<Long> docFreq = getDocFrequency(term, t);
				docFreqResponseMap.put(term, docFreq);
			}
			t.exec();
			t = jedis.multi();
			Map<String, Long> tfMap = strResponseToLong(tfResponseMap);
			Map<String, Long> docFreqMap = longResponseToLong(docFreqResponseMap);
			Map<String, Double> document = this.getDocument(numDocuments, tfMap, docFreqMap);
			for(String term : document.keySet()){
				double tfIdf = document.get(term);
				String tfIdfStr = tfIdf + "";
				t.set(getValueKey(url, term), tfIdfStr);
				t.hset(getDocKey(url), term, tfIdfStr); // Adds term and TF-IDF to URL.
				t.rpush(getDocValuesKey(url), tfIdfStr); // Adds TF-IDF to document value list.
			}
			t.exec();
		}
	}

	private Map<String, Long> strResponseToLong(Map<String, Response<String>> responseMap){
		Map<String, Long> longMap = new HashMap<>();
		for(String key : responseMap.keySet()){
			String val = responseMap.get(key).get();
			if(val != null)
				longMap.put(key, Long.parseLong(val));
		}
		return longMap;
	}

	private Map<String, Long> longResponseToLong(Map<String, Response<Long>> responseMap){
		Map<String, Long> longMap = new HashMap<>();
		for(String key : responseMap.keySet()){
			longMap.put(key, responseMap.get(key).get());
		}
		return longMap;
	}

	private Map<String, Double> getDocument(Long numDocuments, Map<String, Long> tfMap, Map<String, Long> docFreqMap){
		Map<String, Double> document = new HashMap<>();
		for(String term : tfMap.keySet()){
			document.put(term, tfIdf(term, tfMap, docFreqMap, numDocuments));
		}
		return document;
	}

	private Double tfIdf(String term, Map<String, Long> tfMap, Map<String, Long> docFreqMap, Long numDocuments){
		Long tf = tfMap.get(term);
		if(tf > 0){
			tf = tf + 1;
		}
		Long docFreq = docFreqMap.get(term);
		double idf = Math.log((double) numDocuments/docFreq);
		return tf*idf;
	}

	private Double tfIdf(String url, String term){
		double tf = getCount(url, term);
		if(tf == 0){
			return 0.0;
		}
		tf = tf + 1.0;
		myView.updateStatus("Index determined TF to be: "+tf+".");
		return tf * idf(term);
	}

	private Response<Long> getDocFrequency(String term, Transaction t){
		return t.scard(urlSetKey(term));
	}

	private Double idf(String term){
		Long numDocuments = numberIndexedPages();
		Long documentFrequency = jedis.scard(urlSetKey(term));
		return Math.log((double) numDocuments/documentFrequency);
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
			double tfIdf = tfIdf(url, term);
			myView.updateStatus("TFIDF: " + tfIdf);
			results.put(getDocument(url), tfIdf);
		}
		return results;
	}

	private Document getDocument(String url){
		return new Document(url, getDocTitle(url), getDocSnippet(url));
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
			jedis.hset(queryKey(query), url, results.get(url)+"");
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

	public void reset() {
		deleteAllKeys();
		deleteTermCounters();
		deleteURLSets();
		deleteQueryData();
		deleteDocData();
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
			Document doc = getDocument(url);
			if(value != null && !value.equals("null"))
				queryMap.put(doc, Double.parseDouble(value));
			else{
				queryMap.put(doc, 0.0);
			}
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
		Set<String> docURLs = jedis.smembers(urlKey());
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