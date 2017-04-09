package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

public class ItemNNRecommender extends AbstractRecommender {

	private Similarity similarity;

	public ItemNNRecommender(Ratings ratings, Similarity similarity) {
		super(ratings);

		this.similarity = similarity;
	}

	@Override
	public double score(int user, int item) {

		Double r = this.ratings.getRating(user, item);

		if (r != null) {
			return r;
		} else {

			Double ret = 0.0;
			for (Integer item2 : this.ratings.getItems(user)) {
				ret = ret + (this.similarity.sim(item, item2) * this.ratings.getRating(user, item2));
			}

			return ret;
		}
	}

	@Override
	public String toString() {
		return "item-based NN (" + this.similarity + ")";
	}

}