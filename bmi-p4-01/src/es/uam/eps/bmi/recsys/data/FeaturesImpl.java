package es.uam.eps.bmi.recsys.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Caracteristicas de los items. Tambie parsea el input en csv.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class FeaturesImpl<F> implements Features<F> {

	/*
	 * Mapa de item id -> mapa de caracteristica, numero de apariciones en item
	 */
	private Map<Integer, Map<F, Double>> data;

	private String featuresFile;
	private String separator;
	private Parser<F> parser;

	public FeaturesImpl(String featuresFile, String separator, Parser<F> parser) {
		this.data = new HashMap<>();

		this.featuresFile = featuresFile;
		this.separator = separator;
		this.parser = parser;

		this.parseInput(this.featuresFile, this.separator);
	}

	private void parseInput(String r, String separator) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(r));

			String line = "";
			while ((line = br.readLine()) != null) {

				String[] d = line.split(separator);
				try {
					this.setFeature(Integer.valueOf(d[0]), this.parser.parse(d[1]), Double.valueOf(d[2]));
				} catch (NumberFormatException n) {
				}
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Set<F> getFeatures(int id) {
		Map<F, Double> ret = this.data.get(id);

		if (ret != null)
			return ret.keySet();
		else
			return null;
	}

	@Override
	public Double getFeature(int id, F feature) {
		
		if (this.data.containsKey(id) && this.data.get(id).containsKey(feature))
			return this.data.get(id).get(feature);

		return 0.0;
	}

	@Override
	public void setFeature(int id, F feature, double value) {

		if (this.data.containsKey(id) == false) {
			Map<F, Double> n = new HashMap<>();
			n.put(feature, value);

			this.data.put(id, n);
		} else {
			this.data.get(id).put(feature, value);
		}

	}

	@Override
	public Set<Integer> getIDs() {
		return this.data.keySet();
	}

}