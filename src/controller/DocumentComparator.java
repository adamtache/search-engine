package controller;

import java.util.Comparator;

import index.Document;

public class DocumentComparator implements Comparator<Document>{
	
	private String search;
	
	public DocumentComparator(String search){
		this.search = search;
	}
	
	@Override
	public int compare(Document d1, Document d2){
		return (int) (d1.get(search) - d2.get(search));
	}
	
}