package es.uam.eps.bmi.search.ranking.impl;

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

	private RankingIteratorImpl iter;

	public RankingImpl(Index index, int cutoff) {
		this.iter = new RankingIteratorImpl(index, cutoff);
	}

	@Override
	public Iterator<SearchRankingDoc> iterator() {
		return this.iter;
	}

	@Override
	public int size() {
		return this.iter.results.size();
	}

	public void add(int doc, double score) {
		this.iter.add(new RankedDocImpl(doc, score));
	}
}