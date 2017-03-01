package es.uam.eps.bmi.search.index.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;

import es.uam.eps.bmi.search.index.AbstractIndexBuilder;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.PostingsList;
import es.uam.eps.bmi.search.index.structure.ram.RAMPostingsList;

public class SerializedRAMIndexBuilder extends AbstractIndexBuilder {

	private static FieldType type;
	private IndexWriter builder;
	private String indexFolder;
	// private Index index;
	private int docId = 0;
	private Hashtable<String, PostingsList> dictionary;

	public SerializedRAMIndexBuilder() {
		type = new FieldType();
		type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		this.dictionary = new Hashtable<String, PostingsList>();
	}

	@Override
	public void build(String collectionPath, String indexPath) throws IOException {

		this.indexFolder = indexPath;

		// creamos el indice
		// this.index = this.getCoreIndex();

		// abrimos un Directory en RAM
		RAMDirectory dir = new RAMDirectory();

		IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
		iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

		builder = new IndexWriter(dir, iwc);

		File f = new File(collectionPath);
		if (f.isDirectory())
			indexFolder(f); // A directory containing text files.
		else if (f.getName().endsWith(".zip"))
			indexZip(f); // A zip file containing compressed text files.
		else
			indexURLs(f); // A file containing a list of URLs.

		builder.close();
		// saveDocNorms(indexFolder);

		this.serializeIndex(indexPath);
	}

	private void serializeIndex(String indexPath) {

		// creamos la carpeta
		File txtDir = new File(indexPath);
		txtDir.mkdir();

		FileWriter file = null;
		try {
			file = new FileWriter(indexPath + "/index.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(file);

		for (String term : this.dictionary.keySet()) {
			pw.println(term + " -> " + this.dictionary.get(term));
		}

		pw.close();
	}

	@Override
	protected void indexText(String text, String path) throws IOException {
		/*
		 * Document doc = new Document(); Field pathField = new
		 * StringField("path", path, Field.Store.YES); doc.add(pathField); Field
		 * field = new Field("content", text, type); doc.add(field);
		 * builder.addDocument(doc);
		 */

		List<String> terms = Arrays.asList(text.split(" "));

		// set de no repetidos
		Set<String> set = new HashSet<String>(terms);
		String[] termsUnique = set.toArray(new String[0]);

		for (String term : termsUnique) {
			this.putDictionary(term, ++docId, Collections.frequency(terms, term));
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
