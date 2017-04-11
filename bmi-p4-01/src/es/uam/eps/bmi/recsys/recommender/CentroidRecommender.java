package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

public class CentroidRecommender<F> extends AbstractRecommender {

	private Similarity similarity;

	public CentroidRecommender(Ratings ratings, Similarity similarity) {
		super(ratings);
		this.similarity = similarity;
	}

	@Override
	public double score(int user, int item) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return "centroid-based (" + this.similarity + ")";
	}

}