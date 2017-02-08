package es.uam.eps.bmi.search.index;

import java.io.IOException;
import java.util.List;

import es.uam.eps.bmi.search.index.freq.FreqVector;

public interface Index {

	public abstract void load(String s);

	public abstract List<String> getAllTerms();

	public abstract int getTermTotalFreq(String s) throws IOException;

	public abstract FreqVector getDocVector(int docID);

	public abstract String getDocPath(int doc);

	public abstract String getTermFreq(String word, int docID);

	public abstract String getTermDocFreq(String word);
	
}
