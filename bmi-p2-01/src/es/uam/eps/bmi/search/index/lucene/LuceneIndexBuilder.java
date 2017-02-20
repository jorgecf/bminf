package es.uam.eps.bmi.search.index.lucene;

import es.uam.eps.bmi.search.index.AbstractIndexBuilder;
import es.uam.eps.bmi.search.index.Index;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author pablo
 */
public class LuceneIndexBuilder extends AbstractIndexBuilder {
    IndexWriter builder;
    protected static FieldType type;

    public LuceneIndexBuilder() {
        type = new FieldType();
        type.setIndexOptions (IndexOptions.DOCS_AND_FREQS);
    }
    
    public void build (String collectionPath, String indexPath) throws IOException {
    }
    
    public void indexText(String text, String path) throws IOException {
    }

    protected Index openIndex(String path) throws IOException {
        return new LuceneIndex(path);
    }
}
