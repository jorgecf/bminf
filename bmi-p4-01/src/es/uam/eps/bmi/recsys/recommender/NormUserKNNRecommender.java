package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

public class NormUserKNNRecommender extends AbstractUserKNNRecommender {

	private int k;
	private Similarity similarity;
	private int minNeighbourRatings;

	public NormUserKNNRecommender(Ratings ratings, Similarity similarity, int k, int minNeighbourRatings) {
		super(ratings);

		this.similarity = similarity;
		this.k = k;
		this.minNeighbourRatings = minNeighbourRatings;
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
					rank.add(user2, s); // es vecino
				}
			}

			return this.scoreAux(user, item, rank, true, this.minNeighbourRatings);
		}
	}

	@Override
	public String toString() {
		return "normalized user-based kNN (" + this.similarity + ")";
	}

}
