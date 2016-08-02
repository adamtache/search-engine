package parser;

import java.util.List;

import index.IIndex;
import parser.nodes.Node;
import search.ISearchResult;

public class TreeEvaluator {

	public ISearchResult evaluateRoots(List<Node> roots, IIndex index){
		EvaluationData ec = new EvaluationData(index);
		ISearchResult result = null;
		for(Node root : roots){
			result = root.evaluate(ec);
			ec.setLastResult(result);
		}
		return result;
	}
	
}