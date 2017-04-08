package es.uam.eps.bmi.recsys.recommender;

import java.util.Iterator;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElement;

public abstract class AbstractUserKNNRecommender extends AbstractRecommender {

	public AbstractUserKNNRecommender(Ratings ratings) {
		super(ratings);
	}

	protected double scoreAux(int user, int item, Ranking kNearest, boolean normalize, int minNeighbourRatings) {

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

		// Normalizacion
		if (normalize == false) {
			c = 1;
		}

		// Restriccion en numero de valoraciones minimo de mis vecinos
		if (nNeighbours >= minNeighbourRatings)
			return (double) acc / c;
		else
			return 0.0;
	}

}
