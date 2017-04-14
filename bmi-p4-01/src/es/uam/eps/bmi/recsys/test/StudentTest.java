package es.uam.eps.bmi.recsys.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Features;
import es.uam.eps.bmi.recsys.data.FeaturesImpl;
import es.uam.eps.bmi.recsys.data.Parser;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.data.RatingsImpl;
import es.uam.eps.bmi.recsys.data.StringParser;
import es.uam.eps.bmi.recsys.recommender.Recommender;
import es.uam.eps.bmi.recsys.recommender.UserKNNRecommender;
import es.uam.eps.bmi.recsys.recommender.similarity.PearsonSimilarity;
import es.uam.eps.bmi.recsys.util.Timer;

public class StudentTest {
	public static void main(String a[]) throws FileNotFoundException {
		System.out.println("=========================");
		System.out.println("Testing MovieLens \"latest-small\" dataset");
		testDataset("data/ratings.csv", "data/tags.csv", ",", new StringParser(), 35, 1176);
	}

	static <F> void testDataset(String ratingsFile, String featuresFile, String separator, Parser<F> featureParser,
			int user, int item) throws FileNotFoundException {
		int n = 100;
		int k = 50;

		Ratings ratings = new RatingsImpl(ratingsFile, separator);
		Features<F> features = new FeaturesImpl<F>(featuresFile, separator, featureParser);

		testRecommenders(ratings, features, k, n, 3, 4);
	}

	static <F> void testRecommenders(Ratings ratings, Features<F> features, int k, int n, int nUsers, int nItems)
			throws FileNotFoundException {
		Timer.reset();
		testRecommender(new UserKNNRecommender(ratings, new PearsonSimilarity(ratings), k), n, nUsers, nItems);
		
	}
	

    static <U,I extends Comparable<I>> void testRecommender(Recommender recommender, int n, int nUsers, int nItems) throws FileNotFoundException {
        System.out.println("-------------------------");
        System.out.println("Testing " + recommender + " recommender");
        Recommendation rec = recommender.recommend(n);
        rec.print(System.out, nUsers, nItems);
        
        File outFolder = new File("recommendations");
        if (!outFolder.exists()) outFolder.mkdir();
        else for (File f : outFolder.listFiles()) if (f.isFile()) f.delete();

        rec.print(new PrintStream("recommendations/" + recommender + ".dat"));
        Timer.time("--> ");
    }
}
