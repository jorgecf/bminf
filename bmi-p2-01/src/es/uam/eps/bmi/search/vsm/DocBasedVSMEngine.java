package es.uam.eps.bmi.search.vsm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.impl.RankingImpl;
import es.uam.eps.bmi.search.vsm.AbstractVSMEngine;

public class DocBasedVSMEngine extends AbstractEngine {

	public DocBasedVSMEngine(Index idx) {
		super(idx);
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {
		
		String[] terms = query.split(" ");
		
		// en acum se iran guardando la suma del tfidf para cada doc, por lo que al final
		//	tendra toda la parte de arriba del coseno de similitud
		Hashtable<Integer, Double> acum = new Hashtable<>();
		RankingImpl ranking = new RankingImpl(index, cutoff);
		
		ArrayList<PostingsList> pls = new ArrayList<PostingsList> ();
		ArrayList<Iterator<Posting>> pls_iter = new ArrayList<Iterator<Posting>> ();
		
		for (int i = 0; i < terms.length; i++) {
			
			PostingsList pl = this.index.getPostings(terms[i]);
			pls.add(pl);
			pls_iter.add(pl.iterator());
			
		}
		
		int max = pls.get(0).size();
		for (int i = 0; i < pls.size(); i ++) {
			if (pls.get(i).size() > max)
				max = i;
		}
		
		int minDocID = max;
		
		while (pls_iter.get(max).hasNext()) {
			
			Posting p1 = (Posting) pls_iter.get(max).next();
			
			for (int i = 0; i < terms.length; i ++) {
				
				if (i == max)
					continue;
				
				if (pls_iter.get(i).hasNext()) {
					Posting p2 = (Posting) pls_iter.get(i).next();
					if (p1.getDocID() <= p2.getDocID())
						break;
					else
						minDocID = i;
				}
				
			}
			
			Posting pMin = (Posting) pls_iter.get(minDocID);
			double tfidf = tfidf(pMin.getFreq(), pls.get(minDocID).size(), this.index.numDocs());

			if (acum.containsKey(pMin.getDocID())) {
				acum.put(pMin.getDocID(), acum.get(pMin.getDocID()) + tfidf);
			} else {
				acum.put(pMin.getDocID(), tfidf);
			}
			
		} 
		
		return null;
	}

}
