package search;
import java.util.Comparator;
import java.util.Map.Entry;

public class ResultsComparator implements Comparator<Entry<String, Double>> {
	
	@Override
	public int compare(Entry<String, Double> e1, Entry<String, Double> e2) {
		return e1.getValue().compareTo(e2.getValue());
	}
	
}