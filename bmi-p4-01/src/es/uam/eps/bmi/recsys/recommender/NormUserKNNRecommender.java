package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.CosineUserSimilarity;

public class NormUserKNNRecommender implements Recommender {

	public NormUserKNNRecommender(Ratings ratings, CosineUserSimilarity cosineUserSimilarity, int k, int i) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Recommendation recommend(int cutoff) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double score(int user, int item) {
		// TODO Auto-generated method stub
		return 0;
	}

}
