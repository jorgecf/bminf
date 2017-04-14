package es.uam.eps.bmi.recsys.recommender.similarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import es.uam.eps.bmi.recsys.data.Ratings;

/**
 * Calcula la similitud entre dos items por el metodo del coseno.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class CosineItemSimilarity implements Similarity {

	private Ratings ratings;
	private Map<Integer, Map<Integer, Double>> data;
	private Map<Integer, Double> itemRatings;

	public CosineItemSimilarity(Ratings ratings) {

		this.ratings = ratings;

		this.data = new HashMap<>();
		this.itemRatings = new HashMap<>();

		Set<Integer> items1 = ratings.getItems();
		Set<Integer> items2 = ratings.getItems();

		// Precalculamos todas las similitudes
		for (Integer item1 : items1) {

			HashMap<Integer, Double> nh = new HashMap<>();
			for (Integer item2 : items2) {

				if (this.data.containsKey(item2) && this.data.get(item2).containsKey(item1)) {
					// simetria, ya almacenado
				} else {
					Double v = this.simAux(item1, item2);
					if (v > 0.0) {
						nh.put(item2, v);
					}
				}
			}

			this.data.put(item1, nh);
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

	public double simAux(int x, int y) {

		double acc = 0.0;
		double acc2u = 0.0;
		double acc2v = 0.0;

		Set<Integer> x1 = this.ratings.getUsers(x);
		Set<Integer> y1 = this.ratings.getUsers(y);

		if (x1 == null || y1 == null) {
			return 0.0;
		}

		// Usuarios que han valorado ambos items
		HashSet<Integer> xy = new HashSet<>(x1);
		xy.retainAll(y1); // Interseccion

		// Sumatorio de users que han valorado ambos items
		for (Integer user : xy) {

			Double rx = this.ratings.getRating(user, x);
			Double ry = this.ratings.getRating(user, y);

			acc += rx * ry;
		}

		// Sumatorio de r(u, i)^2 (ratings obtenidos por x)
		if (this.itemRatings.containsKey(x) == false) {
			for (Integer user : this.ratings.getUsers(x)) {
				Double rx = this.ratings.getRating(user, x);

				acc2u += rx * rx;
			}

			this.itemRatings.put(x, acc2u);
		} else {
			acc2u = this.itemRatings.get(x);
		}

		// Sumatorio de r(v, i)^2 (ratings obtenidos por y)
		if (this.itemRatings.containsKey(y) == false) {
			for (Integer user : this.ratings.getUsers(y)) {
				Double ry = this.ratings.getRating(user, y);

				acc2v += ry * ry;
			}

			this.itemRatings.put(y, acc2v);
		} else {
			acc2v = this.itemRatings.get(y);
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