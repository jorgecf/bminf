package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.CosineItemSimilarity;
import es.uam.eps.bmi.recsys.recommender.similarity.JaccardFeatureSimilarity;

public class ItemNNRecommender implements Recommender {

	public ItemNNRecommender(Ratings ratings, CosineItemSimilarity cosineItemSimilarity) {
		// TODO Auto-generated constructor stub
	}

	//public ItemNNRecommender(Ratings ratings, JaccardFeatureSimilarity<F> jaccardFeatureSimilarity) {
		// TODO Auto-generated constructor stub
//	}


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
