package es.uam.eps.bmi.search.index.structure.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.positional.PositionalPostingImpl;

public class PositionalPostingsList implements PostingsList {

	List<PositionalPostingImpl> postings;

	public PositionalPostingsList() {
		postings = new ArrayList<PositionalPostingImpl>();
	}

	public PositionalPostingsList(int docID, int position) {
		if (this.postings == null)
			this.postings = new ArrayList<PositionalPostingImpl>();
		
		this.postings.add(new PositionalPostingImpl(docID, 1, position));
	}

	@Override
	public Iterator<Posting> iterator() {
		List<Posting> lp = new ArrayList<>();

		for (PositionalPostingImpl ppi : this.postings) {
			lp.add(ppi);
		}

		return lp.iterator();
	}

	@Override
	public int size() {
		return postings.size();
	}

	// supuesto: adicion por docID's ordenados y crecientes
	public void add(int docID, int position) {

		// ya existe el posting de este termino para ese docID, actualizar
		// frecuencia y posiciones
		if (!postings.isEmpty() && docID == postings.get(postings.size() - 1).getDocID()) {
			postings.get(postings.size() - 1).addPosition(position);
		}
		// aun no existe, crearlo
		else {
			this.postings.add(new PositionalPostingImpl(docID, 1, position));
		}

	}

	public void add(int doc, long freq, List<Integer> l) {
		if (this.postings == null)
			this.postings = new ArrayList<PositionalPostingImpl>();
		
		this.postings.add(new PositionalPostingImpl(doc, freq, l));
	}

}
