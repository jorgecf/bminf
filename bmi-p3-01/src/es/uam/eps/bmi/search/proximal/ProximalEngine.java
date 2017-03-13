package es.uam.eps.bmi.search.proximal;

import java.io.IOException;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.vsm.AbstractVSMEngine;

public class ProximalEngine extends AbstractVSMEngine {

	public ProximalEngine(Index index) {
		super(index);
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
