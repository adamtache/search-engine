package search;

import java.util.Map.Entry;

public interface ISearchData {

	public abstract int size();

	public abstract Entry<String, Double> poll();

	public abstract String get(int result);

	public abstract void print();

}