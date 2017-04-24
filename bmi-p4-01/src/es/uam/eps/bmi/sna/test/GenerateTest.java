package es.uam.eps.bmi.sna.test;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.event.GraphEvent.Edge;
import edu.uci.ics.jung.graph.event.GraphEvent.Vertex;
import es.uam.eps.bmi.sna.generator.ErdosRenyi;

public class GenerateTest {
	
	Factory<UndirectedGraph<Vertex,Edge>> graphFactory;
	Factory<Vertex> vertexFactory;
	Factory<Edge> edgeFactory;
	
	ErdosRenyi<Vertex, Edge> er = new ErdosRenyi<Vertex, Edge>(graphFactory, vertexFactory, edgeFactory, 4000, 0.5);

}
