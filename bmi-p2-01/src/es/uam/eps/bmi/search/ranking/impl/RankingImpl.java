package es.uam.eps.bmi.search.ranking.impl;

import java.io.IOException;
import java.util.Iterator;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;

/**
 * Ranking de busqueda propio.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 */
public class RankingImpl implements SearchRanking {

	private Index index;
	private RankingIteratorImpl iter;

	public RankingImpl(Index index, int cutoff) {
		this.iter = new RankingIteratorImpl(cutoff);
		this.index = index;
	}

	@Override
	public Iterator<SearchRankingDoc> iterator() {
		return this.iter;
	}

	@Override
	public int size() {
		return this.iter.getSize();
	}

	public void add(int doc, double score) throws IOException {
		this.iter.add(new RankedDocImpl(doc, score, index.getDocPath(doc)));
	}
}