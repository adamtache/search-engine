package parser.nodes;

import index.IIndex;
import parser.EvaluationData;
import search.ISearchResult;

public class OrNode extends BooleanNode {

	@Override
	public ISearchResult evaluate(EvaluationData myEvaluationController) {
		IIndex index = myEvaluationController.getIndex();
		index.getView().updateStatus("Evaluating OR operator.");
		return myEvaluationController.getLastResult().or(evaluateChild(0, myEvaluationController));
	}

}