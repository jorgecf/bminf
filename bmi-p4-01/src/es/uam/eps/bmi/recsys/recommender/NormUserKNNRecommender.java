package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.CosineUserSimilarity;

public class NormUserKNNRecommender extends AbstractRecommender {

	public NormUserKNNRecommender(Ratings ratings, CosineUserSimilarity cosineUserSimilarity, int k, int i) {
		// TODO Auto-generated constructor stub
		super(ratings);
	}

	@Override
	public double score(int user, int item) {
		// TODO Auto-generated method stub
		return 0;
	}

}
