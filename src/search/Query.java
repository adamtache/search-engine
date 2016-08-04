package search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Query {
	
	private Map<String, Double> queryMap;

	public Query(Map<String, Double> queryMap){
		this.queryMap = queryMap;
	}
	
	public Set<String> getVocabulary(){
		return queryMap.keySet();
	}
	
	public List<Double> getQueryVector(){
		return new ArrayList<>(queryMap.values());
	}
	
}