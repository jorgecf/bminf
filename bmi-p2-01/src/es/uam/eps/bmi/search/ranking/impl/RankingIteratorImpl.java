package es.uam.eps.bmi.search.ranking.impl;

import java.util.ArrayList;
import java.util.Collections;
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

	List<RankedDocImpl> results;
	Index index;
	int n = 0;
	int cutoff;


	// lista vacia
	public RankingIteratorImpl() {
		this.index = null;
		this.results = new ArrayList<>();
	}

	public RankingIteratorImpl(Index index, int cutoff) {
		this.index = index;
		this.cutoff = cutoff;
		this.results = new ArrayList<>();
	}

	public void add(RankedDocImpl r) {

		this.results.add(r);
		
		Collections.sort(this.results);
		if (this.results.size() > cutoff) {
			this.results = this.results.subList(0, this.cutoff - 1);
		}
	}

	@Override
	public boolean hasNext() {
		return n < this.results.size();
	}

	@Override
	public SearchRankingDoc next() {
		return new RankingDocImpl(index, results.get(n++));
	}
}