package es.uam.eps.bmi.search.index.structure.impl;

public class DocumentLink {
	private int from;
	private int to;

	public DocumentLink(int from, int to) {
		super();
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
		return "[" + this.from + " -> "+this.to+"]";
	}
}