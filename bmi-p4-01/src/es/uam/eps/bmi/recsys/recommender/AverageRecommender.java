package es.uam.eps.bmi.recsys.recommender;

import java.util.HashMap;
import java.util.Map;

import es.uam.eps.bmi.recsys.data.Ratings;

/**
 * Recommender por media de los items.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class AverageRecommender extends AbstractRecommender {

	private int minRatings;
	private Map<Integer, Double> ratingSum;

	public AverageRecommender(Ratings ratings, int minRatings) {
		super(ratings);

		this.minRatings = minRatings;

		// Hacemos el promedio de ratings.
		this.ratingSum = new HashMap<Integer, Double>();

		for (Integer item : ratings.getItems()) {

			double acc = 0;
			for (Integer user : ratings.getUsers(item)) {
				acc += ratings.getRating(user, item);
			}

			// Solo se tiene en cuenta si cumple el requisito de valoraciones
			// minimas.
			int nRatings = ratings.getUsers(item).size();
			if (nRatings >= this.minRatings) {
				acc = acc / nRatings;
				this.ratingSum.put(item, acc);
			}
		}

	}

	@Override
	public double score(int user, int item) {
		Double s = ratingSum.get(item);
		return (s == null) ? 0 : s;
	}

	@Override
	public String toString() {
		return "average";
	}

}