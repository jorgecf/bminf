package es.uam.eps.bmi.search.graph;

import java.io.IOException;
import java.util.HashMap;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.SearchEngine;
import es.uam.eps.bmi.search.index.DocumentMap;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRanking;

public class PageRank extends AbstractEngine implements DocumentMap {

	private double r;
	private int convergenceCondition;


	public PageRank(String string, double r, int convergenceCondition) {
		super(null);
		this.r = r;
		this.convergenceCondition = convergenceCondition;
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {

		// Ignoraremos la query

		// Para cada doc sacamos su PageRank
		for (int i = 0; i < index.numDocs(); i++) {

			// Calculamos los outlinks
			// HashMap<String, Integer> out = new HashMap<>();

		}
	}

	@Override
	public String getDocPath(int docID) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDocNorm(int docID) throws IOException {
		// no hacer
		return 0;
	}

	@Override
	public DocumentMap getDocMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
