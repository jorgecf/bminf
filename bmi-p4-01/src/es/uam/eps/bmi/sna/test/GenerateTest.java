package es.uam.eps.bmi.sna.test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import es.uam.eps.bmi.sna.generator.EdgeFactory;
import es.uam.eps.bmi.sna.generator.GraphFactory;
import es.uam.eps.bmi.sna.generator.UndirectedGraphFactory;
import es.uam.eps.bmi.sna.generator.VertexFactory;

public class GenerateTest {
	
	public static void main (String a[]) {
		
		Factory<UndirectedGraph<Integer,Integer>> graphFactory1 = new UndirectedGraphFactory();
		Factory<Integer> vertexFactory1 = new VertexFactory();
		Factory<Integer> edgeFactory1 = new EdgeFactory();
		
		ErdosRenyiGenerator<Integer, Integer> er = new ErdosRenyiGenerator<Integer, Integer>(graphFactory1, vertexFactory1, edgeFactory1, 100, 0.5);
		Graph<Integer, Integer> gEr = er.create();
		
		File archivo1 = new File("graph/erdos.csv");
		
		Factory<Graph<Integer,Integer>> graphFactory2 = new GraphFactory();
		Factory<Integer> vertexFactory2 = new VertexFactory();
		Factory<Integer> edgeFactory2 = new EdgeFactory();
		Set<Integer> s = new HashSet<Integer>();
		
		BarabasiAlbertGenerator<Integer, Integer> ba = new BarabasiAlbertGenerator<Integer, Integer>(graphFactory2, vertexFactory2, edgeFactory2, 10, 100, s);
		Graph<Integer, Integer> gBa = ba.create();
		
		File archivo2 = new File("graph/barabasi.csv");
		
	}
	
}
