package crawler;

import java.io.IOException;

import org.jsoup.select.Elements;

import index.IIndex;

public interface Crawler {
	
	public void crawl() throws IOException;
	
	public void queueInternalLinks(Elements paragraphs);
	
	public default void updateDB(){
		getIndex().deleteQueryData();
	}

	public IIndex getIndex();
	
}