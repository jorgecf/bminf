package es.uam.eps.bmi.sna.metric.edge;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import es.uam.eps.bmi.sna.metric.LocalMetric;
import es.uam.eps.bmi.sna.ranking.Ranking;
import es.uam.eps.bmi.sna.ranking.RankingImpl;
import es.uam.eps.bmi.sna.structure.Edge;
import es.uam.eps.bmi.sna.structure.UndirectedSocialNetwork;

public class Embededness<U extends Comparable<U>> implements LocalMetric<Edge<U>, U> {

	private Ranking<Edge<U>> ret;

	public Embededness(int topK) {
		this.ret = new RankingImpl<>(topK);
	}

	@Override
	public Ranking<Edge<U>> compute(UndirectedSocialNetwork<U> network) {

		// compaador custom
		Set<Edge<U>> connections = new TreeSet<Edge<U>>(new Comparator<Edge<U>>() {
			@Override
			public int compare(Edge<U> o1, Edge<U> o2) {
				return o1.compareTo(o2);
			}
		});

		// cada pareja de nodos
		for (U user : network.getUsers()) {
			for (U user2 : network.getUsers()) {
				if (user != user2) {

					Edge<U> e = new Edge<U>(user, user2); // sin repetidos
					if (connections.contains(new Edge<U>(user2, user)) == false) {
						connections.add(e);
						ret.add(e, this.compute(network, e));
					}
				}
			}
		}

		return ret;
	}

	@Override
	public double compute(UndirectedSocialNetwork<U> network, Edge<U> element) {

		U u = element.getFirst();
		U v = element.getSecond();
		Set<U> vecinosU = new HashSet<>(network.getContacts(u));
		Set<U> vecinosV = new HashSet<>(network.getContacts(v));

		// vecinos(u) - {v} y viceversa
		vecinosU.remove(v);
		vecinosV.remove(u);

		Set<U> intersection = new HashSet<U>(vecinosU);
		intersection.retainAll(vecinosV);

		Set<U> union = new HashSet<U>(vecinosU);
		union.addAll(vecinosV);

		return (double) intersection.size() / union.size();
	}

}
