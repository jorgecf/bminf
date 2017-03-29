package es.uam.eps.bmi.search.proximal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.positional.PositionalPosting;
import es.uam.eps.bmi.search.index.structure.positional.PositionsIterator;
import es.uam.eps.bmi.search.index.structure.positional.lucene.LucenePositionalPostingsList;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.impl.RankingImpl;
import es.uam.eps.bmi.search.vsm.AbstractVSMEngine;

/**
 * Engine de busqueda que realiza una busqueda proximal (por intervalos).
 * 
 * @author Alejandro Martin
 * @author Jorge Cifuentes
 *
 */
public class ProximalEngine extends AbstractVSMEngine {

	public ProximalEngine(Index index) {
		super(index);
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {

		String[] terms;
		boolean flagLiteral = false;

		// Primero comprobamos si es una consulta literal
		if (query.charAt(0) == '"' && query.charAt(query.length() - 1) == '"') {
			terms = query.replaceAll("\"", "").split(" ");
			flagLiteral = true;
		} else {
			terms = query.split(" ");
		}

		RankingImpl ranking = new RankingImpl(index, cutoff);

		for (int doc = 0; doc < index.numDocs(); doc++) {

			// busqueda proximal
			ArrayList<Integer> a = new ArrayList<>();
			ArrayList<Integer> b = new ArrayList<>();

			// busqueda literal
			ArrayList<Integer> a_lit = new ArrayList<>();
			ArrayList<Integer> b_lit = new ArrayList<>();

			a.add(-1); // -infinito
			b.add(-1);

			a_lit.add(-1);
			b_lit.add(-1);

			int i = 1;

			// bucle principal
			while (true) {
				ArrayList<LucenePositionalPostingsList> pl = new ArrayList<>();
				ArrayList<LucenePositionalPostingsList> pl2 = new ArrayList<>();

				for (String t : terms) {
					pl.add((LucenePositionalPostingsList) this.index.getPostings(t));
					pl2.add((LucenePositionalPostingsList) this.index.getPostings(t));
				}

				// calculamos el valor de b
				int max_b = -1;
				Iterator<LucenePositionalPostingsList> it = pl.iterator();
				while (it.hasNext()) {
					int maxAux = -1;

					Iterator<Posting> itpos = it.next().iterator();
					while (itpos.hasNext()) {
						Posting q = itpos.next();
						PositionsIterator pi = (PositionsIterator) ((PositionalPosting) q).iterator();

						if (q.getDocID() == doc)
							maxAux = pi.nextAfter(a.get(i - 1));
					}

					if (maxAux > max_b) {
						max_b = maxAux;
					}

				}

				if (max_b >= Integer.MAX_VALUE) {
					b.add(-1); // "infinito"
					break;
				} else if (max_b == -1) { // no encontrado
					break;
				} else {
					b.add(max_b);
				}

				// calculamos el valor de a
				int min_a = max_b;
				Iterator<LucenePositionalPostingsList> it2 = pl2.iterator();
				while (it2.hasNext()) {
					int minAux = -1;

					Iterator<Posting> itpos2 = it2.next().iterator();
					while (itpos2.hasNext()) {
						Posting q = itpos2.next();
						PositionsIterator pi = (PositionsIterator) ((PositionalPosting) q).iterator();

						if (q.getDocID() == doc)
							minAux = pi.nextBefore(b.get(i));
					}

					if (minAux < min_a) {
						min_a = minAux;
					}

				}

				if (min_a == -1) {
					break; // no hay ningun intervalo posible
				}

				a.add(min_a);

				// Si hay busqueda literal, solo si cumplen literalidad se
				// agregan
				if (flagLiteral == true) {
					if (comprobarLiteral(min_a, doc, terms) == true) {
						a_lit.add(min_a);
						b_lit.add(max_b);
					}
				}

				i++;
			}

			// calculamos la score
			double score = 0;

			if (flagLiteral == true) {
				score = calculaScore(a_lit, b_lit, terms.length);
			} else {
				score = calculaScore(a, b, terms.length);
			}

			if (score > 0)
				ranking.add(doc, score);

		}

		return ranking;
	}

	/**
	 * Calcula el score de un documento partido en intervalos proximales.
	 * 
	 * @param a
	 *            Lista de inicios de intervalo.
	 * @param b
	 *            Lista de finales de intervalo.
	 * @param terms
	 *            Terminos el doc.
	 * @return Score calculada.
	 */
	private double calculaScore(ArrayList<Integer> a, ArrayList<Integer> b, int termsLength) {

		double score = 0;
		for (int j = 1; j < a.size(); j++) {
			score += (double) 1 / ((b.get(j) - a.get(j)) - termsLength + 2);
		}

		return score;
	}

	/**
	 * Comprueba si un intervalo cumple literalidad.
	 * 
	 * @param min_a
	 *            Primer valor del intervalo.
	 * @param doc
	 *            docID.
	 * @param terms
	 *            Terminos del documento.
	 * @return True si lo cumple, false sino
	 * @throws IOException
	 */
	private boolean comprobarLiteral(int min_a, int doc, String[] terms) throws IOException {

		ArrayList<LucenePositionalPostingsList> pl = new ArrayList<>();

		for (String t : terms) {
			pl.add((LucenePositionalPostingsList) this.index.getPostings(t));
		}

		Iterator<LucenePositionalPostingsList> it = pl.iterator();

		int pos = min_a;
		while (it.hasNext()) {

			Iterator<Posting> itpos = it.next().iterator();
			while (itpos.hasNext()) {

				Posting q = itpos.next();
				PositionsIterator pi = (PositionsIterator) ((PositionalPosting) q).iterator();

				if (q.getDocID() == doc) {

					boolean contains = false;

					while (pi.hasNext()) {
						if (pi.next() == pos) {
							contains = true;
							break;
						}
					}

					if (contains == false) {
						return false;
					}

					break;
				}
			}

			pos++;
		}

		return true;

	}
}