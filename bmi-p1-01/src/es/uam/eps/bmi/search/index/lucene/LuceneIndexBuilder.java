package es.uam.eps.bmi.search.index.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
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
	 * Crea un indice a partir de unos archivos HTML dados.
	 * 
	 * @param collectionPath
	 *            ruta de los archivos a indexar, puede ser una lista con una
	 *            web por linea que se descargara, o un zip/carpeta con archivos
	 *            html ya descargados
	 * @param indexPath
	 *            ruta del indice a crear
	 * 
	 */
	@Override
	public void build(String collectionPath, String indexPath) throws IOException {

		File collectionFile = new File(collectionPath);
		if (collectionFile.exists() == false) {
			throw new IOException();
		}

		/*
		 * instanciamos el IndexWriter para poder escribir los Documents en el
		 * Index
		 */
		Path path = Paths.get(indexPath);
		Directory indexDir = FSDirectory.open(path);

		this.idxwriter = new IndexWriter(indexDir, new IndexWriterConfig());

		/*
		 * leemos los archivos de disco y cargamos sus rutas, despues creamos un
		 * objeto Document con sus Fields a partir de cada ruta almacenada y lo
		 * indexamos
		 */

		/*
		 * Si es un zip, lo descomprimimos y leemos de manera normal los
		 * archivos html que obtengamos
		 */
		if (collectionPath.endsWith(".zip")) {

			ZipFile zipFile = new ZipFile(collectionPath);

			this.unzipFile(collectionPath, "tmp/");
			zipFile.close();

			File zipCollectionFile = new File("tmp/");
			for (File f : zipCollectionFile.listFiles()) {
				this.indexDocument((this.getDocument(Jsoup.parse(f, "UTF-8", f.getName()))));
			}

		}
		/* Si es un directorio de html's cargamos cada archivo */
		else if (collectionFile.isDirectory() == true) {

			for (File f : collectionFile.listFiles()) {
				if (f.isDirectory() == false) { // no entramos en directorios
					this.indexDocument((this.getDocument(Jsoup.parse(f, "UTF-8", f.getName()))));
				}
			}
		}
		/* Si es un txt con webs, descargamos cada web */
		else if (collectionFile.isFile() == true) {

			Stream<String> stream = Files.lines(Paths.get(collectionPath));

			stream.forEach(line -> {
				try {
					this.indexDocument((this.getDocument(Jsoup.connect(line).get())));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			stream.close();
		}

		this.idxwriter.close();
	}

	/**
	 * Obtiene un Document de Apache Lucene a partir de uno de jsoup
	 * 
	 * @param d
	 *            documento jsoup valido
	 * 
	 * @return documento lucene con dos fields, la ruta en disco y el contenido
	 */
	private Document getDocument(org.jsoup.nodes.Document d) {

		if (d == null)
			return null;

		/* Creamos los campos del documento */
		Field contentField = new TextField("content", d.body().text(), Store.YES);
		Field filePathField = new StringField("filepath", d.baseUri(), Store.YES);

		/* Creamos el documento */
		Document document = new Document();

		document.add(contentField);
		document.add(filePathField);

		return document;
	}

	/**
	 * Indexa (escribe en disco) un documento usando el indexwriter propio del
	 * LuceneIndexBuilder
	 * 
	 * @param d
	 *            documento lucene a indexar
	 * 
	 * @throws IOException
	 *             si falla al añadir un documento al indice
	 */
	private void indexDocument(Document d) throws IOException {
		System.out.println("Indexing (" + this.idxwriter.numDocs() + "): " + d.getField("filepath").stringValue());
		this.idxwriter.addDocument(d);
	}

	/**
	 * Descomprime un archivo zip (solo los archivos al primer nivel)
	 * 
	 * @param zipPath
	 *            ruta del archivo zip
	 * @param destPath
	 *            ruta donde colocar los archivos descomprimidos
	 */
	private void unzipFile(String zipPath, String destPath) {

		// TODO comprobar parametros

		File dir = new File(destPath);
		if (!dir.exists()) // creacion de directorio destino
			dir.mkdirs();

		FileInputStream fis;
		int BUFFER = 2048;
		byte[] buffer = new byte[BUFFER];

		try {

			/* leemos el zip origen y extraemos la primera entrada */
			fis = new FileInputStream(zipPath);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry zEntry = zis.getNextEntry();

			/* iteramos sobre las entradas del archivo zip */
			while (zEntry != null) {

				String fileName = zEntry.getName();
				File destFile = new File(destPath + File.separator + fileName);

				FileOutputStream fos = new FileOutputStream(destFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				/* liberamos recursos */
				fos.close();
				zis.closeEntry();

				zEntry = zis.getNextEntry();
			}

			/* liberamos recursos finales */
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
