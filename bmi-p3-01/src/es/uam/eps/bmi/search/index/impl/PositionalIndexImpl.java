package es.uam.eps.bmi.search.index.impl;

import java.io.IOException;

import es.uam.eps.bmi.search.index.structure.impl.PositionalDiskHashDictionary;

/**
 * Indice posicional (con lista de posiciones en las postings list).
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class PositionalIndexImpl extends BaseIndex {

	public PositionalIndexImpl(String indexFolder) throws IOException {
		super(indexFolder);

		this.dictionary = new PositionalDiskHashDictionary(indexFolder);
		((PositionalDiskHashDictionary) dictionary).load();
	}
}