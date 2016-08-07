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

	public static ISearchResult getSearchResult(TokenizedData tokenizedData, IIndex index){
		List<String> tokens = tokenizedData.getTokens();
		if(isVectorSpaceModel(tokens)){
			return getVectorModelData(tokens, index);
		}
		// Boolean model
		List<Node> roots = new TreeFactory(index).createRoot(tokens);
		return getData(roots, tokenizedData, index);
	}

	public static ISearchResult getSpellCorrectedResult(TokenizedData tokenizedData, IIndex index){
		List<String> correctedTokens = tokenizedData.getSpellCorrected();
		if(isVectorSpaceModel(correctedTokens)){
			return getVectorModelData(correctedTokens, index);
		}
		// Boolean model
		List<Node> correctedRoots = new TreeFactory(index).createRoot(correctedTokens);
		return getData(correctedRoots, tokenizedData, index);
	}

	private static ISearchResult getVectorModelData(List<String> tokens, IIndex index){
		tokens = getOnlyTerms(tokens);
		Set<String> docTerms = index.getDocTerms();
		Query query = getQuery(docTerms, tokens);
		List<Double> queryVector = query.getQueryVector();
		Map<Document, Double> cosSimMap = getCosSimMap(tokens, docTerms, queryVector, index);
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

	private static Map<Document, Double> getCosSimMap(List<String> tokens, Set<String> docTerms, List<Double> queryVector, IIndex index){
		Map<Document, Double> cosSimMap = new HashMap<>();
		Set<String> URLs = index.getMatchingDocURLs(tokens);
		for(String url : URLs){
			List<Double> doc = index.getDoc(url, docTerms);
			double similarityValue = calculateSimilarity(doc, queryVector);
			Document document = new Document(url, index.getDocTitle(url), index.getDocSnippet(url));
			cosSimMap.put(document, similarityValue);
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

	private static Query getQuery(Set<String> docTerms, List<String> tokens){
		List<String> docTermList = new ArrayList<>(docTerms);
		Map<String, Double> queryMap = new HashMap<>();
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
			queryMap.put(docTermList.get(x), queryVector.get(x));
		}
		return new Query(queryMap);
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

	private static ISearchResult getData(List<Node> roots, TokenizedData tokenizedData, IIndex index){
		ISearchResult data = evaluateRoots(roots, index);
		if(data != null)
			data.setTokenizedData(tokenizedData);
		return data;
	}

	private static ISearchResult evaluateRoots(List<Node> roots, IIndex index){
		return new TreeEvaluator().evaluateRoots(roots, index);
	}

}