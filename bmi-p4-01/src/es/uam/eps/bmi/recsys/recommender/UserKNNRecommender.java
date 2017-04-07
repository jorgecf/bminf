package es.uam.eps.bmi.recsys.recommender;

import java.util.Iterator;
import java.util.Set;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.data.RatingsImpl;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElement;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

public class UserKNNRecommender extends AbstractRecommender {

	private Ratings ratings2;
	// private RankingImpl rank;

	public UserKNNRecommender(Ratings ratings, Similarity sim, int k) {
		super(ratings);

		Set<Integer> users1 = ratings.getUsers();
		Set<Integer> users2 = ratings.getUsers();
		Set<Integer> items = ratings.getItems();

		for (Integer user : users1) {

			for (Integer item : items) {

				// Si no hay rating lo simulamos
				if (this.ratings.getRating(user, item) == null) {

					// Sumatorio de sim entre user actual y otros users que
					// han valorado el item
					Double acc = 0.0;

					for (Integer vuser : users2) {
						if (user != vuser) {
							Double r = this.ratings.getRating(vuser, item);
							if (r != null) {
								acc += sim.sim(user, vuser) * r;
							}
						}
					}

					this.ratings.rate(user, item, acc);
				}
			}
		}

		// knn
		this.ratings2 = new RatingsImpl();

		for (Integer user : ratings.getUsers()) {

			Ranking rank = new RankingImpl(k);
			for (Integer item : ratings.getItems(user)) {
				rank.add(item, this.ratings.getRating(user, item)); // k mejores
			}

			// for (RankingElement re : rank) {
			Iterator<RankingElement> it = rank.iterator();
			while (it.hasNext()) {
				RankingElement re = it.next();
				ratings2.rate(user, re.getID(), re.getScore());
			}
		}

		this.ratings = null;
		this.ratings = this.ratings2;
	}

	@Override
	public double score(int user, int item) {

		Double r = this.ratings.getRating(user, item);

		if (r == null)
			return 0.11112;
		else
			return r; // TODO simplif con average

	}

}