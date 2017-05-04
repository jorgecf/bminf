package es.uam.eps.bmi.sna.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
	
	public static void main (String a[]) throws IOException {
		
		Factory<UndirectedGraph<Integer,Integer>> graphFactory1 = new UndirectedGraphFactory();
		Factory<Integer> vertexFactory1 = new VertexFactory();
		Factory<Integer> edgeFactory1 = new EdgeFactory();
		
		ErdosRenyiGenerator<Integer, Integer> er = new ErdosRenyiGenerator<Integer, Integer>(graphFactory1, vertexFactory1, edgeFactory1, 4000, 0.5);
		Graph<Integer, Integer> gEr = er.create();
		
		FileWriter archivo1 = new FileWriter("graph/erdos.csv");
		PrintWriter pw1 = new PrintWriter(archivo1);
		
		// Escribir en el fichero erdos.csv los pares
		for (Integer vertex : gEr.getVertices()) {
			for (Integer neigh : gEr.getNeighbors(vertex))
				pw1.println(vertex + "," + neigh);
		}
		
		archivo1.close();
		
		Factory<Graph<Integer,Integer>> graphFactory2 = new GraphFactory();
		Factory<Integer> vertexFactory2 = new VertexFactory();
		Factory<Integer> edgeFactory2 = new EdgeFactory();
		Set<Integer> s = new TreeSet<Integer>();
		
		BarabasiAlbertGenerator<Integer, Integer> ba = new BarabasiAlbertGenerator<Integer, Integer>(graphFactory2, vertexFactory2, edgeFactory2, 2000, 2000, s);
		ba.evolveGraph(10);
		Graph<Integer, Integer> gBa = ba.create();
		
		FileWriter archivo2 = new FileWriter("graph/barabasi.csv");
		PrintWriter pw2 = new PrintWriter(archivo2);
		
		// Escribir en el fichero barabasi.csv los pares
		for (Integer vertex : gBa.getVertices()) {
			for (Integer neigh : gBa.getNeighbors(vertex))
				pw2.println(vertex + "," + neigh);
		}
		
		archivo2.close();
		
	}
	
}
