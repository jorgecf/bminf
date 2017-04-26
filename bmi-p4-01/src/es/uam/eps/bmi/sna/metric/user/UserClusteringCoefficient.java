package es.uam.eps.bmi.sna.metric.user;

import es.uam.eps.bmi.sna.metric.LocalMetric;
import es.uam.eps.bmi.sna.ranking.Ranking;
import es.uam.eps.bmi.sna.structure.UndirectedSocialNetwork;

public class UserClusteringCoefficient<U extends Comparable<U>> implements LocalMetric<U, U> {

	public UserClusteringCoefficient(int topK) {
		// TODO
	}

	public UserClusteringCoefficient() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Ranking<U> compute(UndirectedSocialNetwork<U> network) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double compute(UndirectedSocialNetwork<U> network, U element) {
		// TODO Auto-generated method stub
		return 0;
	}

}