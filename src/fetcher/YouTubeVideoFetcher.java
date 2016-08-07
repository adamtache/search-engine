package fetcher;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class YouTubeVideoFetcher extends Fetcher{

	private final String timed_text = "http://video.google.com/timedtext?lang=en&v=";

	@Override
	public YouTubeData fetch(String url) throws IOException {
		sleepIfNeeded();
		System.out.println("FETCHING: " + url);
		String video_id = url.substring(url.indexOf("?v=")+3);
		String crawl_url = timed_text + video_id; // Crawl Google's timed text page
		System.out.println("CRAWL URL: " + crawl_url);
		Document timedTextDoc = super.getDocument(crawl_url);

		Elements paras = null;
		if(timedTextDoc == null){ // Timed text content not available for this page.
			// select the transcript content and grab text from timed text page.
			Elements content = timedTextDoc.getElementsByAttribute("transcript");
			paras = content.select("text");
		}
		
		Document youtubeDoc = super.getDocument(url);
		Elements youtubeParas = youtubeDoc.getElementsByClass("content-wrapper");

		return new YouTubeData(url, super.getTitle(youtubeDoc), paras, youtubeParas);
	}

}