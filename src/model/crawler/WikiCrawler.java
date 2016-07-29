package model.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import model.WikiNodeIterable;
import model.fetcher.WikiFetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class WikiCrawler extends Crawler{

	final static WikiFetcher wf = new WikiFetcher();

	/**
	 * Gets list of crawled Wikipedia pages from a starting page to a goal page.
	 * Start and goal are both user generated.
	 *
	 * @throws IOException
	 */
	public List<String> getCrawled() throws IOException{
		String goal = this.getGoalURL();
		List<String> crawled = new ArrayList<>();
		String url = this.getStartURL();
		crawled.add(url);
		boolean foundLink = false;
		boolean end = false;
		while(!end){
			System.out.println("Visited " + url);
			Elements paras = wf.fetch("https://en.wikipedia.org" + url);
			for(Element para : paras){ // Loop through paragraphs until one found with valid link
				Iterable<Node> iter = new WikiNodeIterable(para);
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
	 * Gets starting URL for crawl. 
	 * First asks user if he/she wants to start at a random article using Wikipedia's random page feature.
	 * Then asks if he/she wants to start at the Java programming language page.
	 * If not, then user has ability to enter a custom URL.
	 *
	 * @throws IOException
	 */
	private String getStartURL() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Want to start at a random article? Yes or no.");
		String random = br.readLine().toLowerCase();
		String url;
		if(random.contains("y")){
			url = Jsoup.connect(getRandomURL()).get().location();
		}
		else{
			System.out.println("Want to start at the Java (Programming language) page?");
			random = br.readLine().toLowerCase();
			if(random.contains("y")){
				url = Jsoup.connect(getJavaURL()).get().location();
			}
			else{
				System.out.println("Please enter a Wikipedia URL.");
				url = br.readLine();
			}
		}
		return url.substring(url.indexOf("/wiki/"));
	}

	/**
	 * Gets goal URL for crawl. 
	 * First asks user if he/she wants to crawl for the Philosophy article.
	 * If not, user can enter a custom URL.
	 *
	 * @throws IOException
	 */
	private String getGoalURL() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Welcome to a Wikipedia crawler.\nWant to crawl for the Philosophy article? Yes or no.");
		String random = br.readLine().toLowerCase();
		String url;
		if(random.contains("y")){
			url = Jsoup.connect(getPhilosophyURL()).get().location();
		}
		else{
			System.out.println("Please enter a Wikipedia URL.");
			url = br.readLine();
		}
		return url.substring(url.indexOf("/wiki/"));
	}
	
	/**
	 * Gets philosophy Wikipedia page URL.
	 * 
	 * @return String, philosophy page URL
	 */
	private String getPhilosophyURL(){
		return "https://en.wikipedia.org/wiki/Philosophy";
	}

	/**
	 * Gets random Wikipedia page URL.
	 * 
	 * @return String, random page URL
	 */
	private String getRandomURL(){
		return "https://en.wikipedia.org/wiki/Special:Random";
	}

	/**
	 * Gets Java programming language Wikipedia page URL.
	 * 
	 * @return String, Java programming language page URL
	 */
	private String getJavaURL(){
		return "https://en.wikipedia.org/wiki/Java_(programming_language)";
	}

}