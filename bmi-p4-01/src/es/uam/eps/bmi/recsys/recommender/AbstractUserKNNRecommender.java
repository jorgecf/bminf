package es.uam.eps.bmi.recsys.recommender;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.RankingElement;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

/**
 * Recommender base para user-based KNN.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public abstract class AbstractUserKNNRecommender extends AbstractRecommender {

	/* Mapa de user - ranking de sus vecinos mas proximos, ya calculado */
	protected Map<Integer, RankingImpl> prevRankings;
	protected Similarity similarity;
	protected int k;
	protected int minNeighbourRatings;
	protected boolean normalize;

	public AbstractUserKNNRecommender(Ratings ratings, Similarity similarity, int k, boolean normalize,
			int minNeighbourRatings) {

		super(ratings);

		this.k = k;
		this.normalize = normalize;
		this.minNeighbourRatings = minNeighbourRatings;
		this.similarity = similarity;
		
		this.prevRankings = new HashMap<>();
	}

	@Override
	public double score(int user, int item) {

		Double r = this.ratings.getRating(user, item);

		if (r != null) {
			return r;
		} else {

			RankingImpl rank;

			// k vecinos mas proximos.
			if (this.prevRankings.containsKey(user) == false) {

				rank = new RankingImpl(this.k);
				for (Integer user2 : this.ratings.getUsers()) {
					Double s = this.similarity.sim(user, user2);
					if (s > 0.0) {
						rank.add(user2, s);
					}
				}

				this.prevRankings.put(user, rank);
			} else {
				rank = this.prevRankings.get(user);
			}

			return this.scoreAux(user, item, rank, this.normalize, this.minNeighbourRatings);
		}
	}

	protected double scoreAux(int user, int item, RankingImpl kNearest, boolean normalize, int minNeighbourRatings) {

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

		// Normalizacion.
		if (normalize == false) {
			c = 1;
		}

		// Restriccion en numero de valoraciones minimo de vecinos.
		if (nNeighbours >= minNeighbourRatings)
			return (double) acc / c;
		else
			return 0.0;
	}

}
