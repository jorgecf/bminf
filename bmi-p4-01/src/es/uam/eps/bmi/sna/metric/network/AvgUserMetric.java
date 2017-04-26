package es.uam.eps.bmi.sna.metric.network;

import es.uam.eps.bmi.sna.metric.GlobalMetric;
import es.uam.eps.bmi.sna.metric.LocalMetric;
import es.uam.eps.bmi.sna.structure.UndirectedSocialNetwork;

public class AvgUserMetric<U extends Comparable<U>> implements GlobalMetric<U>{

	public AvgUserMetric(LocalMetric<U, U> metric) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double compute(UndirectedSocialNetwork<U> network) {
		// TODO Auto-generated method stub
		return 0;
	}

}
