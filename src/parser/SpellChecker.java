package parser;

import org.gauner.jSpellCorrect.ToySpellingCorrector;

/**
 * SpellChecker using jSpellCorrect's ToySpellingCorrector. Learns from a training set then can correct provided words.
 * 
 */
public class SpellChecker {
	
	private ToySpellingCorrector sc;

	public SpellChecker(){
		sc = new ToySpellingCorrector();
		// train some data from a text file
		sc.trainFile("src/resources/english_training_data.txt");
		// train a single word
		sc.trainSingle("forest");
	}
	
	public String getCorrect(String word){
		return sc.correct(word);
	}
	
}