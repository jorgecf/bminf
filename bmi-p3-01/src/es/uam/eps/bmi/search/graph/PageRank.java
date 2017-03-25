package es.uam.eps.bmi.search.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.index.DocumentMap;
import es.uam.eps.bmi.search.index.structure.impl.DocumentLink;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.impl.RankingImpl;
import es.uam.eps.bmi.search.util.Timer;
import javafx.util.Pair;

public class PageRank extends AbstractEngine implements DocumentMap {

	private double r;
	private int convergenceCondition;
	private String linkFile;

	private static final Double CONVERGENCE_TRESHOLD = 0.00001;

	private Map<Integer, String> docPaths;
	private Map<String, Integer> inverseDocPaths;

	public PageRank(String linkFile, double r, int convergenceCondition) {
		super(null);

		this.linkFile = linkFile;
		this.r = r;
		this.convergenceCondition = convergenceCondition;
	}

	@Override
	public SearchRanking search(String query, int cutoff) throws IOException {

		// Ignoraremos la query

		this.docPaths = new HashMap<>();
		this.inverseDocPaths = new HashMap<>();

		// Cargamos los links (from -> to) desde el archivo de links
		List<DocumentLink> links = new ArrayList<>();

		BufferedReader sc = new BufferedReader(new InputStreamReader(new FileInputStream(this.linkFile), "UTF-8"));

		String red;
		while ((red = sc.readLine()) != null) {
			String[] link = red.split("\\s+");

			// TODO if no hay 2 ---> excepcion

			if (this.inverseDocPaths.get(link[0]) == null) {
				int newID = this.docPaths.size() + 1;
				this.docPaths.put(newID, link[0]);
				this.inverseDocPaths.put(link[0], newID);
			}

			if (this.inverseDocPaths.get(link[1]) == null) {
				int newID = this.docPaths.size() + 1;
				this.docPaths.put(newID, link[1]);
				this.inverseDocPaths.put(link[1], newID);
			}

			links.add(new DocumentLink(this.inverseDocPaths.get(link[0]), this.inverseDocPaths.get(link[1])));
		}
		sc.close();

		// Calculamos los outlinks
		Map<Integer, Integer> outlinks = new HashMap<>();

		for (int k = 0; k < links.size(); k++) {

			int nextFrom = links.get(k).getFrom();

			if (outlinks.containsKey(nextFrom) == false) {
				outlinks.put(nextFrom, 1);
			} else {
				outlinks.put(nextFrom, 1 + outlinks.get(nextFrom));
			}
		}

		// PageRank score inicial
		Map<Integer, Double> pageRank = new HashMap<>();
		for (Integer docID : this.docPaths.keySet()) {
			pageRank.put(docID, (double) 1 / this.docPaths.size());
		}

		// Calculo iterativo
		int w = 0;
		while (w < this.convergenceCondition) {

			// Siguiente PageRank
			Map<Integer, Double> pageRank2 = new HashMap<>();

			for (int id = 1; id <= this.docPaths.size(); id++) {
				pageRank2.put(id, (double) this.r / this.docPaths.size()); // r/N
			}

			for (int k = 0; k < links.size(); k++) {

				int i = links.get(k).getFrom();
				int j = links.get(k).getTo();

				pageRank2.put(j, pageRank2.get(j) + (double) ((1 - this.r) * pageRank.get(i) / outlinks.get(i)));
			}

			// comprobamos si tenenmos el nivel de afinamiento necesario
			boolean converged = true;
			Iterator<Double> bi = pageRank.values().iterator();
			Iterator<Double> ai = pageRank2.values().iterator();

			while (ai.hasNext() && bi.hasNext()) {
				if (Math.abs(ai.next() - bi.next()) > CONVERGENCE_TRESHOLD) {
					converged = false;
					break;
				}
			}

			// actualizamos P(i) = P'(i)
			pageRank = pageRank2;

			if (converged) {
				System.out.println("PageRank ha CONVERGIDO en la " + w + " iteracion!!!");
				break;
			}

			// !!! TODO sumideros TODO !!!

			w++;

		}

		RankingImpl ranking = new RankingImpl(this, cutoff);
		for (Integer docID : this.docPaths.keySet()) {
			ranking.add(docID, pageRank.get(docID));
		}

		return ranking;

	}

	@Override
	public String getDocPath(int docID) throws IOException {
		return this.docPaths.get(docID);
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