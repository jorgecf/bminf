package es.uam.eps.bmi.utils;

import java.io.IOException;

import es.uam.eps.bmi.search.index.lucene.LuceneIndex;

/**
 * Calcula el coseno de similitud usando el metodo tf-idf.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 */
public class CosineSimilarity {

	/**
	 * Devuelve la frecuencia de un termino en un documento.
	 * 
	 * @param index
	 *            indice sobre el que operar
	 * @param term
	 *            termino que buscar
	 * @param docID
	 *            documento que buscar
	 * @return frecuencia
	 * @throws IOException
	 */
	public static double termFrequency(LuceneIndex index, String term, int docID) throws IOException {

		long frec = index.getTermFreq(term, docID);
		if (frec <= 0)
			return 0;

		return 1 + (Math.log(frec) / Math.log(2));
	}

	/**
	 * Devuelve la frecuencia en coleccion inversa.
	 * 
	 * @param index
	 *            indice sobre el que operar
	 * @param term
	 *            termino que buscar
	 * @return frecuencia
	 * @throws IOException
	 */
	public static double inverseDocumentFrequency(LuceneIndex index, String term) throws IOException {

		double idf = (double) (Math.log((double) index.getIndexReader().numDocs() / (1 + index.getTermDocFreq(term)))
				/ Math.log(2));

		return 1 + idf;
	}
}
