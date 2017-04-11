package es.uam.eps.bmi.recsys.metric;

import java.util.Iterator;
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
public class Precision implements Metric {

	private Ratings ratings;
	private double treshold;
	private int cutoff;

	public Precision(Ratings ratings, double treshold, int cutoff) {
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

			// Obtenenemos el ranking de lo que se le ha recomenddo
			Ranking r = rec.getRecommendation(user);

			int c = 0;

			Iterator<RankingElement> it = r.iterator();
			while (it.hasNext() && c != this.cutoff) {

				RankingElement rel = it.next();

				// Lo ha valorado con nota necesaria?: Si si, es una buena
				// recomendacion
				Double rat = this.ratings.getRating(user, rel.getID());
				if (rat != null && rat > this.treshold) {
					nRelevants++;
				}

				c++;
			}

			pAcc += ((double) nRelevants) / this.cutoff; // relevantes-en-k / k
		}

		return ((double) pAcc) / rec.getUsers().size();
	}

	@Override
	public String toString() {
		return "Precision@" + this.cutoff;
	}

}