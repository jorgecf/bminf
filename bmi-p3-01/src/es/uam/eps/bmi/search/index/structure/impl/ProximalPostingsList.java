package es.uam.eps.bmi.search.index.structure.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.positional.PositionalPosting;
import es.uam.eps.bmi.search.index.structure.positional.PositionsIterator;

public class ProximalPostingsList implements PostingsList {

	List<PositionalPosting> data;

	public ProximalPostingsList() {
		super();
		this.data = new ArrayList<>();
	}

	@Override
	public Iterator<Posting> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return this.data.size();
	}

	public int getMinPosition(int docID, int fromLow) {
		// return Collections.min(this.data);
		for (PositionalPosting p : this.data) {
			if (p.getDocID() == docID) {
				//PositionsIterator pi = new PositionsIterator(p);
				return ((PositionsIterator) p.iterator()).nextAfter(fromLow);
			}
		}

		return -1;
	}

	public int getMaxPosition(int docID, int toHigh) {
		
		for (PositionalPosting p : this.data) {
			if (p.getDocID() == docID) {
				//PositionsIterator pi = new PositionsIterator(p);
				return ((PositionsIterator) p.iterator()).nextBefore(toHigh);
			}
		}

		return -1;
	}

}
