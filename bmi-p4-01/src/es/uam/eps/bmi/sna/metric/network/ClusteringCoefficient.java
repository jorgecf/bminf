package es.uam.eps.bmi.sna.metric.network;

import es.uam.eps.bmi.sna.metric.GlobalMetric;
import es.uam.eps.bmi.sna.structure.UndirectedSocialNetwork;

public class ClusteringCoefficient<U> implements GlobalMetric<U> {

	@Override
	public double compute(UndirectedSocialNetwork<U> network) {

		double numTriang = 0.0;
		double numVertex = 0.0;

		// Numero de triangulos cerrados
		for (U user : network.getUsers()) {
			for (U user2 : network.getContacts(user)) {

				for (U user3 : network.getContacts(user2)) {

					// Se cierra el triangulo
					if (network.connected(user, user3) == true) {
						numTriang++;
					}
				}

			}
		}

		numTriang/=2; // quitamos repetidos
		
		// Numero de vertices
		for (U user : network.getUsers()) {
			int size = network.getContacts(user).size();
			numVertex = numVertex + (double) ((size * (size - 1)) / 2);
		}

		return (double) numTriang / numVertex;
	}

}