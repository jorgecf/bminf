package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

public class NormUserKNNRecommender extends AbstractUserKNNRecommender {

	public NormUserKNNRecommender(Ratings ratings, Similarity similarity, int k, int minNeighbourRatings) {
		super(ratings, similarity,k, true,minNeighbourRatings);
	}

	@Override
	public String toString() {
		return "normalized user-based kNN (" + this.similarity + ")";
	}

}