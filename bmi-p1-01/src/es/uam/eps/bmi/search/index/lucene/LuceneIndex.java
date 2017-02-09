package es.uam.eps.bmi.search.index.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.jsoup.nodes.Document;

import es.uam.eps.bmi.search.index.AbstractIndex;
import es.uam.eps.bmi.search.index.freq.FreqVector;
import es.uam.eps.bmi.search.index.freq.lucene.LuceneFreqVector;
import es.uam.eps.bmi.search.index.freq.lucene.LuceneFreqVectorIterator;
import es.uam.eps.bmi.search.index.freq.lucene.LuceneTermFreq;

public class LuceneIndex extends AbstractIndex {

	private IndexReader idxReader;
	private String indexPath;

	private String path; // ruta al indice
	// private String content;

	public LuceneIndex(String path) throws IOException {
		super(path);
	}

	@Override
	public void load(String iPath) {

		Path path = Paths.get(iPath);
		this.indexPath = iPath;

		/*
		 * Creamos un FSDirectory a partir de la ruta pasada y lo abrimos en el
		 * indexReader
		 */
		try {
			Directory directory = FSDirectory.open(path);
			this.idxReader = DirectoryReader.open(directory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> getRawTerms() {

		List<String> ret = new ArrayList<String>();

		for (int i = 0; i < idxReader.numDocs(); i++) {

			// TODO if (idxReader.isDeleted(i))
			// continue;

			org.apache.lucene.document.Document doc;
			try {
				doc = idxReader.document(i);
				String[] s = doc.getValues("content");

				for (String s1 : s) {
					ret.addAll(new ArrayList<String>(Arrays.asList(s1.split(" "))));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/* eliminamos tokens innecesarios y mayusculas */
		List<String> res = ret.stream().map(str -> str.replaceAll("[{}*//()@;]", "").toLowerCase())
				.filter(str -> str.length() > 0).sorted(String::compareTo).collect(Collectors.toList());

		return res;
	}

	@Override
	public List<String> getAllTerms() {
		return this.getRawTerms().stream().distinct().collect(Collectors.toList());
	}

	@Override
	public int getTermTotalFreq(String s) {
		List<String> ret = this.getRawTerms();
		return Collections.frequency(ret, s);
	}

	@Override
	public FreqVector getDocVector(int docID) {

		LuceneTermFreq ltf;
		LuceneFreqVector lfv;
		LuceneFreqVectorIterator ltfi;

		TermsEnum t;

		
		/*
		try {
			t = this.idxReader.getTermVector(docID, "content").iterator();
			ltf = new LuceneTermFreq(t);
		} catch (IOException e) {
			e.printStackTrace();
		}
		 */
		// ltfi = new LuceneFreqVectorIterator(t);
		// lfv = new LuceneFreqVector();
		
		
		
		

		return null;
	}

	@Override
	public String getDocPath(int doc) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPath() {
		return path;
	}

	@Override
	public String getTermFreq(String word, int docID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTermDocFreq(String word) {
		// TODO Auto-generated method stub
		return null;
	}
}
