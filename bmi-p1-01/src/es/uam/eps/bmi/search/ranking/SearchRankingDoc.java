package es.uam.eps.bmi.search.ranking;

import java.io.IOException;

import org.apache.lucene.search.ScoreDoc;

/**
 * El SearchRankingDoc se compone de un scoreDoc (documento resultado de una
 * busqueda y su score) y la ruta al archivo correpondiente.
 * 
 * @author
 * @author
 *
 */
public class SearchRankingDoc implements Comparable<SearchRankingDoc> {

	protected ScoreDoc rankedDoc;
	protected String path;

	public SearchRankingDoc() {
	}

	public SearchRankingDoc(ScoreDoc scoreDoc, String path) {
		this.rankedDoc = scoreDoc;
		this.path = path;
	}

	@Override
	public int compareTo(SearchRankingDoc o) {

		// los documentos son ordenados por docID
		if (this.rankedDoc.doc > o.getScoreDoc().doc) {
			return 1;
		} else if (this.rankedDoc.doc < o.getScoreDoc().doc) {
			return -1;
		}

		return 0;
	}

	public String getPath() throws IOException {
		return path;
	}

	public double getScore() {
		return this.rankedDoc.score;
	}

	public ScoreDoc getScoreDoc() {
		return rankedDoc;
	}

}
