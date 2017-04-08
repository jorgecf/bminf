package es.uam.eps.bmi.recsys.recommender.similarity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import es.uam.eps.bmi.recsys.data.Ratings;

public class CosineItemSimilarity implements Similarity {

	private Ratings ratings;
	// private Map<Integer, Map<Integer, Double>> data;

	public CosineItemSimilarity(Ratings ratings) {
		this.ratings = ratings;
	}

	@Override
	public double sim(int x, int y) {

		double acc = 0.0;
		double acc2u = 0.0;
		double acc2v = 0.0;

		Set<Integer> x1 = this.ratings.getUsers(x);
		Set<Integer> y1 = this.ratings.getUsers(y);

		// Usuarios que han valorado ambos items
		HashSet<Integer> xy = new HashSet<>(x1);
		xy.retainAll(y1); // Interseccion

		// Sumatorio de users que han valorado ambos items
		for (Integer user : xy) {

			Double rx = this.ratings.getRating(user, x);
			Double ry = this.ratings.getRating(user, y);

			if (rx != null && ry != null) {
				acc += rx * ry;
			}
		}

		// Sumatorio de r(u, i)^2 (ratings obtenidos por x)
		for (Integer user : this.ratings.getUsers(x)) {
			Double rx = this.ratings.getRating(user, x);

			if (rx != null) {
				acc2u += rx * rx;
			}

		}

		// Sumatorio de r(v, i)^2 (ratings obtenidos por y)
		for (Integer user : this.ratings.getUsers(y)) {
			Double ry = this.ratings.getRating(user, y);

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