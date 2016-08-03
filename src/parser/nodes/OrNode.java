package parser.nodes;

import parser.EvaluationData;
import search.ISearchResult;

public class OrNode extends BooleanNode {

	@Override
	public ISearchResult evaluate(EvaluationData myEvaluationController) {
		return myEvaluationController.getLastResult().or(evaluateChild(0, myEvaluationController));
	}

}