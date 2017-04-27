package es.uam.eps.bmi.sna.metric.user;

import es.uam.eps.bmi.sna.metric.LocalMetric;
import es.uam.eps.bmi.sna.ranking.Ranking;
import es.uam.eps.bmi.sna.ranking.RankingImpl;
import es.uam.eps.bmi.sna.structure.UndirectedSocialNetwork;

/**
 * Calcula el coeficiente de clustering local de un nodo de una red.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class UserClusteringCoefficient<U extends Comparable<U>> implements LocalMetric<U, U> {

	private Ranking<U> ret;

	public UserClusteringCoefficient(int topK) {
		this.ret = new RankingImpl<>(topK);
	}

	public UserClusteringCoefficient() {
		this.ret = new RankingImpl<>();
	}

	@Override
	public Ranking<U> compute(UndirectedSocialNetwork<U> network) {

		for (U user : network.getUsers()) {
			ret.add(user, this.compute(network, user));
		}

		return ret;
	}

	@Override
	public double compute(UndirectedSocialNetwork<U> network, U element) {

		double neighbourCon = 0.0;
		double possibleCon = 0.0;

		// posibles conexiones entre vecinos (grafo no dirigido)
		possibleCon = (double) (network.getContacts(element).size() * (network.getContacts(element).size() - 1)) / 2;

		// conexiones entre vecinos
		for (U neighbour : network.getContacts(element)) {
			for (U neighbour2 : network.getContacts(element)) {
				if (network.connected(neighbour, neighbour2))
					neighbourCon++;
			}
		}

		// eliminamos conexiones repetidas
		neighbourCon /= 2;

		return (double) neighbourCon / possibleCon;
	}

	@Override
	public String toString() {
		return "UserClusteringCoefficient";
	}

}