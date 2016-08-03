package parser;

import index.IIndex;
import search.ISearchResult;

public class EvaluationData {

	private ISearchResult last_result;
	private IIndex myIndex;
	
	public EvaluationData(IIndex index){
		this.myIndex = index;
	}
	
	public IIndex getIndex(){
		return this.myIndex;
	}
	
	public ISearchResult getLastResult(){
		return this.last_result;
	}

	public void setLastResult(ISearchResult result) {
		this.last_result = result;
	}
	
}