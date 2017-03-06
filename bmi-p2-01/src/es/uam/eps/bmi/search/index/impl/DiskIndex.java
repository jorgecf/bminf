package es.uam.eps.bmi.search.index.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Stream;

import es.uam.eps.bmi.search.index.AbstractIndex;
import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.impl.RAMPostingsList;

public class DiskIndex extends AbstractIndex {

	// private Hashtable<String, PostingsList> dictionary;
	private List<String> dictionary;
	private int numDocs;
	private List<String> paths;

	public DiskIndex(String path) throws IOException {
		super(path);
	}

	@Override
	public int numDocs() {
		return this.numDocs;
	}

	@Override
	public PostingsList getPostings(String term) throws IOException {
		// return this.dictionary.get(term);

		int line = this.dictionary.indexOf(term);
		RAMPostingsList pl = new RAMPostingsList();

		Stream<String> lines = Files.lines(Paths.get(indexFolder + Config.postingsFileName));
		// line32 = lines.skip(31).findFirst().get();
		String parse = lines.skip(line).findFirst().get();
		pl.stringToPosting(parse);
		return pl;

	}

	@Override
	public Collection<String> getAllTerms() throws IOException {
		// return this.dictionary.keySet();
		return this.dictionary;
	}

	@Override
	public long getTotalFreq(String term) throws IOException {

		/*
		 * // numero de veces que aparece "word" en todos los documentos
		 * RAMPostingsList pl = (RAMPostingsList) this.dictionary.get(term);
		 * 
		 * int res = 0; for (Posting p : pl) { res += p.getFreq(); }
		 * 
		 * return res;
		 */
		return -1;
	}

	@Override
	public long getDocFreq(String term) throws IOException {
		// numero de documentos donde aparece term: longitud de su posting list
		// return this.dictionary.get(term).size();
		return this.getPostings(term).size();
	}

	@Override
	public void load(String path) throws IOException {

		this.dictionary = new ArrayList<String>();
		this.paths = new ArrayList<>();

		this.indexFolder = path;
		this.readIndex(indexFolder);

		// cargamos los paths
		BufferedReader br2 = null;

		try {
			String sCurrentLine;

			br2 = new BufferedReader(new FileReader(path + Config.pathsFileName));

			while ((sCurrentLine = br2.readLine()) != null) {
				this.paths.add(sCurrentLine);
				this.numDocs++; // numero de docs leidos
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.loadNorms(path);

	}

	private void readIndex(String indexPath) {

		// cargamos los terminos del diccionario
		BufferedReader brdicc = null;

		try {
			String sCurrentLineDicc;
			brdicc = new BufferedReader(new FileReader(this.indexFolder + Config.dictionaryFileName));

			while ((sCurrentLineDicc = brdicc.readLine()) != null) {
				this.dictionary.add(sCurrentLineDicc);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

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
