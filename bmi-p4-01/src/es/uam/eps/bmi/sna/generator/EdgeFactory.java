package es.uam.eps.bmi.sna.generator;

import org.apache.commons.collections15.Factory;

public class EdgeFactory implements Factory<Integer> {
    private Integer nEdges = 0;
    public Integer create() {
        return nEdges++;
    }
}
