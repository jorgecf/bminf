package es.uam.eps.bmi.search.index.structure.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;

public class RAMPostingsList implements PostingsList, Serializable {

	private List<Posting> pl;

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

	@Override
	public String toString() {
		String s = "";

		for (Posting p : pl) {
			s += p.getDocID() + " " + p.getFreq() + " ";
		}

		return s.substring(0, s.length() - 1); // delete trailing space
	}

	public void stringToPosting(String input) {

		// RAMPostingsList pl = new RAMPostingsList();
		String[] l = input.split(" ");

		// 3 2 4 87
		int i = 2;
		int docId = -1;
		int freq = -1;

		while (l.length >= i) {

			docId = Integer.parseInt(l[i - 2]);
			freq = Integer.parseInt(l[i - 1]);
			i += 2;

			this.add(docId, freq);
		}

	}
}
