package es.uam.eps.bmi.search.ranking.impl;

import java.util.List;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.ranking.SearchRankingIterator;

/**
 * Iterador implementado: un array de ImplRankedDoc's sobre el que se itera.
 *
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 */
public class RankingIteratorImpl implements SearchRankingIterator {

	RankedDocImpl results[];
	Index index;
	int n = 0;

	public RankingIteratorImpl(Index index, List<RankedDocImpl> list) {
		this.index = index;
		this.results = list.toArray(new RankedDocImpl[list.size()]);
	}

	// lista vacia
	public RankingIteratorImpl() {
		this.index = null;
		this.results = new RankedDocImpl[0];
	}

	@Override
	public boolean hasNext() {
		return n < this.results.length;
	}

	@Override
	public SearchRankingDoc next() {
		return new RankingDocImpl(index, results[n++]);
	}

}
