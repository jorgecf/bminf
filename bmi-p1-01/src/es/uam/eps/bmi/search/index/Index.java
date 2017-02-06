package es.uam.eps.bmi.search.index;

import java.util.List;

import es.uam.eps.bmi.search.index.freq.FreqVector;

public interface Index {

	public abstract void load(String s);

	public abstract List<String> getAllTerms();

	public abstract int getTermTotalFreq(String s);

	public abstract FreqVector getDocVector(int docID);
	
}
