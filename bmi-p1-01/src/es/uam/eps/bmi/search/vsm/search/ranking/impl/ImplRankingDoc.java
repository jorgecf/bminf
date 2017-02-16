package es.uam.eps.bmi.search.ranking.impl;

import java.io.IOException;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;

public class ImplRankingDoc extends SearchRankingDoc {

	private Index index;
	private ImplRankedDoc rankedDoc;

	public ImplRankingDoc(Index index, ImplRankedDoc r) {
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
