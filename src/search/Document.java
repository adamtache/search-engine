package search;

public class Document {
	
	private String url;
	private String title;
	private String snippet;

	public Document(String url, String title, String snippet){
		this.url = url;
		this.title = title;
		this.snippet = snippet;
	}
	
	public String getURL(){
		return this.url;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getSnippet(){
		return this.snippet;
	}
	
}