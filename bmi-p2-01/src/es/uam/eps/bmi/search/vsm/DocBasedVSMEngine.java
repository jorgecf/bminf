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

/**
 * Engine de busqueda implementada por el metodo de modelo vectorial orientado a
 * documentos.
 * 
 * @author Alejandro Martin
 * @author Jorge Cifuentes
 */
public class DocBasedVSMEngine extends AbstractVSMEngine {

	public DocBasedVSMEngine(Index idx) {
		super(idx);
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {

		String[] terms = query.split(" ");

		// en acum se iran guardando la suma del tfidf para cada doc, por lo que
		// al final
		// tendra toda la parte de arriba del coseno de similitud
		Hashtable<Integer, Double> acum = new Hashtable<>();
		RankingImpl ranking = new RankingImpl(index, cutoff);

		ArrayList<PostingsList> pls = new ArrayList<PostingsList>();
		ArrayList<Iterator<Posting>> pls_iter = new ArrayList<Iterator<Posting>>();

		// obtenemos los postings y los iteradores para cada termino
		for (int i = 0; i < terms.length; i++) {
			PostingsList pl = this.index.getPostings(terms[i]);

			pls.add(pl);
			pls_iter.add(pl.iterator());
		}

		// tabla hash para mantener los postings activos
		Hashtable<Integer, Posting> ps = new Hashtable<Integer, Posting>();

		// obtenemos los primeros postings
		for (int i = 0; i < pls_iter.size(); i++) {
			Posting p = (Posting) pls_iter.get(i).next();
			ps.put(i, p);
		}

		int minDocID = this.index.numDocs(); // minimo docID
		int numQ = 0; // numero de Query correspondiente al minimo docID

		// se recorren los postings hasta que todos queden vacios
		while (ps.size() != 0) {

			// obtenemos documento y query con menor docID
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

			// si quedan postings por recorrer se itera con next,
			// en caso contrario se borra la entrada de la hash
			if (pls_iter.get(numQ).hasNext())
				ps.put(numQ, pls_iter.get(numQ).next());
			else
				ps.remove(numQ);

			// se asigna como "menor" el mayor documento para que se vuelva a
			// iterar
			// y coger el menor docID de nuevo
			minDocID = this.index.numDocs();
		}

		// dividimos cada valor entre el modulo del documento
		Enumeration<Integer> e = acum.keys();
		while (e.hasMoreElements()) {
			int key = (int) e.nextElement();

			Double score = acum.get(key) / this.index.getDocNorm(key);
			ranking.add(key, score);
		}

		return ranking;
	}
}
