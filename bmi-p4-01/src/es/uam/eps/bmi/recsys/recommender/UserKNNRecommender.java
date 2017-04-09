package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

public class UserKNNRecommender extends AbstractUserKNNRecommender {

	private int k;
	private Similarity similarity;

	public UserKNNRecommender(Ratings ratings, Similarity sim, int k) {
		super(ratings);
		this.k = k;
		this.similarity = sim;
	}

	@Override
	public double score(int user, int item) {

		Double r = this.ratings.getRating(user, item);

		if (r != null) {
			return r;
		} else {

			// k vecinos mas proximos
			RankingImpl rank = new RankingImpl(this.k);
			for (Integer user2 : this.ratings.getUsers()) {
				Double s = this.similarity.sim(user, user2);
				if (s > 0.0) {
					rank.add(user2, s);
				}
			}

			return this.scoreAux(user, item, rank, false, 0);
		}
	}

	@Override
	public String toString() {
		return "user-based kNN (" + this.similarity + ")";
	}

}