package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElement;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

public class UserKNNRecommender extends AbstractRecommender {

	private int k;
	private Similarity similarity;

	public UserKNNRecommender(Ratings ratings, Similarity sim, int k) {
		super(ratings);
		this.k = k;
		this.similarity = sim;

	}

	@Override
	public double score(int user, int item) {

		// k vecinos mas proximos
		RankingImpl rank = new RankingImpl(this.k);
		for (Integer user2 : this.ratings.getUsers()) {
			Double s = this.similarity.sim(user, user2);
			if (s > 0.0)
				rank.add(user2, s);
		}

		Double r = this.ratings.getRating(user, item);

		if (r != null)
			return r;
		else
			return this.scoreAux(user, item, rank); // TODO simplif con average

	}

	private double scoreAux(int user, int item, Ranking kNearest) {

		double acc = 0.0;
		for (RankingElement neighbour : kNearest) {
			Double nr = this.ratings.getRating(neighbour.getID(), item);
			if (nr != null)
				acc = acc + (neighbour.getScore() * nr);
		}

		return acc;
	}
}
