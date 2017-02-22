package es.uam.eps.bmi.search.vsm;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.impl.RankingImpl;

public class TermBasedVSMEngine extends AbstractVSMEngine {

	public TermBasedVSMEngine(Index idx) {
		super(idx);
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {

		String[] terms = query.split(" ");

		// en acum se iran guardando la suma del tfidf para cada doc, por lo que al final
		//	tendra toda la parte de arriba del coseno de similitud
		Hashtable<Integer, Double> acum = new Hashtable<>();
		RankingImpl ranking = new RankingImpl(index, cutoff);

		// obtenemos cada lista de postings (cada termino qi tiene una)
		for (int i = 0; i < terms.length; i++) {
			
			PostingsList pl = this.index.getPostings(terms[i]);

			Iterator<Posting> iter = pl.iterator();
			while (iter.hasNext()) {
				// calculamos el tfidf para cada posting (doc, frec)
				Posting p = (Posting) iter.next();

				double tfidf = tfidf(p.getFreq(), pl.size(), this.index.numDocs());

				if (acum.containsKey(p.getDocID())) {
					acum.put(p.getDocID(), acum.get(p.getDocID()) + tfidf);
				} else {
					acum.put(p.getDocID(), tfidf);
				}
			}
		}
		
		// dividismos cada valor entre el modulo del documento
		Enumeration<Integer> e = acum.keys();
		while (e.hasMoreElements()) {
			int key = (int) e.nextElement();
			
			Double n = this.index.getDocNorm(key);
			Double sc = acum.get(key) / n;
			
			ranking.add(key, sc);
		}
		
		return ranking;
	}

}
