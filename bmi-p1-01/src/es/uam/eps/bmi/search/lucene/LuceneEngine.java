package es.uam.eps.bmi.search.lucene;

import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.index.NoIndexException;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.lucene.LuceneRanking;

public class LuceneEngine extends AbstractEngine {

	private LuceneIndex index;
	private IndexSearcher idxSearcher;

	public LuceneEngine(String path) throws IOException, NoIndexException {
		super(path);
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {

		if (this.idxSearcher == null)
			throw new NoIndexException(this.indexFolder);

		// creamos un Query Booleana donde cada palabra de la query es a su vez
		// un TermQuery, para buscar (preferentemente) documentos donde
		// aparezcan todas las palabras
		BooleanQuery.Builder builder = new BooleanQuery.Builder();

		String[] words = query.split(" ");

		for (String s : words) {
			TermQuery tq = new TermQuery(new Term("content", s));
			builder.add(tq, BooleanClause.Occur.SHOULD);
		}

		BooleanQuery pQuery = builder.build();

		// realizamos la busqueda
		TopDocs top = this.idxSearcher.search(pQuery, cutoff);

		return new LuceneRanking(index, top.scoreDocs);
	}

	@Override
	public void loadIndex(String path) throws IOException {

		this.index = new LuceneIndex(path);

		if (this.index != null && this.index.getIndexReader() != null)
			this.idxSearcher = new IndexSearcher(this.index.getIndexReader());
	}

}
