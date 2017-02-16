package es.uam.eps.bmi.search.ranking.lucene;

import java.util.Iterator;

import org.apache.lucene.search.ScoreDoc;

import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
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

	private LuceneRankingIterator iter;

	public LuceneRanking(LuceneIndex index, ScoreDoc[] scoreDocs) {
		iter = new LuceneRankingIterator(index,  scoreDocs);
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
