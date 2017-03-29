package es.uam.eps.bmi.search.graph;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.uam.eps.bmi.search.AbstractEngine;
import es.uam.eps.bmi.search.index.DocumentMap;
import es.uam.eps.bmi.search.index.structure.impl.DocumentLink;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.impl.RankingImpl;

/**
 * Clase que calcula el PageRank de una lista de docs a partir de sus enlaces.
 * Realiza un numero pasado por argumentos de iteraciones, que detiene si antes
 * se converge con un umbral definido en todos los docs.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
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
		Set<Integer> fromSet = new HashSet<>();
		Set<Integer> sinksSet = new HashSet<>();

		BufferedReader sc = new BufferedReader(new InputStreamReader(new FileInputStream(this.linkFile), "UTF-8"));

		String red;
		while ((red = sc.readLine()) != null) {
			String[] link = red.split("\\s+"); // todos los caracteres de espacio

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

			fromSet.add(this.inverseDocPaths.get(link[0]));
			sinksSet.add(this.inverseDocPaths.get(link[1]));
		}
		sc.close();

		// Obtenemos los nodos sumidero
		sinksSet.removeAll(fromSet);

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
		
		double initialScore = (double) 1 / this.docPaths.size();
		for (Integer docID : this.docPaths.keySet()) {
			pageRank.put(docID, initialScore);
		}

		// Calculo iterativo hasta que converja (numero de iteraciones o
		// treshold)
		int w = 0;
		while (w < this.convergenceCondition) {

			// Siguiente PageRank
			Map<Integer, Double> pageRank2 = new HashMap<>();

			// P'(i) = r/N
			double rn = (double) this.r / this.docPaths.size();
			for (int id = 1; id <= this.docPaths.size(); id++) {
				pageRank2.put(id, rn);
			}

			// P'(j) = P'(j) + (1-r) * P(i) / out(i)
			double rInv = (1 - this.r);
			for (int k = 0; k < links.size(); k++) {

				int i = links.get(k).getFrom();
				int j = links.get(k).getTo();

				pageRank2.put(j, pageRank2.get(j) + (double) (rInv * pageRank.get(i) / outlinks.get(i)));
			}

			// Valor de ajuste: si hay sumideros se ajustan todos los pageranks2
			double adjust = 0;
			if (sinksSet.size() > 0) {
				adjust = (double) ((1 - pageRank2.values().stream().mapToDouble(d -> d.doubleValue()).sum())
						/ this.docPaths.size());
				
				final double adj = adjust;
				pageRank2.replaceAll((k, v) -> v + adj);
			}			

			// Comprobamos si hay convergencia
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
				System.out.println("[info] PageRank ha convergido en la " + w + " iteracion");
				break;
			}

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
		// No hacer
		return 1;
	}

	@Override
	public DocumentMap getDocMap() {
		return this;
	}

}