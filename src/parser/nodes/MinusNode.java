package parser.nodes;

import index.IIndex;
import parser.EvaluationData;
import search.ISearchResult;

public class MinusNode extends BooleanNode {

	@Override
	public ISearchResult evaluate(EvaluationData myEvaluationController) {
		IIndex index = myEvaluationController.getIndex();
		index.getView().updateStatus("Evaluating AND operator.");
		return myEvaluationController.getLastResult().minus(evaluateChild(0, myEvaluationController));
	}

}