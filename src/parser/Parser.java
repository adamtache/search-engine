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
		return Arrays.stream(term.split("\\s+")).map(String::toLowerCase)
				.collect(Collectors.toCollection(ArrayList::new));
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
		ISearchResult result = null;
		for(Node root : roots){
			result = root.evaluate(myController);
		}
		return result;
	}

}