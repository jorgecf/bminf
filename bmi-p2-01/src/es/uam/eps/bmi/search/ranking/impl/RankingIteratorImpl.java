package es.uam.eps.bmi.search.ranking.impl;

import java.util.Comparator;
import java.util.TreeSet;

import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.ranking.SearchRankingIterator;

/**
 * Iterador implementado: un array de ImplRankedDoc's sobre el que se itera.
 *
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 */
public class RankingIteratorImpl implements SearchRankingIterator {

	private int cuttoff;
	private TreeSet<RankedDocImpl> results; // MaxHeap

	// lista vacia
	public RankingIteratorImpl(int cutoff) {
		this.cuttoff = cutoff;

		this.results = new TreeSet<>(new Comparator<RankedDocImpl>() {

			@Override
			public int compare(RankedDocImpl o1, RankedDocImpl o2) {
				return o1.compareTo(o2);
			}
		});
	}

	public void add(RankedDocImpl r) {

		this.results.add(r);
		
		if (this.results.size() > this.cuttoff) {
			this.results.pollLast();
		}
	}

	@Override
	public boolean hasNext() {
		return this.results.size() > 0;
	}

	@Override
	public SearchRankingDoc next() {
		return this.results.pollFirst();
	}

	public int getSize() {
		return results.size();
	}
}