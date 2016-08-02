package search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class SearchData implements ISearchData {

	private PriorityQueue<Entry<String, Double>> data;
	
	public SearchData(PriorityQueue<Entry<String, Double>> data){
		this.data = data;
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public String get(int result) {
		List<Entry<String, Double>> temp = new ArrayList<>();
		for(int x=0; x<result; x++){
			temp.add(data.poll());
		}
		Entry<String, Double> resultEntry = data.poll();
		temp.add(resultEntry);
		data.addAll(temp);
		return resultEntry.getKey();
	}
	
	@Override
	public void print(){
		List<Entry<String,Double>> entries = getEntries();
		for(Entry<String,Double> dataEntry: entries){
			System.out.println("Visited: " + dataEntry.getKey()+" TF-IDF: " + dataEntry.getValue());
		}
	}
	
	public List<Entry<String,Double>> getEntries(){
		List<Entry<String, Double>> temp = new ArrayList<>();
		for(int x=0; x <= data.size(); x++){
			Entry<String, Double> dataEntry = data.poll();
			temp.add(dataEntry);
		}
		data.addAll(temp);
		return temp;
	}
	
}