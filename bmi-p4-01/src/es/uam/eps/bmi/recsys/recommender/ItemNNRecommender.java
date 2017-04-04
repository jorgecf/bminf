package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.CosineItemSimilarity;
import es.uam.eps.bmi.recsys.recommender.similarity.JaccardFeatureSimilarity;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

public class ItemNNRecommender implements Recommender {

	public ItemNNRecommender(Ratings ratings, Similarity similarity) {
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
