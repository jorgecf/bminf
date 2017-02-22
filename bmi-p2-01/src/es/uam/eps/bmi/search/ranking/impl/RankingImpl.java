package es.uam.eps.bmi.search.ranking.impl;

import java.util.Iterator;
import java.util.List;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Ranking de busqueda propio.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 */
public class RankingImpl implements SearchRanking {

	private RankingIteratorImpl iter;

	public RankingImpl(LuceneIndex index, List<RankedDocImpl> list) {
		this.iter = new RankingIteratorImpl(index, list);
	}

	public RankingImpl(Index index, int cutoff) {
		throw new NotImplementedException();
		// TODO
	}

	@Override
	public Iterator<SearchRankingDoc> iterator() {
		return this.iter;
	}

	@Override
	public int size() {
		return this.iter.results.length;
	}

	public void add(int doc, double score) {
		throw new NotImplementedException();
		// TODO
	}
}