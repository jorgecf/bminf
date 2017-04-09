package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

/**
 * Recommender por el metodo de k-nearest neighbours.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class UserKNNRecommender extends AbstractUserKNNRecommender {

	public UserKNNRecommender(Ratings ratings, Similarity sim, int k) {
		super(ratings, sim, k, false, 0);
	}

	@Override
	public String toString() {
		return "user-based kNN (" + this.similarity + ")";
	}

}