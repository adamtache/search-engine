package search;

import java.io.IOException;

import index.IIndex;
import parser.Parser;
import view.IView;

public class Searcher implements ISearcher {

	private IView myView;
	private Parser myParser;

	public Searcher(IView view, IIndex index) throws IOException {
		this.myView = view;
		this.myParser = new Parser(index);
	}

	public ISearchResult getResults(String term) {
		myView.updateStatus("Searcher telling index to create TF-Idf data.");
		return ResultsFactory.getSearchResult(myParser.tokenize(term));
	}

	public void search(String term) throws IOException {
		// TODO Auto-generated method stub
		
	}

}