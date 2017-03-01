package es.uam.eps.bmi.search.index.impl;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Hashtable;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;

import es.uam.eps.bmi.search.index.AbstractIndex;
import es.uam.eps.bmi.search.index.NoIndexException;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.lucene.LucenePostingsList;
import es.uam.eps.bmi.search.index.structure.ram.RAMPostingsList;

public class SerializedRAMIndex extends AbstractIndex {

	private IndexReader index;
	private Hashtable<String, PostingsList> dictionary;

	public SerializedRAMIndex(String path) throws IOException {
		super(path);
		this.dictionary = new Hashtable<String, PostingsList>();
	}

	public Hashtable<String, PostingsList> getDictionary() {
		return dictionary;
	}

	@Override
	public int numDocs() {
		return this.index.numDocs();
	}

	@Override
	public PostingsList getPostings(String term) throws IOException {
		return this.dictionary.get(term);
	}

	@Override
	public Collection<String> getAllTerms() throws IOException {
		return this.dictionary.keySet();
	}

	@Override
	public long getTotalFreq(String term) throws IOException {
		return index.totalTermFreq(new Term("content", term));
	}

	@Override
	public long getDocFreq(String term) throws IOException {
		return index.docFreq(new Term("content", term));
	}

	@Override
	public void load(String path) throws IOException {
		try {
			index = DirectoryReader.open(FSDirectory.open(Paths.get(path)));
			loadNorms(path);
		} catch (IndexNotFoundException ex) {
			throw new NoIndexException(path);
		}
	}

	@Override
	public String getDocPath(int docID) throws IOException {
		return index.document(docID).get("path");
	}

	@Override
	public double getDocNorm(int docID) throws IOException {
		return docNorms[docID];
	}

	
	
	

}
