package es.uam.eps.bmi.sna.metric.network;

import es.uam.eps.bmi.sna.metric.GlobalMetric;
import es.uam.eps.bmi.sna.structure.UndirectedSocialNetwork;

/**
 * Calcula la asortatividad de una red.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class Assortativity<U> implements GlobalMetric<U> {

	@Override
	public double compute(UndirectedSocialNetwork<U> network) {

		int m = network.nEdges();

		double grads2 = 0.0;
		double grads3 = 0.0;
		double gradsEdges = 0.0;

		for (U user : network.getUsers()) {
			// (sumatorio g(U) ^ 2 ) ^2
			// el grado es el numero de edges en los que participa
			grads2 = grads2 + Math.pow(network.getContacts(user).size(), 2);

			// sumatorio g(U) ^ 3
			grads3 = grads3 + Math.pow(network.getContacts(user).size(), 3);

			for (U user2 : network.getContacts(user)) {
				gradsEdges = gradsEdges + (network.getContacts(user).size() * network.getContacts(user2).size());
			}
		}

		grads2 = Math.pow(grads2, 2);
		gradsEdges /= 2; // eliminamos arcos repetidos

		return (double) (4 * m * gradsEdges - grads2) / (2 * m * grads3 - grads2);
	}

	@Override
	public String toString() {
		return "Assortativity";
	}

}