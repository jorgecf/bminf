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

	/*private double scoreAux(int user, int item, Ranking kNearest, boolean normalize) {

		double acc = 0.0;
		int nNeighbours = 0;
		double c = 0.0;

		Iterator<RankingElement> it = kNearest.iterator();
		while (it.hasNext()) {
			RankingElement neighbour = it.next();
			Double nr = this.ratings.getRating(neighbour.getID(), item);
			if (nr != null) {
				acc = acc + (neighbour.getScore() * nr);
				nNeighbours++;

				if (normalize) {
					c = c + neighbour.getScore();
				}
			}
		}

		if (nNeighbours >= this.minNeighbourRatings)
			return (double) acc / c;
		else
			return 0.0;
	}*/

	@Override
	public String toString() {
		return "normalized user-based kNN (" + this.similarity + ")";
	}

}
