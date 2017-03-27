package es.uam.eps.bmi.search.index.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.structure.Posting;
import es.uam.eps.bmi.search.index.structure.impl.PositionalDictionary;
import es.uam.eps.bmi.search.index.structure.positional.PositionalPostingImpl;
import es.uam.eps.bmi.search.index.structure.impl.PositionalPostingsList;

public class PositionalIndexBuilderImpl extends BaseIndexBuilder {

	
	public PositionalIndexBuilderImpl() {
		super();
		
		nDocs = 0;
		dictionary = new PositionalDictionary();
		docPaths = new ArrayList<String>();
	}

	@Override
	public void build(String collectionPath, String indexPath) throws IOException {
		clear(indexPath);
		/*nDocs = 0;
		dictionary = new PositionalDictionary();
		docPaths = new ArrayList<String>();*/

		File f = new File(collectionPath);
		if (f.isDirectory())
			indexFolder(f); // A directory containing text files.
		else if (f.getName().endsWith(".zip"))
			indexZip(f); // A zip file containing compressed text files.
		else
			indexURLs(f); // A file containing a list of URLs.
		save(indexPath);
		saveDocPaths(indexPath);
		saveDocNorms(indexPath);
	}

	@Override
	public void save(String indexPath) throws IOException {

		// writers en archivo
		FileOutputStream os = new FileOutputStream(indexPath + Config.dictionaryFileName, false);
		PrintStream psDicc = new PrintStream(os, true, "UTF-8");
		
		RandomAccessFile postingsFile = new RandomAccessFile(indexPath + Config.postingsFileName, "rw");

		long address = 0;
		for (String term : dictionary.getAllTerms()) {
			PositionalPostingsList postings = (PositionalPostingsList) dictionary.getPostings(term);

			postingsFile.writeInt(postings.size());

			for (Posting p : postings) {
				postingsFile.writeInt(p.getDocID());
				postingsFile.writeLong(p.getFreq());

				List<Integer> pl = ((PositionalPostingImpl) p).getPositions();
				for (int i = 0; i < p.getFreq(); i++) {
					postingsFile.writeInt(pl.get(i));
				}
			}
			
			psDicc.println(term + "\t" + address); // termino y su offset
			address = postingsFile.getFilePointer();
		}

		postingsFile.close();
		psDicc.close();

	}

	@Override
	public void indexText(String text, String path) throws IOException {
		// super.indexText(text, path);

		String[] terms = text.toLowerCase().split("\\P{Alpha}+");

		int i = 0; // para calculo de posiciones
		for (String term : terms) {
			((PositionalDictionary) dictionary).add(term, nDocs, i);
			i++;
		}
		docPaths.add(path);
		nDocs++;
	}

}