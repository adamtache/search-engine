package parser;

import java.util.List;

import index.IIndex;

public class TokenizedData {
	
	private List<String> myTokens;
	private List<String> mySpellCorrected;

	public TokenizedData(List<String> tokens, List<String> spellCorrected, IIndex index){
		this.myTokens = tokens;
		this.mySpellCorrected = spellCorrected;
	}
	
	public List<String> getTokens(){
		return this.myTokens;
	}
	
	public List<String> getSpellCorrected(){
		return this.mySpellCorrected;
	}
	
}