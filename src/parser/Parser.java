package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import index.IIndex;

public class Parser {

	private EvaluationData myController;
	private SpellChecker mySpellChecker;

	public Parser(IIndex index){
		this.myController = new EvaluationData(index);
		this.mySpellChecker = new SpellChecker();
	}

	public TokenizedData tokenize(String term) {
		List<String> tokens = Arrays.stream(term.split("\\s+")).map(String::toLowerCase)
				.collect(Collectors.toCollection(ArrayList::new));
//		myController.updateStatus("Split tokens by space.");
		tokens = this.removeRepeatingBooleanOperators(tokens);
//		myController.updateStatus("Filtered repeating boolean operators.");
		tokens = this.separateParenthesis(tokens);
//		myController.updateStatus("Separated parenthesis.");
		List<String> removedPunctuation = new ArrayList<>();
		for(String token : tokens){
			if(!(token.equals("(") || token.equals(")"))){
				removedPunctuation.add(token.replaceAll("\\p{Punct}+", ""));
			}
			else{
				removedPunctuation.add(token);
			}
		}
//		myController.updateStatus("Tokenized: " + term);
		List<String> spellCorrected = this.correctSpelling(tokens);
		return new TokenizedData(tokens, spellCorrected, myController.getIndex());
	}

	private List<String> correctSpelling(List<String> tokens){
		List<String> corrected = new ArrayList<>();
		for(String token : tokens){
			corrected.add(mySpellChecker.getCorrect(token));
		}
		return corrected;
	}

	private List<String> removeRepeatingBooleanOperators(List<String> tokens){
		if(tokens.size() == 0){
			return tokens;
		}
		String lastToken = tokens.get(0);
		for(int x=0; x<tokens.size(); x++){
			String currToken = tokens.get(x);
			List<String> ands = new ArrayList<>(); ands.add("&"); ands.add("and");
			List<String> ors = new ArrayList<>(); ors.add("+"); ors.add("or");
			List<String> nots = new ArrayList<>(); nots.add("not"); nots.add("minus"); nots.add("-");
			boolean repeatingAnds = ands.contains(currToken) && ands.contains(lastToken);
			boolean repeatingOrs = ors.contains(currToken) && ors.contains(lastToken);;
			boolean repeatingNots = nots.contains(currToken) && nots.contains(lastToken);
			if(repeatingAnds || repeatingOrs || repeatingNots){
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
			if(token.equals("")) continue;
			if(token.length() > 0){
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
		}
		return tokens;
	}

	public static boolean isWord(String str){
		return str.matches("[a-zA-Z]+");
	}

}