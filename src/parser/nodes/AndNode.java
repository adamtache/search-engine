package parser.nodes;

import parser.EvaluationData;
import search.ISearchResult;

public class AndNode extends BooleanNode {

	@Override
	public ISearchResult evaluate(EvaluationData myEvaluationController) {
		return myEvaluationController.getLastResult().and(evaluateChild(0, myEvaluationController));
	}

}