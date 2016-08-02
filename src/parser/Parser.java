package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import index.IIndex;
import parser.nodes.Node;
import search.ISearchResult;

public class Parser {

	private TreeFactory myTreeFactory;
	private EvaluationData myController;

	public Parser(IIndex index){
		this.myTreeFactory = new TreeFactory(index);
		this.myController = new EvaluationData(index);
	}

	public List<String> tokenize(String term) {
		myController.updateStatus("Split tokens by space.");
		List<String> tokens = Arrays.stream(term.split("\\s+")).map(String::toLowerCase)
				.collect(Collectors.toCollection(ArrayList::new));
		tokens = this.removeRepeatingBooleanOperators(tokens);
		myController.updateStatus("Checked for repeating boolean operators.");
		tokens = this.separateParenthesis(tokens);
		myController.updateStatus("Separated parenthesis.");
		List<String> removedPunctuation = new ArrayList<>();
		for(String token : tokens){
			if(!(token.equals("(") || token.equals(")"))){
				removedPunctuation.add(token.replaceAll("\\p{Punct}+", ""));
			}
			else{
				removedPunctuation.add(token);
			}
		}
		myController.updateStatus("Tokenized: " + term);
		return tokens;
	}

	private List<String> removeRepeatingBooleanOperators(List<String> tokens){
		if(tokens.size() == 0){
			return tokens;
		}
		String lastToken = tokens.get(0);
		for(int x=0; x<tokens.size(); x++){
			boolean repeatingAnds = lastToken.equals("and") && tokens.get(x).equals("and");
			boolean repeatingOrs = lastToken.equals("or") && tokens.get(x).equals("or");
			boolean repeatingMinus = lastToken.equals("-") && tokens.get(x).equals("-");
			boolean repeatingMinus2 = lastToken.equals("minus") && tokens.get(x).equals("-");
			boolean repeatingMinus3 = lastToken.equals("-") && tokens.get(x).equals("minus");
			boolean repeatingMinus4 = lastToken.equals("minus") && tokens.get(x).equals("minus");
			boolean repeatingPlus = lastToken.equals("+") && tokens.get(x).equals("+");
			if(repeatingAnds || repeatingOrs || repeatingMinus || repeatingPlus || repeatingMinus2 || repeatingMinus3 || repeatingMinus4){
				tokens.remove(x);
				x--;
			}
			else{
				lastToken = tokens.get(x);
			}
		}
		return tokens;
	}

	private List<String> separateParenthesis(List<String> tokens){
		for(int x=0; x<tokens.size(); x++){
			String token = tokens.get(x);
			if(token.charAt(0) == '('){
				if(token.length() > 1){
					tokens.set(x, token.substring(1));
					tokens.add(x, "(");
					x++;
				}
			}
			if(token.charAt(token.length() - 1) == ')'){
				if(token.length() > 1){
					tokens.set(x, token.substring(0, token.length() - 1));
					tokens.add(x, ")");
					x++;
				}
			}
		}
		return tokens;
	}

	public List<ISearchResult> evaluateTokens(List<String> tokens) {
		List<ISearchResult> evaluations = new ArrayList<>();
		while (!tokens.isEmpty()) {
			List<Node> myRoots = myTreeFactory.createRoot(tokens);
			evaluations.add(executeRoots(myRoots));
		}
		return evaluations;
	}

	private ISearchResult executeRoots(List<Node> roots) {
		return new TreeEvaluator().evaluateRoots(roots, myController.getIndex());
	}

}