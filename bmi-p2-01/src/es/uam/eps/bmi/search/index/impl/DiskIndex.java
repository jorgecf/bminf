package es.uam.eps.bmi.search.index.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import es.uam.eps.bmi.search.index.AbstractIndex;
import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.impl.RAMPostingsList;

public class DiskIndex extends AbstractIndex {

	// private Hashtable<String, PostingsList> dictionary;
	// private List<String> dictionary;
	private HashMap<String, Integer> dictionary;
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
		RandomAccessFile pf = new RandomAccessFile(new File(this.indexFolder + Config.postingsFileName), "r");
		RAMPostingsList pl = new RAMPostingsList();

		int pos = this.dictionary.get(term);
		pf.seek(pos);
		pl.stringToPosting(pf.readLine());

		pf.close();
		return pl;

	}

	@Override
	public Collection<String> getAllTerms() throws IOException {
		return this.dictionary.keySet();
	}

	@Override
	public long getTotalFreq(String term) throws IOException {

		// numero de veces que aparece "word" en todos los documentos
		RAMPostingsList pl = (RAMPostingsList) this.getPostings(term);

		int res = 0;
		for (Posting p : pl) {
			res += p.getFreq();
		}

		return res;
	}

	@Override
	public long getDocFreq(String term) throws IOException {
		// numero de documentos donde aparece term: longitud de su posting list
		return this.getPostings(term).size();
	}

	@Override
	public void load(String path) throws IOException {

		this.dictionary = new HashMap<>();
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
				String[] data = sCurrentLineDicc.split(" ");
				this.dictionary.put(data[0], Integer.valueOf(data[1]));

				// TODO if data size no es 2 error
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getDocPath(int docID) throws IOException {
		return this.paths.get(docID);
	}

	@Override
	public double getDocNorm(int docID) throws IOException {
		return this.docNorms[docID];
	}

}
