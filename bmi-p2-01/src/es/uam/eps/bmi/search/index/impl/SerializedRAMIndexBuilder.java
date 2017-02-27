package es.uam.eps.bmi.search.index.impl;

import java.io.File;
import java.io.IOException;

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

public class SerializedRAMIndexBuilder extends AbstractIndexBuilder {

	private static FieldType type;
	private IndexWriter builder;
	private String indexFolder;
	private Index index;

	public SerializedRAMIndexBuilder() {
		type = new FieldType();
		type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
	}

	@Override
	public void build(String collectionPath, String indexPath) throws IOException {

		this.indexFolder = indexPath;

		// creamos el indice
		this.index = this.getCoreIndex();

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
	}

	@Override
	protected void indexText(String text, String path) throws IOException {
		Document doc = new Document();
		Field pathField = new StringField("path", path, Field.Store.YES);
		doc.add(pathField);
		Field field = new Field("content", text, type);
		doc.add(field);
		builder.addDocument(doc);

		((SerializedRAMIndex) this.index).putDictionary();
	}

	@Override
	protected Index getCoreIndex() throws IOException {
		return new SerializedRAMIndex(indexFolder);
	}

}
