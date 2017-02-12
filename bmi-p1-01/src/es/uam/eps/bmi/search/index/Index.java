package es.uam.eps.bmi.search.index;

import java.io.IOException;
import java.util.List;

import es.uam.eps.bmi.search.index.freq.FreqVector;

public interface Index {

	public abstract void load(String s) throws IOException, NoIndexException;

	public abstract List<String> getAllTerms();

	public abstract int getTermTotalFreq(String word) throws IOException;

	public abstract FreqVector getDocVector(int docID) throws IOException;

	public abstract String getDocPath(int docID) throws IOException;

	public abstract long getTermFreq(String word, int docID) throws IOException;

	public abstract long getTermDocFreq(String word) throws IOException;
	
}
