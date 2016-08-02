package parser.nodes;

import index.IIndex;
import parser.EvaluationData;
import search.ISearchResult;

public class AndNode extends BooleanNode {

	@Override
	public ISearchResult evaluate(EvaluationData myEvaluationController) {
		IIndex index = myEvaluationController.getIndex();
		index.getView().updateStatus("Evaluating AND operator.");
		return myEvaluationController.getLastResult().and(evaluateChild(0, myEvaluationController));
	}

}