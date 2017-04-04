package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.CosineItemSimilarity;
import es.uam.eps.bmi.recsys.recommender.similarity.JaccardFeatureSimilarity;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

public class ItemNNRecommender extends AbstractRecommender {

	public ItemNNRecommender(Ratings ratings, Similarity similarity) {
		// TODO Auto-generated constructor stub
		super(ratings);
	}

	@Override
	public double score(int user, int item) {
		// TODO Auto-generated method stub
		return 0;
	}

}
