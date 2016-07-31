package index;

import java.util.Map;

public interface IIndex {

	public abstract Map<String, Integer> getCounts(String term);

}