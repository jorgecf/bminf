package es.uam.eps.bmi.search.index.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;

import es.uam.eps.bmi.search.index.AbstractIndex;
import es.uam.eps.bmi.search.index.freq.FreqVector;

public class LuceneIndex extends AbstractIndex {

	private IndexReader idxReader;
	private String indexPath;

	private String path; // ruta al indice
	// private String content;

	public LuceneIndex(String path) throws IOException {
		super(path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void load(String iPath) {

		// cargar el indice

		this.indexPath = iPath;

		Path path = Paths.get(iPath);

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

	@Override
	public List<String> getAllTerms() {

		// cargar terminos del indice

		List<String> ret;

		return null;
	}

	@Override
	public int getTermTotalFreq(String s) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FreqVector getDocVector(int docID) {
		// TODO Auto-generated method stub
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

	public void setPath(String path) {
		this.path = path;
	}

}
