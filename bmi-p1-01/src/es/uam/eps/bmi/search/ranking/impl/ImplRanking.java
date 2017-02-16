package es.uam.eps.bmi.search.ranking.impl;

import java.util.Iterator;
import java.util.List;

import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;

/**
 * Ranking de busqueda propio.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 */
public class ImplRanking implements SearchRanking {

	private ImplRankingIterator iter;

	public ImplRanking(LuceneIndex index, List<ImplRankedDoc> list) {
		this.iter = new ImplRankingIterator(index, list);
	}

	@Override
	public Iterator<SearchRankingDoc> iterator() {
		return this.iter;
	}

	@Override
	public int size() {
		return this.iter.results.length;
	}
}
