package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

public class UserKNNRecommender extends AbstractUserKNNRecommender {

	public UserKNNRecommender(Ratings ratings, Similarity sim, int k) {
		super(ratings, sim, k, false, 0);
	}

	@Override
	public String toString() {
		return "user-based kNN (" + this.similarity + ")";
	}

}