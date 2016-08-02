package search;

import java.util.List;
import java.util.Map.Entry;


public interface ISearchData {

	public abstract int size();

	public abstract String get(int result);

	public abstract void print();

	public abstract List<Entry<String, Double>> getEntries();

}