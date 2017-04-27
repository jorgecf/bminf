package es.uam.eps.bmi.sna.metric.network;

import es.uam.eps.bmi.sna.metric.GlobalMetric;
import es.uam.eps.bmi.sna.metric.LocalMetric;
import es.uam.eps.bmi.sna.ranking.Ranking;
import es.uam.eps.bmi.sna.ranking.RankingElement;
import es.uam.eps.bmi.sna.structure.UndirectedSocialNetwork;

/**
 * Calcula la media de una metrica para sus usuarios.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class AvgUserMetric<U extends Comparable<U>> implements GlobalMetric<U> {

	private LocalMetric<U, U> metric;

	public AvgUserMetric(LocalMetric<U, U> metric) {
		this.metric = metric;
	}

	@Override
	public double compute(UndirectedSocialNetwork<U> network) {

		double avg = 0.0;

		Ranking<U> r = metric.compute(network);
		for (RankingElement<U> re : r) {
			avg += re.getScore();
		}

		return (double) avg / r.size();
	}

	@Override
	public String toString() {
		return "Avg(" + this.metric + ")";
	}

}