package es.uam.eps.bmi.sna.metric.edge;

import es.uam.eps.bmi.sna.metric.LocalMetric;
import es.uam.eps.bmi.sna.ranking.Ranking;
import es.uam.eps.bmi.sna.structure.Edge;
import es.uam.eps.bmi.sna.structure.UndirectedSocialNetwork;

public class Embededness<U extends Comparable<U>> implements LocalMetric<Edge<U>, U> {
	
	public Embededness(int topK) {
		// TODO
	}

	@Override
	public Ranking<Edge<U>> compute(UndirectedSocialNetwork<U> network) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double compute(UndirectedSocialNetwork<U> network, Edge<U> element) {
		// TODO Auto-generated method stub
		return 0;
	}


}
