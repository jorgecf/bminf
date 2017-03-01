package es.uam.eps.bmi.search.index.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

import es.uam.eps.bmi.search.index.AbstractIndex;
import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.impl.RAMPostingsList;

public class SerializedRAMIndex extends AbstractIndex {

	// private IndexReader index;
	private Hashtable<String, PostingsList> dictionary;
	private int numDocs;
	private List<String> paths;

	public SerializedRAMIndex(String path) throws IOException {
		super(path);
	}

	public Hashtable<String, PostingsList> getDictionary() {
		return dictionary;
	}

	@Override
	public int numDocs() {
		// return this.index.numDocs();
		return this.numDocs;
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
		// return index.totalTermFreq(new Term("content", term));

		// numero de veces que aparece "word" en todos los documentos

		RAMPostingsList pl = (RAMPostingsList) this.dictionary.get(term);

		int res = 0;
		for (Posting p : pl) {
			res += p.getFreq();
		}

		return res;
	}

	@Override
	public long getDocFreq(String term) throws IOException {
		// return index.docFreq(new Term("content", term));

		// numero de documentos donde aparece: longitud de su posting list
		return this.dictionary.get(term).size();
	}

	@Override
	public void load(String path) throws IOException {
		/*
		 * try { index =
		 * DirectoryReader.open(FSDirectory.open(Paths.get(path)));
		 * loadNorms(path); } catch (IndexNotFoundException ex) { throw new
		 * NoIndexException(path); }
		 */

		this.dictionary = new Hashtable<String, PostingsList>();
		this.paths = new ArrayList<>();

		BufferedReader br = null;
		FileReader fr = null;

		try {

			// fr = new FileReader(path + "/index.txt");
			// br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(path + "/index.txt"));

			// this.numDocs = Integer.valueOf(br.readLine());

			while ((sCurrentLine = br.readLine()) != null) {
				this.deserializeIndex(sCurrentLine);
				// if (sCurrentLine.contains("information"))
				// System.out.println("leido= " + sCurrentLine);

			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

		// cargamos los paths
		BufferedReader br2 = null;
		FileReader fr2 = null;

		try {

			// fr2 = new FileReader(path + Config.pathsFileName);
			// br2 = new BufferedReader(fr2);

			String sCurrentLine;

			br2 = new BufferedReader(new FileReader(path + Config.pathsFileName));

			while ((sCurrentLine = br2.readLine()) != null) {
				// System.out.println("leido= " + sCurrentLine);
				this.paths.add(sCurrentLine);
				this.numDocs++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// cargamos las normas
		this.loadNorms(path);

	}

	private void deserializeIndex(String indexLine) {

		String[] l = indexLine.split(" ");
		RAMPostingsList pl = new RAMPostingsList();

		int i = 3;
		int docId = -1;
		int freq = -1;

		while (l.length >= i) {

			docId = Integer.parseInt(l[i - 2]);
			freq = Integer.parseInt(l[i - 1]);
			i += 2;

			pl.add(docId, freq);
		}

		// lo restablecemos en el diccionario
		this.dictionary.put(l[0], pl);

	}

	@Override
	public String getDocPath(int docID) throws IOException {
		// return index.document(docID).get("path");

		return this.paths.get(docID);
	}

	@Override
	public double getDocNorm(int docID) throws IOException {
		return docNorms[docID];
	}

}
