package search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import index.IIndex;
import parser.Parser;
import parser.TokenizedData;
import parser.TreeEvaluator;
import parser.TreeFactory;
import parser.nodes.Node;

public class ResultsFactory {

	public static ISearchResult getSearchResult(TokenizedData tokenizedData){
		List<String> tokens = tokenizedData.getTokens();
		if(isVectorSpaceModel(tokens)){
			return getVectorModelData(tokens, tokenizedData.getIndex());
		}
		// Boolean model
		List<Node> roots = new TreeFactory(tokenizedData.getIndex()).createRoot(tokens);
		return getData(roots, tokenizedData);
	}

	public static ISearchResult getSpellCorrectedResult(TokenizedData tokenizedData){
		List<String> correctedTokens = tokenizedData.getSpellCorrected();
		if(isVectorSpaceModel(correctedTokens)){
			return getVectorModelData(correctedTokens, tokenizedData.getIndex());
		}
		// Boolean model
		List<Node> correctedRoots = new TreeFactory(tokenizedData.getIndex()).createRoot(correctedTokens);
		return getData(correctedRoots, tokenizedData);
	}

	private static ISearchResult getVectorModelData(List<String> tokens, IIndex index){
		tokens = getOnlyTerms(tokens);
		Set<String> docTerms = index.getDocTerms();
		List<Double> queryVector = getQueryVector(docTerms, tokens);
		Map<String, Double> cosSimMap = getCosSimMap(tokens, docTerms, queryVector, index);
		return new SearchResult(cosSimMap);
	}

	private static List<String> getOnlyTerms(List<String> tokens){
		List<String> terms = new ArrayList<>();
		for(int x=0; x<tokens.size(); x++){
			String token = tokens.get(x);
			if(Parser.isWord(token))
				terms.add(token);
		}
		return terms;
	}

	private static Map<String, Double> getCosSimMap(List<String> tokens, Set<String> docTerms, List<Double> queryVector, IIndex index){
		Map<String, Double> cosSimMap = new HashMap<>();
		Set<String> URLs = index.getMatchingDocURLs(tokens);
		for(String url : URLs){
			List<Double> doc = index.getDoc(url, docTerms);
			double similarityValue = calculateSimilarity(doc, queryVector);
			cosSimMap.put(url, similarityValue);
		}
		return cosSimMap;
	}

	private static double calculateSimilarity(List<Double> document, List<Double> queryVector){
		double docLength = getLength(document);
		if(docLength == 0){
			return 0;
		}
		double queryLength = getLength(queryVector);
		if(queryLength == 0){
			return 0;
		}
		return getDotProduct(document, queryVector)/(docLength * queryLength);
	}

	private static double getDotProduct(List<Double> vector1, List<Double> vector2){
		double dotProduct = 0;
		for(int x=0; x<vector1.size(); x++){
			dotProduct += vector1.get(x) * vector2.get(x);
		}
		return dotProduct;
	}

	private static Double getLength(List<Double> vector){
		double length = 0;
		for(Double value : vector){
			length += value * value;
		}
		return Math.sqrt(length);
	}

	private static List<Double> getQueryVector(Set<String> docTerms, List<String> tokens){
		List<Double> queryVector = new ArrayList<>();
		double maxTf = 0;
		for(String term : docTerms){
			double tf = 0;
			for(String token : tokens){
				if(token.toLowerCase().equals(term.toLowerCase())){
					tf++;
				}
			}
			queryVector.add(tf);
			if(tf > maxTf){
				maxTf = tf;
			}
		}
		for(int x=0; x<queryVector.size(); x++){
			double tf = queryVector.get(x);
			if(tf != 0)
				queryVector.set(x, Math.log(tokens.size() / tf) * tf / maxTf);
		}
		return queryVector;
	}

	private static boolean isVectorSpaceModel(List<String> tokens){
		if(tokens.size() < 2){
			return false;
		}
		for(String token : tokens){
			if(isBooleanOperator(token)){
				return false;
			}
		}
		return true;
	}

	private static boolean isBooleanOperator(String token){
		token = token.toLowerCase();
		String[] booleanOperators = {"and", "or", "not", "minus", "-", "&"};
		for(String bool : booleanOperators){
			if(bool.equals(token.toLowerCase())){
				return true;
			}
		}
		return false;
	}

	private static ISearchResult getData(List<Node> roots, TokenizedData tokenizedData){
		ISearchResult data = evaluateRoots(roots, tokenizedData.getIndex());
		if(data != null)
			data.setTokenizedData(tokenizedData);
		return data;
	}

	private static ISearchResult evaluateRoots(List<Node> roots, IIndex index){
		return new TreeEvaluator().evaluateRoots(roots, index);
	}

}