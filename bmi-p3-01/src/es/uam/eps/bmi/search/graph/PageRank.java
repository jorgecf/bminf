package es.uam.eps.bmi.search.graph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.index.DocumentMap;
import es.uam.eps.bmi.search.index.structure.impl.DocumentLink;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.impl.RankingImpl;

public class PageRank extends AbstractEngine implements DocumentMap {

	private double r;
	private int convergenceCondition;
	private String linkFile;

	private String docPaths[];

	public PageRank(String linkFile, double r, int convergenceCondition) {
		super(null);

		this.linkFile = linkFile;
		this.r = r;
		this.convergenceCondition = convergenceCondition;
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {

		// Ignoraremos la query

		// Cargamos los links (from -> to) desde el archivo de links
		List<DocumentLink> links = new ArrayList<>();
		Set<String> uniqueDocs = new HashSet<>();

		Scanner sc = new Scanner(new File(this.linkFile));
		while (sc.hasNextLine()) {
			String[] link = sc.nextLine().split("\\s+");
			// TODO if no hay 2 ---> excepcion

			links.add(new DocumentLink(link[0], link[1]));
			uniqueDocs.add(link[0]);
			uniqueDocs.add(link[1]);
		}
		sc.close();

		// IDs de documentos
		this.docPaths = new String[uniqueDocs.size()];
		int d = 0;
		for (String s : uniqueDocs) {
			this.docPaths[d] = s;
			d++;
		}

		// Calculamos los outlinks
		Map<String, Integer> outlinks = new HashMap<>();

		for (int k = 0; k < links.size(); k++) {

			String nextFrom = links.get(k).getFrom();

			if (outlinks.containsKey(nextFrom) == false) {
				outlinks.put(nextFrom, 1);
			} else {
				outlinks.put(nextFrom, 1 + outlinks.get(nextFrom));
			}
		}

		// PageRank score inicial
		Map<String, Double> pageRank = new HashMap<>();
		uniqueDocs.forEach(doc -> pageRank.put(doc, (double) 1 / uniqueDocs.size()));

		// Calculo iterativo
		int w = 0;
		while (w < this.convergenceCondition) {

			Map<String, Double> pageRank2 = new HashMap<>();
			uniqueDocs.forEach(doc -> pageRank2.put(doc, (double) this.r / uniqueDocs.size()));

			for (int k = 0; k < links.size(); k++) {

				String i = links.get(k).getFrom(); //TODO cambiar a docid cuando funcione 100%
				String j = links.get(k).getTo();

				pageRank2.put(j, pageRank2.get(j) + (double) ((1 - this.r) * pageRank.get(i) / outlinks.get(i)));
			}

			uniqueDocs.forEach(doc -> pageRank.put(doc, pageRank2.get(doc)));

			// TODO sumideros

			w++;
		}

		RankingImpl ranking = new RankingImpl(this, cutoff);
		
		for(int l=0;l<this.docPaths.length;l++){
			ranking.add(l, pageRank.get(this.getDocPath(l))); //TODO cambiar nombre por doc
		}
																			

		return ranking;

	}

	@Override
	public String getDocPath(int docID) throws IOException {
		return this.docPaths[docID];
	}

	@Override
	public double getDocNorm(int docID) throws IOException {
		// no hacer
		return 1;
	}

	@Override
	public DocumentMap getDocMap() {
		return this;
	}

}
