package controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import model.crawler.WikiCrawler;
import model.index.Index;
import model.index.WikiIndex;

public class Main {

	public static void main(String[] args) throws IOException{
		printTFIDF();
	}

	public static void printTFIDF() throws IOException{
		Index indexer = new WikiIndex();
		IndexController ic = new IndexController(indexer);
		List<String> urls = new WikiCrawler().getCrawled();
		System.out.println("URLS: " + urls);
		ic.indexUrls(urls);
		indexer.printIndex();
		Set<String> terms = indexer.keySet();
		for(String term : terms){
			for(String url : urls){
				System.out.println("TF-IDF of term: " + term + " for URL: " + url +" is: " + ic.tfIdf(term, url));
			}
		}
	}

}