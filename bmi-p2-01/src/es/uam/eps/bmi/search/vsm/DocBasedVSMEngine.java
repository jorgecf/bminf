package es.uam.eps.bmi.search.vsm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.impl.RankingImpl;
import es.uam.eps.bmi.search.vsm.AbstractVSMEngine;

public class DocBasedVSMEngine extends AbstractVSMEngine {

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
		
		// Obtenemos los postings y los iteradores para cada termino
		for (int i = 0; i < terms.length; i++) {
			
			PostingsList pl = this.index.getPostings(terms[i]);
			pls.add(pl);
			pls_iter.add(pl.iterator());
			
		}
		
		// Tabla hash para mantener los postings activos
		Hashtable<Integer, Posting> ps = new Hashtable<Integer, Posting> ();
		
		// Obtener primeros postings
		for (int i = 0; i < pls_iter.size(); i ++) {
			Posting p = (Posting) pls_iter.get(i).next();
			ps.put(i, p);
		}
		
		int minDocID = this.index.numDocs(); // Minimo docID
		int numQ = 0; // Numero de Query correspondiente al minimo docID
		
		// Se recorren los postings hasta que todos queden vacíos
		while (ps.size() != 0) {
			
			// Obtener documento y query con menor docID
			for (int key : ps.keySet()) {
				
				int aux_minDocID = ps.get(key).getDocID();
				
				if (aux_minDocID <= minDocID) {
					minDocID = aux_minDocID;
					numQ = key;
				}
				
			}
			
			// calculamos el tfidf para cada posting (doc, frec)
			Posting p = ps.get(numQ);
			
			double tfidf = tfidf(p.getFreq(), pls.get(numQ).size(), this.index.numDocs());

			if (acum.containsKey(p.getDocID())) {
				acum.put(p.getDocID(), acum.get(p.getDocID()) + tfidf);
			} else {
				acum.put(p.getDocID(), tfidf);
			}
			
			// Si quedan postings por recorrer se itera con next,
			// en caso contrario se borra la entrada de la hash del "heap"
			if (pls_iter.get(numQ).hasNext())
				ps.put(numQ, pls_iter.get(numQ).next());
			else
				ps.remove(numQ);
			
			// Se asigna como "menor" el mayor documento para que se vuelva a iterar
			// y coger el menor docID de nuevo
			minDocID = this.index.numDocs();
			
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
