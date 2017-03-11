package es.uam.eps.bmi.search.index.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.impl.RAMPostingsList;

/**
 * Indice que guarda los terminos y listas de postings en disco, despues de
 * crearlos enteramente en ram.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class SerializedRAMIndexBuilder extends BaseIndexBuilder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	protected void serializeIndex(String indexPath) throws IOException {

		// serializar los terminos del diccionario
		FileOutputStream fileOut = new FileOutputStream(indexPath + Config.dictionaryFileName);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);

		// cast de Object[] a String[], para serializar los terminos
		String[] temrSer = this.dictionary.keySet().toArray(new String[this.dictionary.keySet().size()]);
		out.writeObject(temrSer);
		out.close();
		fileOut.close();

		// serializar las postingslist del diccionario
		FileOutputStream fileOut2 = new FileOutputStream(indexPath + Config.postingsFileName);
		ObjectOutputStream out2 = new ObjectOutputStream(fileOut2);

		// cast de Object[] a RAMPostingsList[], para serializar las
		// postingslist del diccionario
		RAMPostingsList[] plsSer = this.dictionary.values()
				.toArray(new RAMPostingsList[this.dictionary.values().size()]);

		out2.writeObject(plsSer);
		out2.close();
		fileOut2.close();
	}

	@SuppressWarnings("unused")
	private void writeIndex(String indexPath) {

		// creamos la carpeta del indice
		File indexDir = new File(indexPath);
		indexDir.mkdir();

		// escribimos los terminos y los postings
		FileWriter file = null;
		FileWriter file2 = null;

		try {
			file = new FileWriter(indexPath + Config.dictionaryFileName);
			file2 = new FileWriter(indexPath + Config.postingsFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(file);
		PrintWriter pw2 = new PrintWriter(file2);

		for (String term : this.dictionary.keySet()) {
			pw.println(term);
			pw2.println(this.dictionary.get(term));
		}

		pw.close();
		pw2.close();
	}

	@Override
	protected Index getCoreIndex() throws IOException {
		return new SerializedRAMIndex(indexFolder);
	}

}
