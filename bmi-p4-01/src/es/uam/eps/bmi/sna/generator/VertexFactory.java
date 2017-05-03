package es.uam.eps.bmi.sna.generator;

import org.apache.commons.collections15.Factory;

public class VertexFactory implements Factory<Integer> {
    private Integer nVertices = 0;
    public Integer create() {
        return nVertices++;
    }
}
