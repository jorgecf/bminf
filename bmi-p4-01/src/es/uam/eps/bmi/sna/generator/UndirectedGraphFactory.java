package es.uam.eps.bmi.sna.generator;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class UndirectedGraphFactory implements Factory<UndirectedGraph<Integer,Integer>> {
    public UndirectedGraph<Integer,Integer> create() {
        return new UndirectedSparseGraph<Integer,Integer>();
    }
}
