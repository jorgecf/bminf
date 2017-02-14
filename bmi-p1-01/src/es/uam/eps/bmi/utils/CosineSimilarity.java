package es.uam.eps.bmi.utils;

import java.io.IOException;

import es.uam.eps.bmi.search.index.lucene.LuceneIndex;

public class CosineSimilarity {

	public static double termFrequency(LuceneIndex index, String term, int docID) throws IOException {
		long frec = index.getTermFreq(term, docID);
		if (frec <= 0)
			return 0;

		double tf = 1 + (Math.log(frec) / Math.log(2));

		return tf;
	}

	public static double inverseDocumentFrequency(LuceneIndex index, String term) throws IOException {

		double idf = (double) (Math.log((double) index.getIndexReader().numDocs() / (1 + index.getTermDocFreq(term)))
				/ Math.log(2));

		return 1 + idf;
	}
}
