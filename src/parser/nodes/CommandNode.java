package parser.nodes;

import java.util.ArrayList;
import java.util.List;

import index.IIndex;
import parser.EvaluationData;
import parser.Parser;
import search.ISearchResult;

public abstract class CommandNode implements Node {

    private ArrayList<Node> myChildren;
    private int myNumChildren;
    private EvaluationData myEvaluationController;

    public CommandNode(){
        myChildren = new ArrayList<>();
    }
    
    public void initialize(EvaluationData evaluationController){
    	this.myEvaluationController = evaluationController;
    }
    
    public ISearchResult getLastResult(){
    	return this.myEvaluationController.getLastResult();
    }
    
    public IIndex getIndex(){
    	return this.myEvaluationController.getIndex();
    }

    public ArrayList<Node> getChildren() {
        return myChildren;
    }

    public void addChild(Node child) {
        myChildren.add(child);
    }
    
    public Parser getParser(){
    	return new Parser(this.myEvaluationController.getIndex());
    }

    /**
     * @return Number of children required for command
     */
    public int numRequiredChildren(){
        return myNumChildren;
    }

    /**
     * @return Number of children currently added to command
     */
    public int numCurrentChildren(){
        return myChildren.size();
    }

    public void setNumChildren(int num){
        myNumChildren = num;
    }

    public ISearchResult evaluateChild(int child, EvaluationData myController)  {
        return myChildren.get(child).evaluate(myController);
    }
    
    public Node getChild(int childNum){
    	return myChildren.get(childNum);
    }
    
    public List<String> clone(List<String> list){
        List<String> copy = new ArrayList<>();
        copy.addAll(list);
        return copy;
    }

}