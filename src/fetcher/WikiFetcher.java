package fetcher;

import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WikiFetcher extends Fetcher{

	/**
	 * Fetches and parses a URL string, returning a list of paragraph elements.
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public PageData fetch(String url) {
		sleepIfNeeded();

		// download and parse the document
		Document doc = super.getDocument(url);

		// select the content text and pull out the paragraphs.
		Element content = doc.getElementById("mw-content-text");

		Elements paras = content.select("p");
		
		return new PageData(url, super.getTitle(doc), paras);
	}
	
}