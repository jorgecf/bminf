package es.uam.eps.bmi.search.ranking.impl;

import java.io.IOException;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;

/**
 * Documento de un ranking.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 */
public class RankingDocImpl extends SearchRankingDoc {

	private Index index;
	private RankedDocImpl rankedDoc;

	public RankingDocImpl(Index index, RankedDocImpl r) {
		this.index = index;
		this.rankedDoc = r;
	}

	public double getScore() {
		return rankedDoc.getScore();
	}

	public String getPath() throws IOException {
		return index.getDocPath(rankedDoc.getDocID());
	}
}
