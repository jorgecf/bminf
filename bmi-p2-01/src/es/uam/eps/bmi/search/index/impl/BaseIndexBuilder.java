package es.uam.eps.bmi.search.index.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import es.uam.eps.bmi.search.index.AbstractIndexBuilder;
import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.impl.RAMPostingsList;

/**
 * Representa un Builder base de indices para los indices implementados,
 * agrupando las funciones basicas para evitar repeticion de codigo.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public abstract class BaseIndexBuilder extends AbstractIndexBuilder {

	protected int docId = 0;
	protected String indexFolder;
	protected LinkedHashMap<String, PostingsList> dictionary;

	/**
	 * Guarda el diccionario, de acuerdo a la estrategia que se vaya a seguir.
	 * 
	 * @param indexPath
	 *            Ruta donde guardar el diccionario.
	 * 
	 * @throws IOException
	 */
	protected abstract void serializeIndex(String indexPath) throws IOException;

	public BaseIndexBuilder() {
		super();
		this.dictionary = new LinkedHashMap<>();
	}

	@Override
	public void build(String collectionPath, String indexPath) throws IOException {

		this.indexFolder = indexPath;

		// creamos las carpetas necesarias
		File indexDir = new File(indexPath);
		indexDir.mkdir();

		File dic = new File(indexPath + Config.dictionaryFileName);
		dic.getParentFile().mkdirs();
		dic.createNewFile();

		File pst = new File(indexPath + Config.postingsFileName);
		pst.getParentFile().mkdirs();
		pst.createNewFile();

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
		
		FileWriter txtHeapLaw = new FileWriter("heapLaw.txt", true);
		PrintWriter pw = new PrintWriter(txtHeapLaw);

		List<String> terms = Arrays.asList(text.replaceAll("[^A-Za-z0-9 ]", " ").toLowerCase().split(" "));

		Set<String> set = new HashSet<String>(terms);
		String[] termsUnique = set.toArray(new String[0]);
		
		pw.println(terms.size() + "\t" + termsUnique.length);
		txtHeapLaw.close();
		pw.close();

		for (String term : termsUnique) {
			this.putDictionary(term, docId, Collections.frequency(terms, term));
		}

		docId++;

		// guardamos su ruta
		File pathFile = new File(this.indexFolder + Config.pathsFileName);

		if (pathFile.exists() == false) {
			pathFile.getParentFile().mkdirs();
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
