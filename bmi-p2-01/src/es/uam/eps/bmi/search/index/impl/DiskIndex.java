package es.uam.eps.bmi.search.index.impl;

import java.io.IOException;
import java.util.Collection;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.PostingsList;

public class DiskIndex implements Index {

	public DiskIndex(String string) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int numDocs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PostingsList getPostings(String term) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getAllTerms() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTotalFreq(String term) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getDocFreq(String term) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void load(String path) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFolder() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocPath(int docID) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDocNorm(int docID) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
