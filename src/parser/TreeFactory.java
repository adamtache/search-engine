package parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import file_loading.ResourceLoader;
import index.IIndex;
import parser.nodes.ListNode;
import parser.nodes.Node;
import parser.nodes.ParenthesisNode;
import parser.nodes.TermNode;

public class TreeFactory {

	private ResourceLoader myResourceLoader;
	private String COMMAND_PATH;
	private String CLASS_EXTENSION;
	private EvaluationData myEvaluationController;

	public TreeFactory(IIndex index){
		try {
			this.myResourceLoader = new ResourceLoader();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.myEvaluationController = new EvaluationData(index);
		this.initResources();
	}

	public List<Node> createRoot(List<String> tokens) {
		myEvaluationController.updateStatus("Creating tree for tokens: "+tokens);
		List<Node> roots = new ArrayList<>();
		while(tokens.size() != 0){
			Node myRoot = createNode(tokens.remove(0));
			createChildren(myRoot, tokens);
			roots.add(myRoot);
		}
		return roots;
	}

	/**
	 * Initializes resources used throughout class
	 */
	private void initResources(){
		COMMAND_PATH = myResourceLoader.getString("CommandPath");
		CLASS_EXTENSION = myResourceLoader.getString("Node");
	}

	private Node createNode(String rootToken) {
		Node node = null;
		rootToken = formatToken(rootToken);
		try {
			node = (Node) Class.forName(COMMAND_PATH + rootToken + CLASS_EXTENSION).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			node = new TermNode(rootToken.toLowerCase());
		}
		return node;
	}

	private String formatToken(String token){
		if(token.equals("-")){
			return "Minus";
		}
		if(token.equals("+") || token.equals("&")){
			return "And";
		}
		if(token.length() == 0){
			return "";
		}
		else if(token.length() == 1){
			return token.toUpperCase();
		}
		return token.substring(0, 1).toUpperCase() + token.substring(1).toLowerCase();
	}

	/**
	 * @param Node myRoot
	 * Create children for myRoot until its current children matches its required children 
	 * @param List<String> tokens - a list of formated command parts
	 */
	private void createChildren(Node myRoot, List<String> tokens) {
		while (myRoot.numCurrentChildren() != myRoot.numRequiredChildren()) {
			Node nextChild = createNextChild(myRoot, tokens);
			if(nextChild != null){
				myRoot.addChild(nextChild);
			}
			else{
				return;
			}
		}
	}

	/**
	 * Create next child in list of tokens
	 * @param List<String> tokens - a list of formated command parts
	 */
	private Node createNextChild(Node myRoot, List<String> tokens) {
		if (tokens.isEmpty()) {
			return null;
		}
		Node child = null;
		String nextToken = tokens.remove(0);
		if (isOpenParenthesis(nextToken)) {
			child = createListNode(nextToken, tokens);
		}
		else {
			child = createNode(nextToken);
			createChildren(child, tokens);
		}
		return child;
	}

	/**
	 * @param String token - token deemed to match open list element by createNextChild method, so a (
	 * @param List<String> tokens - a list of formated command parts
	 */
	private ListNode createListNode(String token, List<String> tokens) {
		ListNode child = null;
		if(isOpenParenthesis(token)){
			child = new ParenthesisNode();
		}
		((ListNode) child).setInnerCommands(createInnerListTokens(tokens));
		return child;
	}

	/**
	 * Creates list of inner tokens for ListNode representing tokens between outermost enclosures
	 * i.e. [ returns tokens inside ]
	 * Inner tokens can be list tokens themselves 
	 * 
	 * @param List<String> tokens
	 */
	private List<String> createInnerListTokens(List<String> commandParts) {
		List<String> innerCommands = new ArrayList<>();
		int openParen = 1;
		int closedParen = 0;
		String currCommand;
		while (openParen != closedParen) {
			currCommand = commandParts.remove(0);
			if (isOpenParenthesis(currCommand)) {
				openParen++;
			}
			else if (isClosedParenthesis(currCommand)) {
				closedParen++;
			}
			if(openParen != closedParen) {
				innerCommands.add(currCommand);
			}
		}
		return innerCommands;
	}

	/**
	 * Determine if the token inputed is an open parenthesis
	 * 
	 * @param String token - string to be compared
	 */
	private boolean isOpenParenthesis(String token) {
		return token.equals("(");
	}

	/**
	 * Determine if the token inputed is a closed parenthesis
	 * 
	 * @param String token - string to be compared
	 */
	private boolean isClosedParenthesis(String token) {
		return token.equals(")");
	}

}