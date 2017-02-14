package es.uam.eps.bmi.search.vsm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.impl.ImplRankedDoc;
import es.uam.eps.bmi.search.ranking.impl.ImplRanking;
import es.uam.eps.bmi.utils.CosineSimilarity;

public class VSMEngine extends AbstractEngine {

	private LuceneIndex index;

	public VSMEngine(String path) throws IOException {
		super(path);
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {

		ArrayList<ImplRankedDoc> matches = new ArrayList<>();
		String[] components = query.split(" ");

		// abrimos el archivo con los modulos almacenados
		FileReader r = new FileReader("index/modulos.txt");
		BufferedReader modReader = new BufferedReader(r);

		// recorremos cada documento del indice
		for (int i = 0; i < this.index.getIndexReader().numDocs(); i++) {

			// recorremos cada palabra de la query
			double sum = 0;
			for (int j = 0; j < components.length; j++) {

				double tf = CosineSimilarity.termFrequency(this.index, components[j], i);
				double idf = CosineSimilarity.inverseDocumentFrequency(this.index, components[j]);

				sum += (tf * idf);
			}

			String line = modReader.readLine();
			String[] mod = line.split("\t");

			// Aplicamos la definicion de similitud coseno por tf-idf,
			// incluyendo la longitud (modulo) del vector query
			sum = (double) sum / (Double.valueOf(mod[1]) * (Math.sqrt(components.length)));

			if (sum > 0) { // sumatorio de arriba del coseno de similitud
				matches.add(new ImplRankedDoc(i, sum));
			}
		}

		// devolvemos los resultados ordenados
		Collections.sort(matches);
		
		modReader.close();

		if (matches.size() >= cutoff)
			return new ImplRanking(index, matches.subList(0, cutoff));
		else
			return new ImplRanking(index, matches);

	}

	@Override
	public void loadIndex(String path) throws IOException {
		this.index = new LuceneIndex(path);
	}

}
