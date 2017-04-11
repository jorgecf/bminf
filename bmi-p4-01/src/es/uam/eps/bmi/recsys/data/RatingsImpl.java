package es.uam.eps.bmi.recsys.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Ratings. (Usuario -> item -> score). Tambien se encarga de parsear la entrada
 * en csv.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class RatingsImpl implements Ratings {

	private String ratingsFile;
	private String separator;

	/* Mapa de usuario -> mapa de item, rating de usuario */
	private Map<Integer, Map<Integer, Double>> data;

	/* Mapa de item -> mapa de usuario, rating de usuario */
	private Map<Integer, Map<Integer, Double>> dataInverse;

	private int nRatings;

	public RatingsImpl() {
		this.data = new HashMap<Integer, Map<Integer, Double>>();
		this.dataInverse = new HashMap<Integer, Map<Integer, Double>>();
		this.nRatings = 0;
	}

	public RatingsImpl(String ratingsFile, String separator) {
		this.ratingsFile = ratingsFile;
		this.separator = separator;

		this.data = new HashMap<Integer, Map<Integer, Double>>();
		this.dataInverse = new HashMap<Integer, Map<Integer, Double>>();
		this.nRatings = 0;

		this.parseInput(this.ratingsFile, this.separator);
	}

	private void parseInput(String r, String separator) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(r));

			String line = "";
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] d = line.split(separator);
				try {
					this.rate(Integer.valueOf(d[0]), Integer.valueOf(d[1]), Double.valueOf(d[2]));
				} catch (NumberFormatException n) {
				}
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void rate(int user, int item, Double rating) {

		// Mapa normal.
		if (this.data.containsKey(user) == false) {
			Map<Integer, Double> n = new HashMap<Integer, Double>();
			n.put(item, rating);
			this.data.put(user, n);
		} else {
			this.data.get(user).put(item, rating);
		}

		// Mapa inverso.
		if (this.dataInverse.containsKey(item) == false) {
			Map<Integer, Double> n = new HashMap<Integer, Double>();
			n.put(user, rating);
			this.dataInverse.put(item, n);
		} else {
			this.dataInverse.get(item).put(user, rating);
		}

		this.nRatings++;
	}

	@Override
	public Double getRating(int user, int item) {

		if (this.data.containsKey(user) && this.data.get(user).containsKey(item))
			return this.data.get(user).get(item);

		return null;
	}

	@Override
	public Set<Integer> getUsers(int item) {
		Map<Integer, Double> ret = this.dataInverse.get(item);

		if (ret != null)
			return ret.keySet();
		else
			return null;
	}

	@Override
	public Set<Integer> getItems(int user) {
		if (this.data.containsKey(user))
			return this.data.get(user).keySet();
		else
			return null;
	}

	@Override
	public Set<Integer> getUsers() {
		return this.data.keySet();
	}

	@Override
	public Set<Integer> getItems() {
		return this.dataInverse.keySet();
	}

	@Override
	public int nRatings() {
		return this.nRatings;
	}

	@Override
	public Ratings[] randomSplit(double ratio) {

		Ratings[] r = new Ratings[2];
		Random rnd = new Random();

		// Creamos los nuevos ratings.
		Ratings r1 = new RatingsImpl();
		Ratings r2 = new RatingsImpl();

		Iterator<Integer> it = this.data.keySet().iterator();
		while (it.hasNext()) {

			int next = it.next();

			Iterator<Integer> it2 = this.data.get(next).keySet().iterator();

			while (it2.hasNext()) {
				int next2 = it2.next();
				Double rat2 = this.data.get(next).get(next2);

				// Dividiamos de acuerdo al ratio en tanto por ciento.
				int nx = rnd.nextInt(100);

				if (nx <= 100 * ratio) {
					r1.rate(next, next2, rat2);
				} else {
					r2.rate(next, next2, rat2);
				}
			}
		}

		// Guardamos los nuevos mapas.
		r[0] = r1;
		r[1] = r2;

		return r;
	}

}
