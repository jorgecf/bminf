package es.uam.eps.bmi.recsys.recommender.similarity;

import es.uam.eps.bmi.recsys.data.Ratings;

public class CosineUserSimilarity implements Similarity {

	private Ratings ratings;

	public CosineUserSimilarity(Ratings ratings) {
		this.ratings = ratings;
	}

	@Override
	public double sim(int x, int y) {

		// tema 5 pag 58 o 60
		Double acc = 0.0;
		Double acc2u = 0.0;
		Double acc2v = 0.0;
		
		//TODO coger menor set x o y
		for (Integer item : this.ratings.getItems(x)) {

			Double rx = this.ratings.getRating(x, item);
			Double ry = this.ratings.getRating(y, item);

			if (rx != null && ry != null) {
				acc += rx * ry;
				acc2u += rx * rx;
				acc2v += ry * ry;
			}
		}

		return (double) acc / Math.sqrt(acc2u * acc2v); /// NaN TODO
	}

}