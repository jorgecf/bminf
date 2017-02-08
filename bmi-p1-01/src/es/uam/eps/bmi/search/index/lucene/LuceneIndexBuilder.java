package es.uam.eps.bmi.search.index.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;

import es.uam.eps.bmi.search.index.IndexBuilder;

public class LuceneIndexBuilder implements IndexBuilder {

	private IndexWriter idxwriter;
	private String indexPath;

	/**
	 * Crea un indice a partir de unos archivos dados.
	 * 
	 * @param collectionPath
	 *            ruta de los archivos a indexar
	 * @param indexPath
	 *            ruta del indice a crear
	 * 
	 * @return
	 */
	@Override
	public void build(String collectionPath, String indexPath) throws IOException {

		/* 1) Leer los archivos de disco y cargar sus rutas */
		File collectionFile = new File(collectionPath);

		if (collectionFile.exists() == false) {
			throw new IOException();
		}

		/*
		 * 2) Crear un objeto Documento con sus Fields a partir de cada ruta
		 * almacenada
		 * 
		 */
		ArrayList<Document> documents = new ArrayList<Document>();

		/* Si es un directorio de html's cargamos cada archivo */
		if (collectionFile.isDirectory() == true) {

			for (File f : collectionFile.listFiles()) {
				documents.add(this.getDocument(Jsoup.parse(f, "UTF-8", f.getName())));
			}
		}
		/* Si es un txt con webs, cargamos cada web */
		else if (collectionFile.isFile() == true) {
			Stream<String> stream = Files.lines(Paths.get(collectionPath));
			stream.forEach(line -> {
				try {
					documents.add(this.getDocument(Jsoup.connect(line).get()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			stream.close();
		}
		// TODO COMPRIMIDO ZIP

		/*
		 * 3) Instanciar el IndexWriter para poder escribir los Documents en el
		 * Index
		 */
		Path path = Paths.get(indexPath);
		Directory indexDir = FSDirectory.open(path);

		this.idxwriter = new IndexWriter(indexDir, new IndexWriterConfig());

		/* 4) Escribir los elementos en el indice */
		for (Document d : documents) {
			this.indexDocument(d);
		}

		this.idxwriter.close();
	}

	private Document getDocument(org.jsoup.nodes.Document d) {

		// TODO check null

		/* Creamos los campos del documento */
		Field contentField = new TextField("content", d.body().text(), Store.YES);
		Field filePathField = new StringField("filepath", d.baseUri(), Store.YES);

		/* Creamos el documento */
		Document document = new Document();

		document.add(contentField);
		document.add(filePathField);

		return document;
	}

	/*
	 * private Document getDocument(String content, String filePath, String
	 * fileName) throws IOException {
	 * 
	 * 
	 * 
	 * Field contentField = new TextField("content", content, Store.YES); Field
	 * filePathField = new StringField("filepath", filePath, Store.YES); Field
	 * fileNameField = new StringField("filename", fileName, Store.YES);
	 * 
	 * Document document = new Document();
	 * 
	 * document.add(contentField); document.add(filePathField);
	 * document.add(fileNameField);
	 * 
	 * return document; }
	 */
	private void indexDocument(Document d) throws IOException {
		System.out.println("Indexing " + d.getField("filename").toString());
		this.idxwriter.addDocument(d);
	}
}
