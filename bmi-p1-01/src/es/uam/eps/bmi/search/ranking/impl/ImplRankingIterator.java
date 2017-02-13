package es.uam.eps.bmi.search.ranking.impl;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.ranking.SearchRankingIterator;
import es.uam.eps.bmi.search.ranking.lucene.LuceneRankingDoc;

public class ImplRankingIterator implements SearchRankingIterator {

	ImplRankedDoc results[];
	Index index;
	int n = 0;

	public ImplRankingIterator(Index index, ImplRankedDoc[] results) {
		this.index = index;
		this.results = results;
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
