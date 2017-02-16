package es.uam.eps.bmi.search.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.IndexBuilder;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.index.lucene.LuceneIndexBuilder;

/**
 * Tester que crea dos archivos de frecuencias.
 * 
 * @author Alejandro Martin
 * @author Jorge Cifuentes
 *
 */
public class TermStats {

	public static void main(String a[]) throws IOException {

		File txtDir = new File("txt/");
		txtDir.mkdir();

		// las frecuencias totales en la colección de los terminos, ordenadas de
		// mayor a menor
		FileWriter txtTermFreq = new FileWriter("txt/termfreq.txt");
		PrintWriter pw1 = new PrintWriter(txtTermFreq);

		IndexBuilder builder = new LuceneIndexBuilder();

		// builder.build("collections/urls.txt", "index");
		builder.build("src/es/uam/eps/bmi/search/ranking", "index");

		Index index = new LuceneIndex("index");
		List<String> terms1 = index.getAllTerms();

		Comparator<String> termComparator = new Comparator<String>() {
			public int compare(String t1, String t2) {
				try {
					return (int) Math.signum(index.getTermTotalFreq(t2) - index.getTermTotalFreq(t1));
				} catch (IOException ex) {
					ex.printStackTrace();
					return 0;
				}
			}
		};

		Collections.sort(terms1, termComparator);

		for (String term : terms1) {
			pw1.println(term + "\t" + index.getTermTotalFreq(term));
		}

		txtTermFreq.close();

		// el numero de documentos que contiene cada termino, igualmente de
		// mayor a menor
		FileWriter txtTermDocFreq = new FileWriter("txt/termdocfreq.txt");
		PrintWriter pw2 = new PrintWriter(txtTermDocFreq);

		List<String> terms2 = index.getAllTerms();

		Collections.sort(terms2, termComparator);

		for (String term : terms1) {
			pw2.println(term + "\t" + index.getTermDocFreq(term));
		}

		txtTermDocFreq.close();
	}
}
