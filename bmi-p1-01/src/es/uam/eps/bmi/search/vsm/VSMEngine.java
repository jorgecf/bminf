package es.uam.eps.bmi.search.vsm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.lucene.index.MultiFields;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.util.Bits;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.ranking.impl.ImplRankedDoc;
import es.uam.eps.bmi.search.ranking.impl.ImplRanking;

public class VSMEngine extends AbstractEngine {

	private LuceneIndex index;
	private IndexSearcher idxSearcher;

	public VSMEngine(String path) throws IOException {
		super(path);
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {
		// TODO Auto-generated method stub

		ImplRankedDoc[] scoreDocs = new ImplRankedDoc[this.index.getIndexReader().numDocs()];

		String[] components = query.split(" ");

		// recorrer cada documento del index
		// int numberOfIndexed = 0;
		for (int i = 0; i < this.index.getIndexReader().numDocs(); i++) {

			// comprobar si isDeleted

			// recorrer cada termino_i de la query
			double sum = 0;
			for (int j = 0; j < components.length; j++) {

				System.out.println(
						"doc: " + i + ", term_i: " + components[j] + "  " + this.index.getTermFreq(components[j], i));

				double tf = termFrequency(components[j], i);
				double idf = inverseDocumentFrequency(components[j]);

				sum += (tf * idf);
				System.out.println("\tTFIDF DE " + components[j] + ", docid: " + i + " es: " + (tf * idf));
			}

			// sumatorio de arriba del coseno de similitud
			System.out.println("sum tfidf's de doc " + i + ", y query: " + query + " es :" + sum);

			scoreDocs[i] = new ImplRankedDoc(i, sum);

		}

		// TODO SOLO MOSTRAR CUTOFF RESULTADOS

		// Collections.sort(scoreDocs);
		Arrays.sort(scoreDocs);

		// ranking
		ImplRanking rank = new ImplRanking(index, Arrays.copyOfRange(scoreDocs, 0, cutoff));

		return rank;

	}

	private double termFrequency(String term, int docID) throws IOException {

		long frec = this.index.getTermFreq(term, docID);
		if (frec <= 0)
			return 0;

		double tf = 1 + (Math.log(frec) / Math.log(2));
		// System.out.println("VSMEngine.termFrequency() - tf " + tf);

		return tf;
	}

	private double inverseDocumentFrequency(String term) throws IOException {
		double idf = (double) this.index.getIndexReader().numDocs() / this.index.getTermDocFreq(term);

		// System.out.println("VSMEngine.inverseDocumentFrequency() - idf " +
		// idf);

		return idf;
	}

	@Override
	public void loadIndex(String path) throws IOException {

		this.index = new LuceneIndex(path);

		if (this.index != null && this.index.getIndexReader() != null)
			this.idxSearcher = new IndexSearcher(this.index.getIndexReader());
	}

}
