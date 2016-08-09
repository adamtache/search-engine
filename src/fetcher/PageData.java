package fetcher;

import org.jsoup.select.Elements;

/**
 * Represents fetched page data. Includes URL, title of page, and page content.
 * 
 */
public class PageData {

	private String title;
	private String url;
	private Elements paras;
	
	public PageData(String url, String title, Elements paras){
		this.url = url;
		this.title = title;
		this.paras = paras;
	}
	
	public Elements getParas(){
		return this.paras;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getURL(){
		return this.url;
	}
	
}