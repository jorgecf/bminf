package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.RecommendationImpl;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;

public abstract class AbstractRecommender implements Recommender {

	private Ratings ratings;

	public AbstractRecommender(Ratings ratings) {
		this.ratings = ratings;
	}

	@Override
	public Recommendation recommend(int cutoff) {

		Recommendation r = new RecommendationImpl();

		// Iteramos sobre cada usuario.
		for (Integer user : this.ratings.getUsers()) {

			// Para cada usuario, generamos su ranking.
			Ranking rank = new RankingImpl(cutoff);

			for (Integer item : this.ratings.getItems()) {

				// No queremos que aparezcan items que este user ya ha valorado.
				if (this.ratings.getItems(user).contains(item) == false)
					rank.add(item, this.score(user, item));
			}

			r.add(user, rank);
		}

		return r;
	}

}
