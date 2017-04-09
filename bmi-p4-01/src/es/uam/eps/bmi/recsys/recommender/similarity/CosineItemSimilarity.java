package es.uam.eps.bmi.recsys.recommender.similarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.util.Timer;

public class CosineItemSimilarity implements Similarity {

	private Ratings ratings;
	private Map<Integer, Map<Integer, Double>> data;
	// private Map<IdPair, Double> data2;
	private Map<Integer, Double> itemRatings;

	public CosineItemSimilarity(Ratings ratings) {

		Timer.reset(" cosine item START ");
		this.ratings = ratings;

		this.data = new HashMap<>();
		//this.data2 = new HashMap<>();
		this.itemRatings = new HashMap<>();

		Set<Integer> items1 = ratings.getItems();
		Set<Integer> items2 = ratings.getItems();

		// Precalculamos todas las similitudes
		/*
		 * for (Integer item1 : items1) {
		 * 
		 * HashMap<Integer, Double> nh = new HashMap<>(); for (Integer item2 :
		 * items2) { if (item1 != item2) { Double v = this.simAux(item1, item2);
		 * if (v > 0.0) nh.put(item2, v); }
		 * 
		 * this.data.put(item1, nh); } }
		 */

		int index = 0;
		int end = items2.size();
		List<Integer> rest = new ArrayList<Integer>(items2);
		for (Integer item1 : items1) {

			index++;
			// System.out.println("CosineItemSimilarity.CosineItemSimilarity()index:::
			// "+index);
			// Set<Integer> rest=new HashSet<>(items2);
			// List<Integer> rest=new ArrayList<Integer>(items2);
			// items2.remove(item1);
			// Timer.reset("haciendo sublista");
			rest = rest.subList(1, end + 1 - index);
			// Timer.time("sublista---> ");

			HashMap<Integer, Double> nh = new HashMap<>();
			for (Integer item2 : rest) {
				// if (item1 != item2) {
				// IdPair pair = new IdPair(item1, item2);

				// if (this.data2.containsKey(pair) == false) {
				Double v = this.simAux(item1, item2);
				if (v > 0.0) {
					// this.data2.put(new IdPair(item1, item2), v);
					nh.put(item2, v);
				}
				// }
				// }

			}

			if (Math.floorMod(index, 100) == 0)
				System.out.println("item number " + index + ", rest size: " + rest.size());

		}

		Timer.time(" cosine item END ");
	}

	@Override
	public double sim(int x, int y) {

		if (this.data.containsKey(x) && this.data.get(x).containsKey(y)) {
			return this.data.get(x).get(y);
		} else {
			return 0.0;
		}
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

			if (rx != null && ry != null) {
				acc += rx * ry;
			}
		}

		// Sumatorio de r(u, i)^2 (ratings obtenidos por x)
		if (this.itemRatings.containsKey(x) == false) {
			for (Integer user : this.ratings.getUsers(x)) {
				Double rx = this.ratings.getRating(user, x);

				if (rx != null) {
					acc2u += rx * rx;
				}

			}

			this.itemRatings.put(x, acc2u);
		} else {
			acc2u = this.itemRatings.get(x);
		}

		// Sumatorio de r(v, i)^2 (ratings obtenidos por y)
		if (this.itemRatings.containsKey(y) == false) {
			for (Integer user : this.ratings.getUsers(y)) {
				Double ry = this.ratings.getRating(user, y);

				if (ry != null) {
					acc2v += ry * ry;
				}
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