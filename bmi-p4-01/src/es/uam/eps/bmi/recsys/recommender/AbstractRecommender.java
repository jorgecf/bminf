package es.uam.eps.bmi.recsys.recommender;

import java.util.HashSet;
import java.util.Set;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.RecommendationImpl;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;

public abstract class AbstractRecommender implements Recommender {

	protected Ratings ratings;

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
			
			// Items no rateados por user (no queremos recomendar los que ya ha valorado)
			Set<Integer> nv = new HashSet<>( this.ratings.getItems());
			nv.removeAll(this.ratings.getItems(user));

			//for (Integer item : this.ratings.getItems()) {
			for (Integer item : nv) {
				// No queremos que aparezcan items que este user ya ha valorado.
				//if (this.ratings.getRating(user, item) == null)
					rank.add(item, this.score(user, item));
			}

			r.add(user, rank);
		}

		return r;
	}

}