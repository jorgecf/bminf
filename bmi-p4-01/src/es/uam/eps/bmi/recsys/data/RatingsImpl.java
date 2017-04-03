package es.uam.eps.bmi.recsys.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class RatingsImpl implements Ratings {

	private String ratingsFile;
	private String separator;

	private Map<Integer, Map<Integer, Double>> data; // user - item/rating
	private Map<Integer, Map<Integer, Double>> dataInverse; // item -
															// user/rating

	public RatingsImpl(String ratingsFile, String separator) {
		this.ratingsFile = ratingsFile;
		this.separator = separator;

		this.data = new HashMap<Integer, Map<Integer, Double>>();
		this.dataInverse = new HashMap<Integer, Map<Integer, Double>>();
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

	}

	@Override
	public Double getRating(int user, int item) {
		return this.data.get(user).get(item);
	}

	@Override
	public Set<Integer> getUsers(int item) {
		return this.dataInverse.get(item).keySet();
	}

	@Override
	public Set<Integer> getItems(int user) {
		return this.data.get(user).keySet();
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
		return this.data.size();
	}

	@Override
	public Ratings[] randomSplit(double ratio) {

		Ratings[] r = new Ratings[2];

		// Creamos los nuevos ratings.
		Ratings r1 = new RatingsImpl(this.ratingsFile, this.separator);
		Ratings r2 = new RatingsImpl(this.ratingsFile, this.separator);

		Iterator<Integer> it = this.data.keySet().iterator();
		while (it.hasNext()) {

			int next = it.next();

			Iterator<Integer> it2 = this.data.get(next).keySet().iterator();
		
			while (it2.hasNext()) {
				int next2 = it2.next();
				Double rat2 = this.data.get(next).get(next2);
				r1.rate(next, next2, rat2); // TODO random
			}

		}

		// Guardamos los nuevos mapas.
		r[0] = r1;
		r[1] = r2;

		return r;
	}

	public void setData(Map<Integer, Map<Integer, Double>> data) {
		this.data = data;
	}

	public void setDataInverse(Map<Integer, Map<Integer, Double>> dataInverse) {
		this.dataInverse = dataInverse;
	}

}
