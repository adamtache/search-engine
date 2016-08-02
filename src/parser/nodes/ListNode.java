package parser.nodes;
import java.util.List;

import index.IIndex;
import parser.EvaluationData;
import parser.Parser;
import search.ISearchResult;

/**
 * Node representation of a List of commands inside of ( ).
 * Extends CommandNode and can have unlimited commands inside.
 */
public class ListNode extends CommandNode {

	private List<String> myInnerTokens;

	public void setInnerCommands(List<String> tokens){
		this.myInnerTokens = tokens;
	}

	public List<String> getInnerTokens(){
		return myInnerTokens;
	}
	
	/**
	 * @return list of evaluations of commands inside ListNode
	 */
	public List<ISearchResult> getEvaluations(IIndex index) {
		return new Parser(index).evaluateTokens(clone(myInnerTokens));
	}

	/**
	 * @return Evaluation of last command in list
	 */
	public ISearchResult evaluate(EvaluationData myEvaluationController) {
		List<ISearchResult> evaluations = getEvaluations(myEvaluationController.getIndex());
		return evaluations.get(evaluations.size() - 1);
	}

}