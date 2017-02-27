package es.uam.eps.bmi.search.index.structure.ram;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;

public class RAMPostingsList implements PostingsList {

	private List<Posting> pl;
	//private int size;

	public RAMPostingsList() {
		super();
		this.pl = new ArrayList<Posting>();
	}
	
	public void add(int docID, int freq) {
		Posting p = new Posting(docID, freq);
		pl.add(p);
	}



	@Override
	public Iterator<Posting> iterator() {
		return this.pl.iterator();
	}

	@Override
	public int size() {
		return this.pl.size();
	}

}
