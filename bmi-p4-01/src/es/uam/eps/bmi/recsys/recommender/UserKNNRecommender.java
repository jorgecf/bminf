package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

public class UserKNNRecommender extends AbstractRecommender {

	public UserKNNRecommender(Ratings ratings, Similarity similarity, int k) {
		// TODO Auto-generated constructor stub
		super(ratings);
	}

	@Override
	public double score(int user, int item) {
		// TODO Auto-generated method stub
		return 0;
	}

}
