package search;
import java.util.Comparator;
import java.util.Map.Entry;

public class RelevanceComparator implements Comparator<Entry<Document, Double>> {
	
	@Override
	public int compare(Entry<Document, Double> e1, Entry<Document, Double> e2) {
		return e2.getValue().compareTo(e1.getValue());
	}
	
}