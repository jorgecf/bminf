package es.uam.eps.bmi.recsys.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FeaturesImpl<F> implements Features<F> {

	private Map<Integer, Map<F, Double>> data; // id - feature/count

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

	private void parseInput(String r, String separator) { // mover a otra clase?
		// TODO

		try {
			BufferedReader br = new BufferedReader(new FileReader(r));

			String line = "";
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] d = line.split(separator);
				try {
					this.setFeature(Integer.valueOf(d[0]), this.parser.parse(d[1]), Double.valueOf(d[2]));
					// this.nRatings++;
				} catch (NumberFormatException n) {
				}
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Iterator<F> iterator() {
		Set<F> ret = new HashSet<>();
		this.data.values().forEach(m -> ret.addAll(m.keySet()));

		return ret.iterator();
	}

	@Override
	public Set<F> getFeatures(int id) {
		return this.data.get(id).keySet();
	}

	@Override
	public Double getFeature(int id, F feature) {
		return this.data.get(id).get(feature);
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
