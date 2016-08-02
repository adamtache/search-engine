package parser.nodes;

import parser.EvaluationData;
import search.ISearchResult;

public interface Node {
	
	public abstract ISearchResult evaluate(EvaluationData myEvaluationController);

	public abstract void addChild(Node child);

	public abstract int numCurrentChildren();

	public abstract int numRequiredChildren();

	public abstract void initialize(EvaluationData myEvaluationController);
	
	public ISearchResult getLastResult();
	
}