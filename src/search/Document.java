package search;

/**
 * Represents a search document, with a URL, title, and snippet for displaying in search results.
 * 
 */
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
	
	public String toString(){
		return title+" "+url;
	}
	
	@Override
	public boolean equals(Object o){
		return ((Document) o).getURL().toLowerCase().equals(this.getURL().toLowerCase());
	}
	
	@Override
	public int hashCode(){
		return 2;
	}
	
}