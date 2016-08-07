package fetcher;

import org.jsoup.select.Elements;

public class YouTubeData extends PageData {
	
	private Elements youtubeParagraphs;

	public YouTubeData(String url, String title, Elements captions_paras, Elements youtubeParagraphs) {
		super(url, title, captions_paras);
		this.youtubeParagraphs = youtubeParagraphs;
	}

	public Elements getYouTubeParas(){
		return this.youtubeParagraphs;
	}
	
}