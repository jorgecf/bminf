package es.uam.eps.bmi.search.ranking.impl;

import java.util.Iterator;

import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;

public class ImplRanking implements SearchRanking {

	private ImplRankingIterator iter;

	public ImplRanking(LuceneIndex index, ImplRankedDoc[] scoreDocs) {
		this.iter = new ImplRankingIterator(index, scoreDocs);
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
