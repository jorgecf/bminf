package es.uam.eps.bmi.search.index.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import es.uam.eps.bmi.search.index.AbstractIndexBuilder;
import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.impl.RAMPostingsList;

public class SerializedRAMIndexBuilder extends AbstractIndexBuilder implements Serializable {

	private static final long serialVersionUID = 1L;
	private String indexFolder;
	private int docId = 0;

	private Hashtable<String, PostingsList> dictionary;

	public SerializedRAMIndexBuilder() {
		this.dictionary = new Hashtable<String, PostingsList>();
	}

	@Override
	public void build(String collectionPath, String indexPath) throws IOException {

		this.indexFolder = indexPath;

		// creamos las carpetas necesarias
		File indexDir = new File(indexPath);
		indexDir.mkdir();

		File f = new File(collectionPath);
		if (f.isDirectory())
			indexFolder(f);
		else if (f.getName().endsWith(".zip"))
			indexZip(f);
		else
			indexURLs(f);

		// serializamos el indice
		this.serializeIndex(indexPath);
		this.saveDocNorms(indexFolder);
	}

	@Override
	protected void indexText(String text, String path) throws IOException {

		List<String> terms = Arrays.asList(text.replaceAll("[^A-Za-z0-9 ]", " ").toLowerCase().split(" "));

		Set<String> set = new HashSet<String>(terms);
		String[] termsUnique = set.toArray(new String[0]);

		for (String term : termsUnique) {
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

	@SuppressWarnings("unused")
	private void writeIndex(String indexPath) {

		// creamos la carpeta del indice
		File indexDir = new File(indexPath);
		indexDir.mkdir();

		// escribimos los terminos y los postings
		FileWriter file = null;
		FileWriter file2 = null;

		try {
			file = new FileWriter(indexPath + Config.dictionaryFileName);
			file2 = new FileWriter(indexPath + Config.postingsFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(file);
		PrintWriter pw2 = new PrintWriter(file2);

		for (String term : this.dictionary.keySet()) {
			pw.println(term);
			pw2.println(this.dictionary.get(term));
		}

		pw.close();
		pw2.close();

	}

	private void serializeIndex(String indexPath) {
		try {

			// serializar los terminos del diccionario
			FileOutputStream fileOut = new FileOutputStream(indexPath + Config.dictionaryFileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			// cast de Object[] a String[], para serializar los terminos
			String[] temrSer = this.dictionary.keySet().toArray(new String[this.dictionary.keySet().size()]);
			out.writeObject(temrSer);
			out.close();
			fileOut.close();

			// serializar las postingslist del diccionario
			FileOutputStream fileOut2 = new FileOutputStream(indexPath + Config.postingsFileName);
			ObjectOutputStream out2 = new ObjectOutputStream(fileOut2);

			// cast de Object[] a RAMPostingsList[], para serializar las
			// postingslist del diccionario
			RAMPostingsList[] plsSer = this.dictionary.values()
					.toArray(new RAMPostingsList[this.dictionary.values().size()]);

			out2.writeObject(plsSer);
			out2.close();
			fileOut2.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

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

	@Override
	protected Index getCoreIndex() throws IOException {
		return new SerializedRAMIndex(indexFolder);
	}

}
