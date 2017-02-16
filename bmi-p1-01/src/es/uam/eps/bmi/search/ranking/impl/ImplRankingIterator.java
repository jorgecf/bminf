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
public class ImplRankingIterator implements SearchRankingIterator {

	ImplRankedDoc results[];
	Index index;
	int n = 0;

	public ImplRankingIterator(Index index, List<ImplRankedDoc> list) {
		this.index = index;
		this.results = list.toArray(new ImplRankedDoc[list.size()]);
	}

	// lista vacia
	public ImplRankingIterator() {
		this.index = null;
		this.results = new ImplRankedDoc[0];
	}

	@Override
	public boolean hasNext() {
		return n < this.results.length;
	}

	@Override
	public SearchRankingDoc next() {
		return new ImplRankingDoc(index, results[n++]);
	}

}
