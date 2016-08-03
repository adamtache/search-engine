package parser.nodes;

import parser.EvaluationData;
import search.ISearchResult;

public class MinusNode extends BooleanNode {

	@Override
	public ISearchResult evaluate(EvaluationData myEvaluationController) {
		return myEvaluationController.getLastResult().minus(evaluateChild(0, myEvaluationController));
	}

}