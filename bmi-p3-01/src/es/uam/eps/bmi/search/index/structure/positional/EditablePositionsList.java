package es.uam.eps.bmi.search.index.structure.positional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pablo
 */
public class EditablePositionsList implements PositionsList {
    List<Long> positions;
    
    EditablePositionsList() {
        positions = new ArrayList<Long>();
    }
    
    public void add(long position) {
        positions.add(position);
    }
}
