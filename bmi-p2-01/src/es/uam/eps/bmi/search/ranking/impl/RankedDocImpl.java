package es.uam.eps.bmi.search.ranking.impl;

import java.io.IOException;

import es.uam.eps.bmi.search.ranking.SearchRankingDoc;

/**
 * Funciona como scoreDoc, modela un documento con un score en nuestro modelo
 * implementado.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 */
public class RankedDocImpl extends SearchRankingDoc {

	private int docID;
	private double score;
	private String path;

	public RankedDocImpl(int docID, double sum, String path) {
		this.docID = docID;
		this.score = sum;
		this.path = path;
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public String getPath() throws IOException {
		return this.path;
	}
}
