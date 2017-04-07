package es.uam.eps.bmi.recsys.recommender.similarity;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.util.Timer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CosineUserSimilarity implements Similarity {

	private Ratings ratings;
	private Map<Integer, Map<Integer, Double>> data;

	public CosineUserSimilarity(Ratings ratings) { // TODO vencidarios
													// simetricos? ---> ¿¿x,y ==
													// y,x??
		Timer.reset();
		this.ratings = ratings;

		this.data = new HashMap<>();

		Set<Integer> users1 = ratings.getUsers();
		Set<Integer> users2 = ratings.getUsers();

		// Precalculamos todas las similitudes
		for (Integer user1 : users1) {

			HashMap<Integer, Double> nh = new HashMap<>();
			for (Integer user2 : users2) {
				Double v = this.simAux(user1, user2);
				nh.put(user2, v);
			}
			this.data.put(user1, nh);

		}

		Timer.time("[1] cosine constructor: ");
	}

	@Override
	public double sim(int x, int y) {

		if (this.data.containsKey(x) && this.data.get(x).containsKey(y)) {
			return this.data.get(x).get(y);
		} else {
			System.out.println("CosineUserSimilarity.sim(): SIM not found");
			return this.simAux(x, y);
		}
	}

	private double simAux(int x, int y) {
		// tema 5 pag 58 o 60
		Double acc = 0.0;
		Double acc2u = 0.0;
		Double acc2v = 0.0;

		// TODO coger menor set x o y
		for (Integer item : this.ratings.getItems(x)) { // getItems(x)

			Double rx = this.ratings.getRating(x, item); // TODO formula mal?
			Double ry = this.ratings.getRating(y, item);

			if (rx != null && ry != null) {
				acc += rx * ry;
			}

		}

		for (Integer item : this.ratings.getItems()) {
			Double rx = this.ratings.getRating(x, item); // TODO formula mal?
			Double ry = this.ratings.getRating(y, item);

			if (rx != null) {
				acc2u += rx * rx;
			}

			if (ry != null) {
				acc2v += ry * ry;
			}
		}

		Double ret = (double) acc / Math.sqrt(acc2u * acc2v);
		if (ret.isNaN())
			return 0.0;
		else
			return ret;
		// return ret; /// NaN TODO
	}
}