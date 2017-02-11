package es.uam.eps.bmi.search.lucene;

import java.io.IOException;

import org.apache.lucene.search.IndexSearcher;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.ranking.SearchRanking;

public class LuceneEngine extends AbstractEngine {

	private LuceneIndex index;
	private IndexSearcher idxSearcher;
	
	public LuceneEngine(String path) throws IOException {
		super(path);
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {
		// TODO Auto-generated method stub
		
		// TODO seguir aqui
		
		return null;
	}

	@Override
	public void loadIndex(String path) throws IOException {
		this.index = new LuceneIndex(path);
	}

}
