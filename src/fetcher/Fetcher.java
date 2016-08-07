package fetcher;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public abstract class Fetcher {
	
	private long lastRequestTime = -1;
	private long minInterval = 1000;
	
	public Fetcher(){
	}
	
	public Fetcher(long lastRequestTime, long minInterval){
		this.lastRequestTime = lastRequestTime;
		this.minInterval = minInterval;
	}
	
	public abstract PageData fetch(String url) throws IOException;

	/**
	 * Rate limits by waiting at least the minimum interval between requests.
	 */
	public void sleepIfNeeded() {
		if (lastRequestTime != -1) {
			long currentTime = System.currentTimeMillis();
			long nextRequestTime = lastRequestTime + minInterval;
			if (currentTime < nextRequestTime) {
				try {
					//System.out.println("Sleeping until " + nextRequestTime);
					Thread.sleep(nextRequestTime - currentTime);
				} catch (InterruptedException e) {
					System.err.println("Warning: sleep interrupted in fetchWikipedia.");
				}
			}
		}
		minInterval = System.currentTimeMillis();
	}

	public Document getDocument(String url) {
		Connection conn = Jsoup.connect(url);
		Document doc = null;
		try {
			doc = conn.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public String getTitle(Document doc) {
		return doc.title();
	}
	
}