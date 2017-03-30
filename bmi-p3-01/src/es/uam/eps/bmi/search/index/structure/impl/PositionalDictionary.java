package es.uam.eps.bmi.search.index.structure.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import es.uam.eps.bmi.search.index.structure.EditableDictionary;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Diccionario preparado para trabajar con postings posicionales.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class PositionalDictionary implements EditableDictionary {

	private static final long serialVersionUID = 1L;

	Map<String, PositionalPostingsList> termPostings;

	public PositionalDictionary() {
		termPostings = new HashMap<String, PositionalPostingsList>();
	}

	public void add(String term, int docID, int position) {
		if (termPostings.containsKey(term))
			termPostings.get(term).add(docID, position);
		else
			termPostings.put(term, new PositionalPostingsList(docID, position));
	}

	@Override
	public PostingsList getPostings(String term) throws IOException {
		return termPostings.get(term);
	}

	@Override
	public Collection<String> getAllTerms() {
		return termPostings.keySet();
	}

	@Override
	public long getDocFreq(String term) throws IOException {
		return termPostings.get(term).size();
	}

	@Override
	public void add(String term, int docID) {
		throw new NotImplementedException();
	}
}