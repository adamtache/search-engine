package index;

import fetcher.Fetcher;
import fetcher.WikiFetcher;

public class WikiIndex extends Index{

	private WikiFetcher wf;
	
	public WikiIndex(){
		this.wf = new WikiFetcher();
	}
	
	public Fetcher getFetcher() {
		return wf;
	}
	
}