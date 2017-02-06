package es.uam.eps.bmi.search.ui;

import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author pablo
 */
public class TextResultsRenderer extends ResultsRenderer {
    
    public TextResultsRenderer (SearchRanking results) {
        super(results);
    }
    
    public String toString() {
        StringBuilder str = new StringBuilder();
        try {
            if (ranking.size() == 0)
                str.append("No results found.");
            else for (SearchRankingDoc result : ranking) {
                String uri = result.getPath();
                if (new File(uri).exists()) uri = new File(uri).toURI().toString();
                str.append(result.getScore() + "\t" + uri);
            }
        } catch ( IOException ex) {
            ex.printStackTrace();
        }
        return str.toString();
    }
}
