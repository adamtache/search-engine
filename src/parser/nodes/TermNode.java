package parser.nodes;

import parser.EvaluationData;
import search.ISearchResult;
import search.SearchResult;

public class TermNode extends CommandNode {

	private String myTerm;
	
	public TermNode(String term){
		this.myTerm = term;
	}

	@Override
	public ISearchResult evaluate(EvaluationData myEvaluationController) {
//		myEvaluationController.updateStatus("Evaluating " + myTerm);
		return new SearchResult(myEvaluationController.getIndex().getValues(myTerm));
	}
	
}