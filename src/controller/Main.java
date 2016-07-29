package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.index.Index;
import model.index.WikiIndex;

public class Main {

	public static void main(String[] args) throws IOException{
		Index indexer = new WikiIndex();
		IndexController id = new IndexController(indexer);
		String url1 = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		String url2 = "https://en.wikipedia.org/wiki/Programming_language";
		List<String> urls = new ArrayList<>();
		urls.add(url1); urls.add(url2);
		id.indexUrls(urls);
		indexer.printIndex();
		System.out.println("TFIDF: " + id.tfIdf("wora"));
	}
	
}