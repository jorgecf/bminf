package es.uam.eps.bmi.search.index.impl;

import java.io.IOException;

public class PositionalIndexBuilderImpl extends BaseIndexBuilder {

	@Override
	public void save(String indexPath) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void indexText(String text, String path) throws IOException {
		// super.indexText(text, path);

		String[] terms = text.toLowerCase().split("\\P{Alpha}+");
		for (String term : terms)
			dictionary.add(term, nDocs);
		docPaths.add(path);
		nDocs++;
		
		
		
	}

}