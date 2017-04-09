package es.uam.eps.bmi.recsys.recommender.similarity;

public class IdPair {

	private Integer p1;
	private Integer p2;

	IdPair(Integer p1, Integer p2) {
		this.p1 = Math.min(p1, p2); // p1, p2 igual a p2, p1
		this.p2 = Math.max(p1, p2);
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof IdPair) {

			final IdPair p = (IdPair) obj;
			if ((this.p1 == p.p1 && this.p2 == p.p2) || (this.p2 == p.p1 && this.p1 == p.p2)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		return p1 * 31 + p2;
	}

	@Override
	public String toString() {
		return "[" + this.p1 + " " + this.p2 + "]";
	}

}
