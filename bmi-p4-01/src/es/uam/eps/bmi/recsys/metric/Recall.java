package es.uam.eps.bmi.recsys.metric;

import java.util.Iterator;
import java.util.Set;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElement;

/**
 * Calcula la Precision@K de una recomendacion respecto a unos ratings.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
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
		int totRelevants = 0;
		double pAcc = 0;

		// Iteramos sobre cada user
		for (Integer user : rec.getUsers()) {

			nRelevants = 0;

			totRelevants = 0;

			Set<Integer> itemSet = this.ratings.getItems(user);
			if (itemSet != null) {
				for (Integer item : itemSet) {
					Double rt = this.ratings.getRating(user, item);
					if (rt.isNaN() == false && rt > this.treshold)
						totRelevants++;
				}
			}

			// Obtenenemos el ranking de lo que se le ha recomenddo
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

			// relevantes-en-k / numero de relevantes TOTALES
			if (this.ratings.getItems(user) != null) {
				Double nAcc = ((double) nRelevants) / totRelevants;
				if (nAcc.isNaN())
					nAcc = 0.0;
				pAcc += nAcc;
			}
		}

		return ((double) pAcc) / rec.getUsers().size();
	}

	@Override
	public String toString() {
		return "Recall@" + this.cutoff;
	}

}
