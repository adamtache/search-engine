package fetcher;

import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Web fetcher for YouTube videos. Fetches YouTube closed captions using video.google.com's "timed text" feature.
 * Also fetches video description and keywords from YouTube meta data.
 * 
 */
public class YouTubeVideoFetcher extends Fetcher{

	private final String timed_text = "http://video.google.com/timedtext?lang=en&v=";

	@Override
	public YouTubeData fetch(String url) throws IOException {
		sleepIfNeeded();
		String video_id = url.substring(url.indexOf("?v=")+3);
		String crawl_url = timed_text + video_id; // Crawl Google's timed text page
		Document timedTextDoc = super.getDocument(crawl_url);
		Elements content = new Elements();
		if(timedTextDoc != null){ // Null if timed text content not available for this page (YouTube doesn't have closed captions).
			// select the transcript content and grab text from timed text page.
			content = timedTextDoc.select("text");
		}

		Document youtubeDoc = super.getDocument(url);
		Elements youtubeParas = youtubeDoc.getElementsByClass("content-wrapper");

		Elements youtubeDescription = youtubeDoc.getElementsByTag("meta");
		Elements youtubeKeywords = youtubeDescription.select("meta[name*=keywords]");
		youtubeDescription = youtubeDescription.select("meta[name*=description]");
		if(youtubeDescription != null)
			content.addAll(youtubeDescription);
		if(youtubeKeywords != null)
			content.addAll(youtubeKeywords);

		return new YouTubeData(url, super.getTitle(youtubeDoc), content, youtubeParas);
	}

}