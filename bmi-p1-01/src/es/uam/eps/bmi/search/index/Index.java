package es.uam.eps.bmi.search.index;

import java.io.IOException;
import java.util.List;

import es.uam.eps.bmi.search.index.freq.FreqVector;

/**
 * Abstraccion de un indice.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 */
public interface Index {

	/**
	 * Carga un indice desde disco.
	 * 
	 * @param s
	 *            ruta en disco del indice
	 * @throws IOException
	 * @throws NoIndexException
	 */
	public abstract void load(String s) throws IOException, NoIndexException;

	/**
	 * Obtiene todos los terminos unicos del indice.
	 * 
	 * @return lista de string
	 */
	public abstract List<String> getAllTerms();

	/**
	 * Devuelve el numero de veces que aparece "word" en todos los documentos.
	 * 
	 * @param word
	 *            palabra a buscar
	 * @return numero de apariciones
	 * @throws IOException
	 */
	public abstract int getTermTotalFreq(String word) throws IOException;

	/**
	 * Devuelve el vector de terminos-frecuencia.
	 * 
	 * @param docID
	 *            documento en el que buscar
	 * @return vector de frecuencias
	 * @throws IOException
	 */
	public abstract FreqVector getDocVector(int docID) throws IOException;

	/**
	 * Devuelve la ruta de un documento.
	 * 
	 * @param docID
	 *            documento que buscar
	 * @return string ruta
	 * @throws IOException
	 */
	public abstract String getDocPath(int docID) throws IOException;

	/**
	 * Devuelve el numero de veces que aparece "word" en un documento
	 * especifico.
	 * 
	 * @param word
	 *            palabra a buscar
	 * @param docID
	 *            documento en el que buscar
	 * @return numero de apariciones
	 * @throws IOException
	 */
	public abstract long getTermFreq(String word, int docID) throws IOException;

	/**
	 * Devuelve el numero de documentos donde aparece "word".
	 * 
	 * @param word
	 *            plaabra a buscar
	 * @return numero de apariciones
	 * @throws IOException
	 */
	public abstract long getTermDocFreq(String word) throws IOException;
}
