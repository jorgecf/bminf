package es.uam.eps.bmi.search.proximal;

import java.io.IOException;
import java.util.ArrayList;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.impl.ProximalPostingsList;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.vsm.AbstractVSMEngine;

public class ProximalEngine extends AbstractVSMEngine {

	public ProximalEngine(Index index) {
		super(index);
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {

		String[] terms = query.split(" ");

		ArrayList<ProximalPostingsList> pl = new ArrayList<>();
		for (String t : terms) {
			pl.add((ProximalPostingsList) this.index.getPostings(t));
		}

		for (int doc = 0; doc < index.numDocs(); doc++) {

			// busqueda proximal
			ArrayList<Integer> a = new ArrayList<>();
			ArrayList<Integer> b = new ArrayList<>();

			// a.add(Double.NEGATIVE_INFINITY);
			a.add(-1); // - infinito
			b.add(-1);

			// TODO loop
			while (true) {
				int i = 0;
				// b
				int max_b = -1;
				for (ProximalPostingsList p : pl) {
					int maxAux = p.getMinPosition(doc, a.get(i - 1));
					if (maxAux > max_b) {
						max_b = maxAux;
					}
				}
				if (max_b == b.get(i - 1)) { // "infinito"
					b.add(-1);
					break;
				} else {
					b.add(max_b);
				}

				// a
				int min_a = max_b;
				for (ProximalPostingsList p : pl) {
					int minAux = p.getMaxPosition(doc, b.get(i));
					if (minAux < min_a) {
						min_a = minAux;
					}
				}
				a.add(min_a);

				i++;
			}

		}
		return null;
	}

}
