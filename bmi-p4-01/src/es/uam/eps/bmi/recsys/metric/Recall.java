package es.uam.eps.bmi.recsys.metric;

import java.util.Iterator;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElement;

public class Recall implements Metric {

	private Ratings ratings;
	private double treshold;
	private int cutoff;

	public Recall(Ratings ratings, double treshold, int cutoff) {
		this.ratings = ratings;
		this.treshold = treshold;
		this.cutoff = cutoff;
	}

	@Override
	public double compute(Recommendation rec) {

		int nRelevants = 0;
		double pAcc = 0;

		// Iteramos sobre cada user
		for (Integer user : rec.getUsers()) {

			nRelevants = 0;

			// Obtenenmos el ranking de lo que se le ha recomenddo
			Ranking r = rec.getRecommendation(user);

			int c = 0;

			Iterator<RankingElement> it = r.iterator();
			while (it.hasNext() && c != this.cutoff) {

				RankingElement rel = it.next();

				// Lo ha valorado con nota necesaria?
				Double rat = this.ratings.getRating(user, rel.getID());
				if (rat != null && rat > this.treshold) {
					nRelevants++;
				}

				c++;
			}

			// relevantes-en-k / numero de relevantes
			if (this.ratings.getItems(user) != null) {
				pAcc += ((double) nRelevants) / this.ratings.getItems(user).size();
			}
		}

		return ((double) pAcc) / rec.getUsers().size();
	}

	@Override
	public String toString() {
		return "Recall@" + this.cutoff;
	}

}
