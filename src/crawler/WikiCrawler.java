package crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import fetcher.WikiFetcher;
import org.jsoup.nodes.Element;

public class WikiCrawler {

	final static WikiFetcher wf = new WikiFetcher();
	
	public List<String> getCrawled() throws IOException{
		String random = this.getRandomURL();
		String start = getRandomStart();
		random = this.getRandomURL();
		String goal = random.substring(random.indexOf("/wiki/"));
		return this.getCrawled(start, goal);
	}

	/**
	 * Gets list of crawled Wikipedia pages from a starting page to a goal page.
	 * Start and goal are both user generated.
	 *
	 * @throws IOException
	 */
	public List<String> getCrawled(String url, String goal) throws IOException{
		List<String> crawled = new ArrayList<>();
		crawled.add(url);
		boolean foundLink = false;
		boolean end = false;
		while(!end){
			System.out.println("Visited " + url);
			Elements paras = wf.fetch("https://en.wikipedia.org" + url);
			for(Element para : paras){ // Loop through paragraphs until one found with valid link
				Iterable<Node> iter = new NodeIterable(para);
				for (Node node: iter) {
					int parenCount = 0;
					if (node instanceof TextNode) {
						for(int y=0; y<((TextNode) node).text().length(); y++){
							if(((TextNode) node).text().charAt(y) == '('){
								parenCount++;
							}
							else if(((TextNode) node).text().charAt(y) == ')'){
								parenCount--;
							}
						}
					}
					if(parenCount != 0){ // Link not valid if inside parenthesis
						continue;
					}
					if(node instanceof Element) {
						Element link = (Element) node;
						if(link.tagName().equals("a")){ // Check for link tag
							Elements parents = link.parents();
							for(Element parent : parents){ // Link not valid if italicized (parent with <i> or <em>)
								if(parent.tagName().equals("i")){
									continue;
								}
								else if(parent.tagName().equals("em")){
									continue;
								}
							}
							String linkText = link.text();
							if(linkText.length() > 0 && Character.isUpperCase(linkText.charAt(0))){ // Link not valid if capitalized text
								continue;
							}
							String next = link.attr("href");
							if(crawled.contains(next)){ // Skip already visited link
								System.out.println("Already visited " + next + ". Exiting.");
								end = true;
								break;
							}
							if(!next.contains("/wiki")){ // Skip external link
								continue;
							}
							String urlPage = url.substring(url.indexOf("/wiki/"));
							if(next.equals(urlPage)){ // Skip link to current page
								System.out.println("Valid link is to current page. Exiting.");
								end = true;
								break;
							}
							else{ // "Next" is a valid link: assign to "url" for next paragraphs iteration
								url = next;
							}
							if(next.equals(goal)){ // Check for goal
								System.out.println("Congratulations. " + goal+" was found.");
								crawled.add(url);
								return crawled;
							}
							if(!(url.indexOf("wiki") == 1)){
								System.out.println("Invalid link found. Exiting.");
								end = true;
							}
							crawled.add(url);
							foundLink = true;
							break;
						}
					}
				}
				if(foundLink || end){
					// If link found on this page, break out of loop
					// Or if at end (infinite loop), break out of loop, which will then also break out of the while loop
					break;
				}
			}
			if(!foundLink){
				System.out.println("Valid link not found on entire page. Exiting.");
				end = true;
				break;
			}
			foundLink = false;
		}
		System.out.println("No success. Didn't reach " + goal);
		return crawled;
	}

	/**
	 * Gets random Wikipedia page URL.
	 * 
	 * @return String, random page URL
	 */
	public String getRandomURL(){
		return "https://en.wikipedia.org/wiki/Special:Random";
	}
	
	public String getRandomStart(){
		String random = this.getRandomURL();
		return random.substring(random.indexOf("/wiki/"));
	}

}