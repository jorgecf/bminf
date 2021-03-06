package es.uam.eps.bmi.search.ui;

import java.io.IOException;

import es.uam.eps.bmi.search.ranking.SearchRankingDoc;

/**
 * Convierte cualquier objeto que herede de SearchRankingDoc en texto.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 */
public class TextResultDocRenderer extends ResultsRenderer {

	private SearchRankingDoc doc;

	public TextResultDocRenderer(SearchRankingDoc result) {
		this.doc = result;
	}

	@Override
	public String toString() {

		try {
			return "\t" + String.valueOf(doc.getScore()) + "\t" + doc.getPath();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
