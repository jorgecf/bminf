package es.uam.eps.bmi.search.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.index.NoIndexException;
import es.uam.eps.bmi.search.index.lucene.LuceneIndex;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import es.uam.eps.bmi.search.ranking.lucene.LuceneRanking;

public class LuceneEngine extends AbstractEngine {

	private LuceneIndex index;
	private IndexSearcher idxSearcher;

	public LuceneEngine(String path) throws IOException, NoIndexException {
		super(path);
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {
	
		LuceneRanking ret = null;

		/*
		
		 PhraseQuery.Builder builder = new PhraseQuery.Builder();
		 
		 String[] words = query.split(" ");
		 
		 for (String s : words) {
		 builder.add(new Term("content", s));
		 }
		 
		 PhraseQuery pQuery = builder.build();
		 
		*/
		
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		
		String[] words = query.split(" ");
		 
		 for (String s : words) {
			 TermQuery tq = new TermQuery(new Term("content", s));
			 builder.add(tq, BooleanClause.Occur.SHOULD);
		 }
		
		BooleanQuery pQuery = builder.build();
		

		if (this.idxSearcher == null)
			throw new NoIndexException(this.indexFolder);

		TopDocs top = this.idxSearcher.search(pQuery, cutoff);

			
			ArrayList<SearchRankingDoc> srDocs = new ArrayList<>();
			for (ScoreDoc sd : top.scoreDocs) {
				SearchRankingDoc doc = new SearchRankingDoc(sd, this.index.getDocPath(sd.doc));
				srDocs.add(doc);
			}
			
			ret = new LuceneRanking(srDocs);
		return ret;
	}

	@Override
	public void loadIndex(String path) throws IOException {
		this.index = new LuceneIndex(path);

		if (this.index != null && this.index.getIndexReader() != null)
			this.idxSearcher = new IndexSearcher(this.index.getIndexReader());
	}

}
