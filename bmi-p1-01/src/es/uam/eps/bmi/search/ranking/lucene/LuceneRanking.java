package es.uam.eps.bmi.search.ranking.lucene;

import java.util.ArrayList;
import java.util.Iterator;

import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;

/**
 * El LuceneRanking se compone de una lista de SearchRankingDocs.
 * 
 * @author
 * @author
 *
 */
public class LuceneRanking implements SearchRanking {

	private ArrayList<SearchRankingDoc> scoreDocs;

	public LuceneRanking(ArrayList<SearchRankingDoc> scoreDocs) {
		this.scoreDocs = scoreDocs;
	}

	@Override
	public Iterator<SearchRankingDoc> iterator() {
		return this.scoreDocs.iterator();
	}

	@Override
	public int size() {
		return this.scoreDocs.size();
	}

}
