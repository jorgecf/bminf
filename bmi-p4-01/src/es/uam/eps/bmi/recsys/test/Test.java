package es.uam.eps.bmi.recsys.test;

import es.uam.eps.bmi.recsys.Recommendation;
import es.uam.eps.bmi.recsys.data.Features;
import es.uam.eps.bmi.recsys.data.FeaturesImpl;
import es.uam.eps.bmi.recsys.data.IntParser;
import es.uam.eps.bmi.recsys.data.Parser;
import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.recommender.Recommender;
import es.uam.eps.bmi.recsys.data.RatingsImpl;
import es.uam.eps.bmi.recsys.data.StringParser;
import es.uam.eps.bmi.recsys.metric.Metric;
import es.uam.eps.bmi.recsys.metric.Precision;
import es.uam.eps.bmi.recsys.metric.Recall;
import es.uam.eps.bmi.recsys.metric.Rmse;
import es.uam.eps.bmi.recsys.recommender.AverageRecommender;
import es.uam.eps.bmi.recsys.recommender.CentroidRecommender;
import es.uam.eps.bmi.recsys.recommender.ItemNNRecommender;
import es.uam.eps.bmi.recsys.recommender.MajorityRecommender;
import es.uam.eps.bmi.recsys.recommender.NormUserKNNRecommender;
import es.uam.eps.bmi.recsys.recommender.UserKNNRecommender;
import es.uam.eps.bmi.recsys.recommender.similarity.CosineFeatureSimilarity;
import es.uam.eps.bmi.recsys.recommender.similarity.CosineItemSimilarity;
import es.uam.eps.bmi.recsys.recommender.similarity.CosineUserSimilarity;
import es.uam.eps.bmi.recsys.recommender.similarity.JaccardFeatureSimilarity;
import es.uam.eps.bmi.recsys.util.Timer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 *
 * @author pablo
 */
public class Test {
    public static void main (String a[]) throws FileNotFoundException {
        System.out.println("=========================");
        System.out.println("Testing MovieLens \"latest-small\" dataset");
        testDataset("data/ratings.csv", "data/tags.csv", ",", new StringParser(), 35, 1176);
        System.out.println("=========================");
        System.out.println("Testing MovieLens HetRec dataset");
        testDataset("data/user_ratedmovies.dat", "data/movie_tags.dat", "\t", new IntParser(), 894, 993);
    }
    
    static <F>void testDataset(String ratingsFile, String featuresFile, String separator, Parser<F> featureParser, int user, int item) 
            throws FileNotFoundException {
        int n = 100;
        int cutoff = 10;
        int k = 50;
        double threshold = 4;

        Ratings ratings = new RatingsImpl(ratingsFile, separator);
        Features<F> features = new FeaturesImpl<F>(featuresFile, separator, featureParser);
        
    //    testData(ratings, features, user, item);
        
        testRecommenders(ratings, features, k, n, 3, 4);

        Ratings folds[] = ratings.randomSplit(0.8);
        Ratings train = folds[0];
        Ratings test = folds[1];
        
        Metric metrics[] = new Metric [] {
            new Rmse(test),
            new Precision(test, threshold, cutoff),
            new Recall(test, threshold, cutoff),
        };
        
      //  evaluateRecommenders(train, features, k, n, metrics);
    }
    
    static <F>void testData(Ratings ratings, Features<F> features, int user, int item) {
        System.out.println("-------------------------");
        System.out.println("Testing the data structures");
        System.out.println(ratings.nRatings() + " ratings by " + ratings.getUsers().size() + " users on " + ratings.getItems().size() + " items");
        System.out.print("Ratings of user " + user + ": ");
        for (int i : ratings.getItems(user)) System.out.print("(" + i + "," + ratings.getRating(user, i) + ") ");
        System.out.println();
        System.out.print("Ratings of item " + item + ": ");
        for (int u : ratings.getUsers(item)) System.out.print("(" + u + "," + ratings.getRating(u, item) + ") ");
        System.out.println();
        System.out.print("Features of item " + item + ": ");
        for (F f : features.getFeatures(item)) System.out.print("(" + f + "," + features.getFeature(item, f) + ") ");
        System.out.println();
    }
    
    static <F> void testRecommenders(Ratings ratings, Features<F> features, int k, int n, int nUsers, int nItems) throws FileNotFoundException {
      //  Timer.reset();
     //   testRecommender(new MajorityRecommender(ratings), n, nUsers, nItems);
     //   Timer.reset();
      //  testRecommender(new AverageRecommender(ratings, 2), n, nUsers, nItems);
      //  Timer.reset();
      //  testRecommender(new UserKNNRecommender(ratings, new CosineUserSimilarity(ratings), k), n, nUsers, nItems);
      //  Timer.reset();
    //    testRecommender(new NormUserKNNRecommender(ratings, new CosineUserSimilarity(ratings), k, 2), n, nUsers, nItems);
     //   Timer.reset();
     //   testRecommender(new ItemNNRecommender(ratings, new CosineItemSimilarity(ratings)), n, nUsers, nItems);
        Timer.reset();
        testRecommender(new CentroidRecommender<F>(ratings, new CosineFeatureSimilarity<F>(features)), n, nUsers, nItems);
     //   Timer.reset();
      //  testRecommender(new ItemNNRecommender(ratings, new JaccardFeatureSimilarity<F>(features)), n, nUsers, nItems);
    }

    static <U extends Comparable<U>,I extends Comparable<I>,F> void evaluateRecommenders(Ratings ratings, Features<F> features, int k, int n, Metric metrics[]) {
        Timer.reset();
        evaluateRecommender(new MajorityRecommender(ratings), n, metrics);
        Timer.reset();
        evaluateRecommender(new AverageRecommender(ratings, 2), n, metrics);
        Timer.reset();
        evaluateRecommender(new UserKNNRecommender(ratings, new CosineUserSimilarity(ratings), k), n, metrics);
        Timer.reset();
        evaluateRecommender(new NormUserKNNRecommender(ratings, new CosineUserSimilarity(ratings), k, 2), n, metrics);
        Timer.reset();
        evaluateRecommender(new ItemNNRecommender(ratings, new CosineItemSimilarity(ratings)), n, metrics);
        Timer.reset();
        evaluateRecommender(new CentroidRecommender<F>(ratings, new CosineFeatureSimilarity<F>(features)), n, metrics);
        Timer.reset();
        evaluateRecommender(new ItemNNRecommender(ratings, new JaccardFeatureSimilarity<F>(features)), n, metrics);
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
    
    static <U,I extends Comparable<I>> void evaluateRecommender(Recommender recommender, int n, Metric metrics[]) {
        System.out.println("-------------------------");
        System.out.println("Evaluating " + recommender + " recommender");
        Recommendation rec = recommender.recommend(n);
        for (Metric metric : metrics) System.out.println("   " + metric + " = " + metric.compute(rec));
        Timer.time("--> ");
    }
}
