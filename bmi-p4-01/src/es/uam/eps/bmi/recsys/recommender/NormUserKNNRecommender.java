package es.uam.eps.bmi.recsys.recommender;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

/**
 * Recommender por el metodo de k-nearest neighbors normalizado. Ademas fija un
 * minimo de valoraciones por item para que este pueda ser recomendado.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class NormUserKNNRecommender extends AbstractUserKNNRecommender {

	public NormUserKNNRecommender(Ratings ratings, Similarity similarity, int k, int minNeighbourRatings) {
		super(ratings, similarity, k, true, minNeighbourRatings);
	}

	@Override
	public String toString() {
		return "normalized user-based kNN (" + this.similarity + ")";
	}

}