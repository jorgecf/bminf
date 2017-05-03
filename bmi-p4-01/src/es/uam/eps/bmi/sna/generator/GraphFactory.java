package es.uam.eps.bmi.sna.generator;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class GraphFactory implements Factory<Graph<Integer,Integer>> {
    public Graph<Integer,Integer> create() {
        return new UndirectedSparseGraph<Integer,Integer>();
    }
}