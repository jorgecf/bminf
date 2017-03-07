package es.uam.eps.bmi.search.index.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.uam.eps.bmi.search.index.AbstractIndexBuilder;
import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.impl.RAMPostingsList;

public class DiskIndexBuilder extends AbstractIndexBuilder implements Serializable {

	private static final long serialVersionUID = 1L;
	private String indexFolder;
	private int docId = 0;
	private PrintStream psDicc;
	private PrintStream psPosting;

	// private Hashtable<String, PostingsList> dictionary;
	private LinkedHashMap<String, PostingsList> dictionary;

	public DiskIndexBuilder() {
		this.dictionary = new LinkedHashMap<String, PostingsList>();
	}

	public void build(String collectionPath, String indexPath) throws IOException {

		this.indexFolder = indexPath;

		// creamos las carpetas necesarias
		File indexDir = new File(indexPath);
		indexDir.mkdir();

		//

		File dic = new File(indexPath + Config.dictionaryFileName);
		dic.getParentFile().mkdirs();
		if (dic.delete()) {
			System.out.println("DEL");
		}
		dic.createNewFile();

		File pst = new File(indexPath + Config.postingsFileName);
		pst.getParentFile().mkdirs();
		if (pst.delete()) {
			System.out.println("DEL2");
		}
		pst.createNewFile();

		FileOutputStream os = new FileOutputStream(indexPath + Config.dictionaryFileName, false);
		psDicc = new PrintStream(os, true, "UTF-8");

		FileOutputStream os2 = new FileOutputStream(indexPath + Config.postingsFileName, false);
		psPosting = new PrintStream(os2, true, "UTF-8");

		File f = new File(collectionPath);
		if (f.isDirectory())
			indexFolder(f);
		else if (f.getName().endsWith(".zip"))
			indexZip(f);
		else
			indexURLs(f);

		// printeamos las postings list
		int offset = 0;
		for (Entry<String, PostingsList> t : this.dictionary.entrySet()) {
			psDicc.println(t.getKey() + " " + offset); // termino y su offset
			offset += 1 + t.getValue().toString().getBytes().length;
			psPosting.println(t.getValue());
		}

		psDicc.close();
		psPosting.close();

		// serializamos el indice
		// this.serializeIndex(indexPath);
		this.saveDocNorms(indexFolder);

	}

	@Override
	protected void indexText(String text, String path) throws IOException {

		List<String> terms = Arrays.asList(text.replaceAll("[^A-Za-z0-9 ]", " ").toLowerCase().split(" "));

		Set<String> set = new HashSet<String>(terms);
		String[] termsUnique = set.toArray(new String[0]);

		for (String term : termsUnique) {

			// if (term.equals("information")) {
			// System.out.println("TEST");
			// }

			// guardamos el termino
			// if (this.dictionary.containsKey(term) == false) {
			// psDicc.println(term);
			// }

			// guardamos su lista de postings

			// TODO TODO TODO escribir posting a posting

			this.putDictionary(term, docId, Collections.frequency(terms, term));

		}

		docId++;

		// guardamos su ruta
		File pathFile = new File(this.indexFolder + Config.pathsFileName);

		if (pathFile.exists() == false) {
			pathFile.createNewFile();
		}

		try {
			Path p = Paths.get(pathFile.toURI());
			path += System.lineSeparator();
			Files.write(p, path.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected Index getCoreIndex() throws IOException {
		return new DiskIndex(indexFolder);
	}

	public void putDictionary(String term, int docID, int freq) {

		if (this.dictionary.containsKey(term) == false) {

			RAMPostingsList pl = new RAMPostingsList();

			pl.add(docID, freq);
			this.dictionary.put(term, pl);
		} else {
			RAMPostingsList pl = (RAMPostingsList) this.dictionary.get(term);
			pl.add(docID, freq);
		}
	}

}
