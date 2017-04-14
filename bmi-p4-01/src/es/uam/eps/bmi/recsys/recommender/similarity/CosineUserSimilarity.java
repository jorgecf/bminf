package es.uam.eps.bmi.recsys.recommender.similarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import es.uam.eps.bmi.recsys.data.Ratings;

/**
 * Calcula la similitud entre usuarios por el metodo del coseno.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class CosineUserSimilarity implements Similarity {

	protected Ratings ratings;

	/* Informacion de user -> (user2, sim[user, user2]) */
	protected Map<Integer, Map<Integer, Double>> data;

	/* Mapa de usuario -> sumatorio de sus ratings al cuadrado */
	protected Map<Integer, Double> userRatings;

	public CosineUserSimilarity(Ratings ratings) {

		this.ratings = ratings;

		this.data = new HashMap<>();
		this.userRatings = new HashMap<>();

		Set<Integer> users1 = ratings.getUsers();
		Set<Integer> users2 = ratings.getUsers();

		// Precalculamos todas las similitudes
		for (Integer user1 : users1) {

			HashMap<Integer, Double> nh = new HashMap<>();

			for (Integer user2 : users2) {

				if (this.data.containsKey(user2) && this.data.get(user2).containsKey(user1)) {
					// simetria
				} else {
					Double v = this.simAux(user1, user2);
					if (v > 0.0) {
						nh.put(user2, v);
					}
				}
			}

			this.data.put(user1, nh);
		}
	}

	@Override
	public double sim(int x, int y) {

		if (this.data.containsKey(x) && this.data.get(x).containsKey(y))
			return this.data.get(x).get(y);

		if (this.data.containsKey(y) && this.data.get(y).containsKey(x))
			return this.data.get(y).get(x);

		return 0.0;

	}

	protected double simAux(int x, int y) {

		Double acc = 0.0;
		Double acc2u = 0.0;
		Double acc2v = 0.0;

		Set<Integer> x1 = this.ratings.getItems(x);
		Set<Integer> y1 = this.ratings.getItems(y);

		// Items valorados por ambos usuarios.
		HashSet<Integer> xy = new HashSet<>(x1);
		xy.retainAll(y1); // interseccion

		// Si no tienen elementos en comun, su similitud sera
		// 0 / algo ---> 0. Lo mismo pasa si algun user no
		// ha valorado nada ( algo / algo * 0 ) ---> 0.
		if (xy.size() == 0 || x1.size() == 0 || y1.size() == 0) {
			return 0.0;
		}

		// Sumatorio de items que ambos users han valorado.
		for (Integer item : xy) {

			Double rx = this.ratings.getRating(x, item);
			Double ry = this.ratings.getRating(y, item);

			acc += rx * ry;
		}

		// Sumatorio de r(u, i)^2 (items rateados por x).
		if (this.userRatings.containsKey(x) == false) {

			for (Integer item : this.ratings.getItems(x)) {
				Double rx = this.ratings.getRating(x, item);
				acc2u += rx * rx;
			}

			this.userRatings.put(x, acc2u);
		} else {
			acc2u = this.userRatings.get(x);
		}

		// Sumatorio de r(v, i)^2 (items rateados por y).
		if (this.userRatings.containsKey(y) == false) {

			for (Integer item : this.ratings.getItems(y)) {
				Double ry = this.ratings.getRating(y, item);
				acc2v += ry * ry;
			}

			this.userRatings.put(y, acc2v);
		} else {
			acc2v = this.userRatings.get(y);
		}

		// Resultado final.
		Double ret = (double) acc / Math.sqrt(acc2u * acc2v);
		if (ret.isNaN())
			return 0.0;
		else
			return ret;
	}

	@Override
	public String toString() {
		return "cosine";
	}

}