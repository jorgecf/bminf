package es.uam.eps.bmi.recsys.recommender.similarity;

import es.uam.eps.bmi.recsys.data.Features;

/**
 *
 * @author pablo
 * 
 * Features pueden ser géneros, tags, etc. F es el tipo de identificador de las características, 
 * p.e. enteros, strings.
 */
public abstract class FeatureSimilarity<F> implements Similarity {
    Features<F> features;
    
    public FeatureSimilarity(Features<F> features) {
        this.features = features;
    }
    
    public Features<F> getFeatures() {
        return features;
    }
}
