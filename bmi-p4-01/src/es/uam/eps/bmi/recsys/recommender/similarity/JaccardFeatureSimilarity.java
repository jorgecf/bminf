package es.uam.eps.bmi.recsys.recommender.similarity;

import java.util.HashSet;
import java.util.Set;

import es.uam.eps.bmi.recsys.data.Features;

/**
 * Calcula la similitud entre dos items por Jaccard sobre sus caracteristicas.
 * (intereseccion de features / union de features).
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class JaccardFeatureSimilarity<F> extends FeatureSimilarity<F> {

	public JaccardFeatureSimilarity(Features<F> features) {
		super(features);
	}

	@Override
	public double sim(int x, int y) {

		// Caracteristicas de cada item
		Set<F> xf = this.xFeatures.getFeatures(x);
		Set<F> yf = this.xFeatures.getFeatures(y);

		if (xf.size() == 0 || yf.size() == 0) {
			return 0.0;
		}

		// Interseccion de x-y
		Set<F> common = new HashSet<>(xf);
		common.retainAll(yf);

		if (common.size() == 0) {
			return 0.0;
		}

		return (double) common.size() / (xf.size() + yf.size() - common.size());
	}

	@Override
	public String toString() {
		return "Jaccard on item features";
	}

}