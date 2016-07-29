package controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import index.Index;

public class Main {

	public static void main(String[] args) throws IOException{
		SearchController sc = new SearchController();
		sc.search();
	}

	public static void printTFIDF(IndexController ic, Index indexer, List<String> urls) throws IOException{
		Set<String> terms = indexer.keySet();
		for(String term : terms){
			for(String url : urls){
				System.out.println("TF-IDF of term: " + term + " for URL: " + url +" is: " + ic.tfIdf(term, url));
				System.out.println("Normalized TF-IDF of term: " + term + " for URL: " + url +" is: " + ic.normalizedTfIdf(term, url));
			}
		}
	}

}