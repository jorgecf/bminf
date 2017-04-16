package es.uam.eps.bmi.recsys.metric;

import java.util.Iterator;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElement;

/**
 * Calcula el error de prediccion con RMSE de una recomendacion respecto a unos
 * ratings.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class Rmse implements Metric {

	private Ratings ratings;

	public Rmse(Ratings ratings) {
		this.ratings = ratings;
	}

	@Override
	public double compute(Recommendation rec) {

		Double rmse = 0.0;
		int nUsers = 0;

		// Iteramos sobre cada user
		for (Integer user : rec.getUsers()) {

			int nTest = 0;
			Double acc = 0.0;
			// Obtenenemos el ranking de lo que se le ha recomendddo
			Ranking rank = rec.getRecommendation(user);

			// Iteramos sobre los items que ha valorado
			Iterator<RankingElement> it = rank.iterator();
			while (it.hasNext()) {

				RankingElement recommended = it.next();

				Double rated = this.ratings.getRating(user, recommended.getID());

				if (rated != null) {
					acc = acc + (Math.pow(recommended.getScore() - rated, 2));
					nTest++;
				}

			}

			// Solo cuentan para el test los elementos tanto valorados como
			// recomendados
			if (nTest > 0) {
				nUsers++;

				Double nx = (Math.sqrt((double) acc / nTest));
				if (nx.isNaN() == false) {
					rmse = rmse + nx;
				}
			}

		}

		return (double) rmse / nUsers;
	}

	@Override
	public String toString() {
		return "Rmse";
	}
}
