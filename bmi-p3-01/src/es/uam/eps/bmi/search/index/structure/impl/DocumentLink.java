package es.uam.eps.bmi.search.index.structure.impl;

/**
 * Representa un link (enlace inicio y enlace final). Los documentos
 * se representan como docIDs.
 * 
 * @author Alejandro Martin
 * @author Jorge Cifuentes
 *
 */
public class DocumentLink {

	private int from;
	private int to;

	public DocumentLink(int from, int to) {
		this.from = from;
		this.to = to;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	@Override
	public String toString() {
		return "[" + this.from + " -> " + this.to + "]";
	}
}