package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.CosineFeatureSimilarity;

public class CentroidRecommender<F>  extends AbstractRecommender {

	public CentroidRecommender(Ratings ratings, CosineFeatureSimilarity<F> cosineFeatureSimilarity) {
		super(ratings);
		// TODO Auto-generated constructor stub
	}


	@Override
	public double score(int user, int item) {
		// TODO Auto-generated method stub
		return 0;
	}

}
