package es.uam.eps.bmi.recsys.recommender.similarity;

import java.util.Set;

import es.uam.eps.bmi.recsys.data.Features;

public class CosineFeatureSimilarity<F> extends FeatureSimilarity<F> {

	public CosineFeatureSimilarity(Features<F> features) {
		super(features);
	}

	@Override
	public double sim(int x, int y) {

		double accFitem = 0.0;
		double userCentroid = 0.0;
		double common = 0.0;

		Set<F> itemF = this.yFeatures.getFeatures(y);
		Set<F> userF = this.xFeatures.getFeatures(x);

		if (itemF.size() == 0 || userF.size() == 0) {
			return 0.0;
		}

		// Sumatorio de las Features del item
		for (F itemFeature : itemF) {
			double n = this.yFeatures.getFeature(y, itemFeature);
			accFitem = accFitem + (n * n);
		}

		// Sumatorio del centroide de usuario
		for (F userFeature : userF) {
			double n = this.xFeatures.getFeature(x, userFeature);
			userCentroid = userCentroid + (n * n);
		}

		// Sumatorio de caracteristicas comunes
		for (F commonFeature : itemF) {//TODO removeall
			common = common
					+ (this.xFeatures.getFeature(x, commonFeature) * this.yFeatures.getFeature(y, commonFeature));
		}

		return (double) common / Math.sqrt(accFitem * userCentroid);
	}

	@Override
	public String toString() {
		return "cosine on user centroid";
	}
}