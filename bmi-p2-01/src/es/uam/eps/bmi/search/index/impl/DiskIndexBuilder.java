package es.uam.eps.bmi.search.index.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map.Entry;

import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.PostingsList;

public class DiskIndexBuilder extends BaseIndexBuilder {

	private PrintStream psDicc;
	private PrintStream psPosting;

	@Override
	protected void serializeIndex(String indexPath) throws IOException {

		// abrimos los escribidores en archivo
		FileOutputStream os = new FileOutputStream(indexPath + Config.dictionaryFileName, false);
		psDicc = new PrintStream(os, true, "UTF-8");

		FileOutputStream os2 = new FileOutputStream(indexPath + Config.postingsFileName, false);
		psPosting = new PrintStream(os2, true, "UTF-8");

		// imprimimos los contenidos generados en el dictionary
		int offset = 0;
		for (Entry<String, PostingsList> t : this.dictionary.entrySet()) {
			psDicc.println(t.getKey() + " " + offset); // termino y su offset
			offset += 1 + t.getValue().toString().getBytes().length;
			psPosting.println(t.getValue());
		}

		psDicc.close();
		psPosting.close();
	}

	@Override
	protected Index getCoreIndex() throws IOException {
		return new DiskIndex(indexFolder);
	}

}
