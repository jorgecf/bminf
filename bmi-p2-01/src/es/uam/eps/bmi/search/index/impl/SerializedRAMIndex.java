package es.uam.eps.bmi.search.index.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.naming.SizeLimitExceededException;

import es.uam.eps.bmi.search.index.AbstractIndex;
import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.impl.RAMPostingsList;

public class SerializedRAMIndex extends AbstractIndex {

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
		// numero de documentos donde aparece term: longitud de su posting list
		return this.dictionary.get(term).size();
	}

	@Override
	public void load(String path) throws IOException {

		this.dictionary = new Hashtable<String, PostingsList>();
		this.paths = new ArrayList<>();

		// cargamos el indice
		this.deserializeIndex(path);

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

		// por ultimo, cargamos las normas
		this.loadNorms(path);
	}

	private void readIndex(String indexPath) {

		// cargamos los terminos del diccionario y las posting list
		BufferedReader brdicc = null;
		BufferedReader brpost = null;

		try {
			String sCurrentLineDicc;
			String sCurrentLinePost;
			brdicc = new BufferedReader(new FileReader(this.indexFolder + Config.dictionaryFileName));
			brpost = new BufferedReader(new FileReader(this.indexFolder + Config.postingsFileName));

			while (((sCurrentLineDicc = brdicc.readLine()) != null)
					&& ((sCurrentLinePost = brpost.readLine()) != null)) {
				this.readIndexAux(sCurrentLineDicc + " " + sCurrentLinePost);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void readIndexAux(String indexLine) {

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

	private void deserializeIndex(String indexPath) {
		// abrir archivo en path + Config.dictionaryfilename-> terminos
		// serializado
		// abrir archivo en path + Config.postingfilename-> postings serializado

		// meter en hashtable
		try {
			
			// deserialiar los terminos del diccionario
			FileInputStream fileIn = new FileInputStream(indexPath + Config.dictionaryFileName);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        String[] termsA = (String[]) in.readObject();
	        in.close();
	        fileIn.close();
	        
	        // deserializar las postingslist del diccionario
	        FileInputStream fileIn2 = new FileInputStream(indexPath + Config.postingsFileName);
	        ObjectInputStream in2 = new ObjectInputStream(fileIn2);
	        RAMPostingsList[] plsA = (RAMPostingsList[]) in2.readObject();
	        in2.close();
	        fileIn2.close();
	        
	        // si ambos arrays tienen la misma longitud,
	        // se introducen terminos y postingslists en el diccionario
	        if (termsA.length == plsA.length) {
	        	for (int i = 0; i < termsA.length; i++) {
	        		this.dictionary.put(termsA[i], plsA[i]);
	        	}
			} else {
				throw new SizeLimitExceededException
					("Las dimensiones de terminos y postingslists son distintas.");
			}
			
		} catch (IOException | ClassNotFoundException | SizeLimitExceededException e) {
			e.printStackTrace();
		}
		

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
