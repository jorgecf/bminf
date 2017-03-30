package es.uam.eps.bmi.search.index.structure.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.positional.PositionalPostingImpl;
import es.uam.eps.bmi.search.index.structure.positional.lucene.LucenePositionalPostingsList;

/**
 * Lista de Postings Posicionales con iterador.
 * 
 * @author Alejandro Martin
 * @author Jorge Cifuentes
 *
 */
public class PositionalPostingsList extends LucenePositionalPostingsList {

	List<PositionalPostingImpl> postings;

	public PositionalPostingsList() {
		super(null, null, 0);
		postings = new ArrayList<PositionalPostingImpl>();
	}

	public PositionalPostingsList(int docID, int position) {
		super(null, null, 0);

		if (this.postings == null)
			this.postings = new ArrayList<PositionalPostingImpl>();

		this.postings.add(new PositionalPostingImpl(docID, 1, position));
	}

	@Override
	public Iterator<Posting> iterator() {
		List<Posting> lp = new ArrayList<>();

		for (PositionalPostingImpl ppi : this.postings) {
			lp.add(ppi);
		}

		return lp.iterator();
	}

	@Override
	public int size() {
		return postings.size();
	}

	/**
	 * Agrega un nuevo posting. Supuesto: adicion por docID's ordenados y
	 * crecientes.
	 * 
	 * @param docID
	 *            docId.
	 * @param position
	 *            Posicion de la aparicion enesima.
	 */
	public void add(int docID, int position) {

		// ya existe el posting de este termino para ese docID, actualizar
		// frecuencia y posiciones
		if (!postings.isEmpty() && docID == postings.get(postings.size() - 1).getDocID()) {
			postings.get(postings.size() - 1).addPosition(position);
		}
		// aun no existe, crearlo
		else {
			this.postings.add(new PositionalPostingImpl(docID, 1, position));
		}

	}

	/**
	 * Agrega un nuevo postings con sus posiciones.
	 * 
	 * @param doc
	 *            codID.
	 * @param freq
	 *            Frecuencia en documento.
	 * @param l
	 *            Posiciones (lista de longitud freq).
	 */
	public void add(int doc, long freq, List<Integer> l) {
		if (this.postings == null)
			this.postings = new ArrayList<PositionalPostingImpl>();

		this.postings.add(new PositionalPostingImpl(doc, freq, l));
	}

}
