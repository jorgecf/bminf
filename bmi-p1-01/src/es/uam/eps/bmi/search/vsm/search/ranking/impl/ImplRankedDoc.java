package es.uam.eps.bmi.search.ranking.impl;

public class ImplRankedDoc implements Comparable<ImplRankedDoc> { // scoredoc

	private int docID;
	private double score;

	public ImplRankedDoc(int docID, double sum) {
		this.docID = docID;
		this.score = sum;
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
	public int compareTo(ImplRankedDoc o) {
		return Double.compare(o.score, this.score);
	}

}
