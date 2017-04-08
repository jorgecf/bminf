package es.uam.eps.bmi.recsys.recommender;

import java.util.Iterator;

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

		Double r = this.ratings.getRating(user, item);

		if (r != null) {
			return r;
		} else {

			// k vecinos mas proximos
			RankingImpl rank = new RankingImpl(this.k);
			for (Integer user2 : this.ratings.getUsers()) {
				Double s = this.similarity.sim(user, user2);
				if (s > 0.0)
					rank.add(user2, s);
			}

			return this.scoreAux(user, item, rank); // TODO simplif con average
		}
	}

	private double scoreAux(int user, int item, Ranking kNearest) {

		double acc = 0.0;

		Iterator<RankingElement> it = kNearest.iterator();
		while (it.hasNext()) {
			RankingElement neighbour = it.next();
			Double nr = this.ratings.getRating(neighbour.getID(), item);
			if (nr != null)
				acc = acc + (neighbour.getScore() * nr);
		}

		return acc;
	}

	@Override
	public String toString() {
		return "user-based kNN (" + this.similarity + ")";
	}

}
