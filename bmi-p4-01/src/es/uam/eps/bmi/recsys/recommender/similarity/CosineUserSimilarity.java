package es.uam.eps.bmi.recsys.recommender.similarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import es.uam.eps.bmi.recsys.data.Ratings;

public class CosineUserSimilarity implements Similarity {

	private Ratings ratings;
	private Map<Integer, Map<Integer, Double>> data;

	public CosineUserSimilarity(Ratings ratings) { // TODO vencidarios
													// simetricos? ---> ¿¿x,y ==
													// y,x??
		this.ratings = ratings;

		this.data = new HashMap<>();

		Set<Integer> users1 = ratings.getUsers();
		Set<Integer> users2 = ratings.getUsers();

		// Precalculamos todas las similitudes
		for (Integer user1 : users1) {

			HashMap<Integer, Double> nh = new HashMap<>();
			for (Integer user2 : users2) {
				if (user1 != user2) {
					Double v = this.simAux(user1, user2);
					if (v > 0.0)
						nh.put(user2, v);
				}

				this.data.put(user1, nh);
			}
		}

	}

	@Override
	public double sim(int x, int y) {

		if (this.data.containsKey(x) && this.data.get(x).containsKey(y)) {
			return this.data.get(x).get(y);
		} else {
			return 0.0;
		}
	}

	private double simAux(int x, int y) {

		Double acc = 0.0;
		Double acc2u = 0.0;
		Double acc2v = 0.0;

		Set<Integer> x1 = this.ratings.getItems(x);
		Set<Integer> y1 = this.ratings.getItems(y);

		// Items valorados por ambos
		HashSet<Integer> xy = new HashSet<>(x1);
		xy.retainAll(y1); // Interseccion

		// Sumatorio de items que ambos users han valorado
		for (Integer item : xy) {

			Double rx = this.ratings.getRating(x, item);
			Double ry = this.ratings.getRating(y, item);

			if (rx != null && ry != null) {
				acc += rx * ry;
			}
		}

		// Sumatorio de r(u, i)^2 (items rateados por x)
		for (Integer item : this.ratings.getItems(x)) {
			Double rx = this.ratings.getRating(x, item);

			if (rx != null) {
				acc2u += rx * rx;
			}

		}

		// Sumatorio de r(v, i)^2 (items rateados por y)
		for (Integer item : this.ratings.getItems(y)) {
			Double ry = this.ratings.getRating(y, item);

			if (ry != null) {
				acc2v += ry * ry;
			}
		}

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