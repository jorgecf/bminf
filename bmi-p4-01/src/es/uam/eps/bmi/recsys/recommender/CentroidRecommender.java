package es.uam.eps.bmi.recsys.recommender;

import java.util.HashSet;
import java.util.Set;

import es.uam.eps.bmi.recsys.data.Features;
import es.uam.eps.bmi.recsys.data.FeaturesImpl;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.similarity.FeatureSimilarity;

/**
 * Recommender por centroide de usuario. 
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class CentroidRecommender<F> extends AbstractRecommender {

	private FeatureSimilarity<F> similarity;

	public CentroidRecommender(Ratings ratings, FeatureSimilarity<F> similarity) {
		super(ratings);

		this.similarity = similarity;

		// Construimos una estructura de Features que aloje cada centroide (Users x Features)
		Features<F> centroids = new FeaturesImpl<>();

		// Obtenemos todas las features.
		Set<F> allf = new HashSet<>();
		for (Integer item : this.similarity.getFeatures().getIDs()) {
			allf.addAll(this.similarity.getFeatures().getFeatures(item));
		}

		// Creamos el centroide (matriz Feature x Usuario)
		for (Integer user : this.ratings.getUsers()) {

			for (F feature : allf) {

				Double val = 0.0;
				for (Integer item : this.ratings.getItems(user)) {
					Double r = this.ratings.getRating(user, item);
					Double f = this.similarity.getFeatures().getFeature(item, feature);
					if (r != null && f != null) {
						val = val + (r * f);
					}
				}
				
				val = (double) val / this.ratings.getItems(user).size();
				centroids.setFeature(user, feature, val);
			}
		}

		// Set de los centroides
		this.similarity.setXFeatures(centroids);
	}

	@Override
	public double score(int user, int item) {
		Double r = this.ratings.getRating(user, item);

		if (r != null)
			return r;
		else
			return this.similarity.sim(user, item);
	}

	@Override
	public String toString() {
		return "centroid-based (" + this.similarity + ")";
	}

}