package search;

import java.util.List;

import index.IIndex;
import parser.TokenizedData;
import parser.TreeEvaluator;
import parser.TreeFactory;
import parser.nodes.Node;

public class ResultsFactory {
	
	public static ISearchResult getSearchResult(TokenizedData tokenizedData){
		List<Node> roots = new TreeFactory(tokenizedData.getIndex()).createRoot(tokenizedData.getTokens());
		ISearchResult data = evaluateRoots(roots, tokenizedData.getIndex());
		data.setTokenizedData(tokenizedData);
		return data;
	}
	
	public static ISearchResult getSpellCorrectedResult(TokenizedData tokenizedData){
		List<Node> roots = new TreeFactory(tokenizedData.getIndex()).createRoot(tokenizedData.getSpellCorrected());
		ISearchResult data = evaluateRoots(roots, tokenizedData.getIndex());
		data.setTokenizedData(tokenizedData);
		return data;
	}
	
	private static ISearchResult evaluateRoots(List<Node> roots, IIndex index){
		return new TreeEvaluator().evaluateRoots(roots, index);
	}
	
}