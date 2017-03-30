package es.uam.eps.bmi.search.index.structure.positional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.uam.eps.bmi.search.index.structure.positional.PositionalPosting;
import es.uam.eps.bmi.search.index.structure.positional.PositionsIterator;

/**
 * Positional posting extendido.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class PositionalPostingImpl extends PositionalPosting {

	private static final long serialVersionUID = 1L;

	public PositionalPostingImpl(int id, long f, int pos) {
		super(id, f, new ArrayList<>());
		positions.add(pos);
	}

	public PositionalPostingImpl(int doc, long freq, List<Integer> l) {
		super(doc, freq, l);

	}

	public void addPosition(int pos) {
		this.add1(); // frequency
		this.positions.add(pos);
	}

	public List<Integer> getPositions() {
		return positions;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new PositionsIterator(positions);
	}
}