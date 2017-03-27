package es.uam.eps.bmi.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import es.uam.eps.bmi.search.index.DocumentMap;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.index.lucene.LuceneIndexBuilder;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.ranking.impl.RankingImpl;

public class CombinedEngine implements SearchEngine {
	
	private SearchEngine[] seArr;
	private double[] weights;
	private DocumentMap dm;
	private LuceneIndexBuilder indxBldr;
	
	public CombinedEngine (SearchEngine[] seArr, double[] weights) {
		this.seArr = seArr;
		this.weights = weights;
		try {
			indxBldr = new LuceneIndexBuilder();
			indxBldr.build(null, "index/combined");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {
		
		SearchRanking[] srArr = new SearchRanking[this.weights.length];
		HashMap<Integer, List<Double>> ranking = new HashMap<Integer, List<Double>> ();
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		HashMap<Integer, Double> hashNorm = new HashMap<Integer, Double>();
		
		for (int i = 0; i < this.weights.length; i ++) {
			srArr[i] = seArr[i].search(query, cutoff);
		}
		
		double max = 0, min = Double.MAX_VALUE;
		int docID = 0;
		
		for (int i = 0; i < srArr.length; i ++) {
			for (SearchRankingDoc result : srArr[i]) {
				if (result.getScore() > max)
					max = result.getScore();
				if (result.getScore() < min)
					min = result.getScore();
				if (!hm.containsKey(result.getPath())) {
					hm.put(result.getPath(), docID);
					docID++;
				}
				if (ranking.containsKey(result.getPath())){
					ranking.get(result.getPath()).add(result.getScore() * this.weights[i]);
				} else {
					ArrayList<Double> l = new ArrayList<Double>();
					l.add(result.getScore() * this.weights[i]);
					ranking.put(hm.get(result.getPath()), l);
					this.indxBldr.indexText("hola", result.getPath());
				}
			}
		}
		
		Iterator<Entry<Integer, List<Double>>> it = ranking.entrySet().iterator();
		
		while (it.hasNext()) {
			Entry<Integer, List<Double>> res = it.next();
			
			double scoreTot = 0;
			
			for (double score : res.getValue())
				scoreTot += (score-min)/(max-min);
			
			hashNorm.put(res.getKey(), scoreTot);
			
		}
		
		this.dm = new LuceneIndex("index/combined");
		
		RankingImpl rankingNorm = new RankingImpl(this.dm, cutoff);
		Iterator<Entry<Integer, Double>> ite = hashNorm.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<Integer, Double> ent = ite.next();
			rankingNorm.add(ent.getKey(), ent.getValue());
		}
		
		return rankingNorm;
	}

	@Override
	public DocumentMap getDocMap() {
		return this.dm;
	}

}
