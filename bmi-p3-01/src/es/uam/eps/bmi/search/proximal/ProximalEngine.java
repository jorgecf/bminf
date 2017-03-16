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

public class ProximalEngine extends AbstractVSMEngine {

	public ProximalEngine(Index index) {
		super(index);
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {

		String[] terms = query.split(" ");
		RankingImpl ranking = new RankingImpl(index, cutoff);

		for (int doc = 0; doc < index.numDocs(); doc++) {

			// busqueda proximal
			ArrayList<Integer> a = new ArrayList<>();
			ArrayList<Integer> b = new ArrayList<>();

			a.add(-1); // - infinito
			b.add(-1);

			int i = 1;

			// TODO loop
			while (true) {
				// TODO sacar fuera y reset iter??
				ArrayList<LucenePositionalPostingsList> pl = new ArrayList<>();
				ArrayList<LucenePositionalPostingsList> pl2 = new ArrayList<>();

				for (String t : terms) {
					pl.add((LucenePositionalPostingsList) this.index.getPostings(t));
					pl2.add((LucenePositionalPostingsList) this.index.getPostings(t));
				}

				// b
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

				if (max_b >= Integer.MAX_VALUE) { // b.get(i - 1)) {
					b.add(-1); // "infinito"
					break;
				} else if (max_b == -1) {
					break;
				} else {
					b.add(max_b);
				}

				// a
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
				a.add(min_a);

				i++;
			}

			// calculamos la score
			double score = 0;
			for (int j = 1; j < a.size() - 1; j++) {
				score += (double) 1 / ((b.get(j) - a.get(j)) - terms.length + 2);
			}
			if (score > 0)
				ranking.add(doc, score);

			System.out.println("ProximalEngine.search() score doc " + doc + " = " + score);
		}

		return ranking;
	}
}