package es.uam.eps.bmi.search.index.structure.impl;

public class DocumentLink {
	private String from;
	private String to;

	public DocumentLink(String from, String to) {
		super();
		this.from = from;
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	@Override
	public String toString() {
		return "[" + this.from + " -> "+this.to+"]";
	}
}