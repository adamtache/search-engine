package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import index.IIndex;
import parser.nodes.Node;
import search.Document;
import search.ISearchResult;
import search.SearchResult;

/**
 * This class is used to evaluate abstract syntax trees of roots. Used for evaluation of boolean and grouped commands.
 * 
 */
public class TreeEvaluator {

	public ISearchResult evaluateRoots(List<Node> roots, IIndex index){
		EvaluationData ec = new EvaluationData(index);
		ISearchResult result = new SearchResult(new HashMap<Document, Double>());
		for(Node root : roots){
			result = root.evaluate(ec);
			ec.setLastResult(result);
		}
		return result;
	}
	
	public List<ISearchResult> evaluateTokens(List<String> tokens, IIndex index) {
		List<ISearchResult> evaluations = new ArrayList<>();
		while (!tokens.isEmpty()) {
			List<Node> myRoots = new TreeFactory(index).createRoot(tokens);
			evaluations.add(executeRoots(myRoots, index));
		}
		return evaluations;
	}

	private ISearchResult executeRoots(List<Node> roots, IIndex index) {
		return new TreeEvaluator().evaluateRoots(roots, index);
	}
	
}