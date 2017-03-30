package es.uam.eps.bmi.search.index.structure.impl;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.structure.PostingsList;

/**
 * Diccionario de hash en disco con posiciones de temrino en documento.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class PositionalDiskHashDictionary extends DiskHashDictionary {

	private static final long serialVersionUID = 1L;

	public PositionalDiskHashDictionary(String path) {
		super(path);
	}

	@Override
	public PostingsList getPostings(String term) throws IOException {
		PositionalPostingsList postings = new PositionalPostingsList();

		if (!termPostings.containsKey(term))
			return postings;

		RandomAccessFile postingsFile = new RandomAccessFile(indexFolder + Config.postingsFileName, "r");
		postingsFile.seek(termPostings.get(term));

		int length = postingsFile.readInt();
		while (length-- > 0) {

			// leemos el docID
			int doc = postingsFile.readInt();

			// leemos la frecuencia
			long freq = postingsFile.readLong();
			List<Integer> l = new ArrayList<>();

			// leemos las posiciones
			for (int i = 0; i < freq; i++) {
				l.add(postingsFile.readInt());
			}

			postings.add(doc, freq, l);
		}

		postingsFile.close();
		return postings;
	}
}