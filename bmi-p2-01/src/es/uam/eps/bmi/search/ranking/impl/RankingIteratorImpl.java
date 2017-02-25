package es.uam.eps.bmi.search.ranking.impl;

import org.apache.lucene.util.PriorityQueue;

import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.ranking.SearchRankingIterator;

/**
 * Iterador implementado: un array de ImplRankedDoc's sobre el que se itera.
 *
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 */
public class RankingIteratorImpl implements SearchRankingIterator {

	private PriorityQueue<RankedDocImpl> results; // MinHeap de Lucene

	// lista vacia
	public RankingIteratorImpl(int cutoff) {

		this.results = new PriorityQueue<RankedDocImpl>(cutoff) {

			@Override
			protected boolean lessThan(RankedDocImpl arg0, RankedDocImpl arg1) {
				if (arg0.compareTo(arg1) < 0)
					return true;
				return false;
			}
		};
	}

	public void add(RankedDocImpl r) {
		this.results.add(r);
	}

	@Override
	public boolean hasNext() {
		return this.results.size() > 0;
	}

	@Override
	public SearchRankingDoc next() {
		return results.pop();
	}

	public int getSize() {
		return results.size();
	}
}