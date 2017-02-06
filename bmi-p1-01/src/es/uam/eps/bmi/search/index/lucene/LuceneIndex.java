package es.uam.eps.bmi.search.index.lucene;

import java.io.IOException;
import java.util.List;

import es.uam.eps.bmi.search.index.AbstractIndex;
import es.uam.eps.bmi.search.index.freq.FreqVector;


public class LuceneIndex extends AbstractIndex{

	private String path; // ruta al indice
	//private String content;
	
	
	public LuceneIndex(String path) throws IOException {
		super(path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void load(String s) {
		// cargar el indice
		// TODO Auto-generated method stub
		// 
	}

	@Override
	public List<String> getAllTerms() {
		// cargar terminos del indice
		// TODO Auto-generated method stub
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
