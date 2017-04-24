package es.uam.eps.bmi.sna.generator;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.graph.UndirectedGraph;

public class ErdosRenyi<V, E> extends ErdosRenyiGenerator<V, E> {

	public ErdosRenyi(Factory<UndirectedGraph<V,E>> graphFactory, Factory<V> vertexFactory, Factory<E> edgeFactory, int numVertices, double p) {
		super(graphFactory, vertexFactory, edgeFactory, numVertices, p);
	}
	
}
