package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.data.RatingsImpl;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElement;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

public class UserKNNRecommender extends AbstractRecommender {

	// private Ratings ratings;
	// private RankingImpl rank;

	public UserKNNRecommender(Ratings ratings, Similarity sim, int k) {
		super(ratings);

		// this.ratings=ratings;

		for (Integer user : ratings.getUsers()) {
			for (Integer item : ratings.getItems()) {

				// Si no hay rating lo simulamos
				if (this.ratings.getRating(user, item) == null) {

					// Sumatorio de sim entre user actual y otros users que
					// han valorado el item
					Double acc = 0.0;

					for (Integer vuser : ratings.getUsers()) {
						Double r = this.ratings.getRating(user, item);
						if (r == null) {
							acc = sim.sim(user, vuser) * r;
						}
					}

					this.ratings.rate(user, item, acc);
				}

			}
		}

		// knn
		Ratings r2 = new RatingsImpl();

		for (Integer user : ratings.getUsers()) {

			Ranking rank = new RankingImpl(k);
			for (Integer item : ratings.getItems(user)) {
				rank.add(item, this.ratings.getRating(user, item)); // k mejores
			}

			for (RankingElement re : rank) {
				r2.rate(user, re.getID(), re.getScore());
			}
		}

	}

	@Override
	public double score(int user, int item) {

		Double r = this.ratings.getRating(user, item);
		if (r == null)
			return 0.0;
		else
			return r; // TODO simplif con average

		// return this.rank.
	}

}