package search;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Document {

	private String url;
	public Map<String, Double> termTfids;
	
	public Document(String url){
		this.url = url;
		this.termTfids = new HashMap<>();
	}
	
	public void addTerm(String term, double tfidf){
		this.termTfids.put(term, tfidf);
	}
	
	public Set<String> getKeys(){
		return this.termTfids.keySet();
	}
	
	public double get(String term){
		Double lookup = termTfids.get(term);
		return lookup == null ? 0.0 : lookup;
	}
	
	public String getUrl(){
		return this.url;
	}
	
}