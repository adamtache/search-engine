package model.crawler;

import java.io.IOException;
import java.util.List;

/**
 * A web crawler uses a web fetcher to grab information about a website in order to index.
 * 
 * @author downey
 *
 */

public abstract class Crawler {

	public abstract List<String> getCrawled() throws IOException;
	
}