package es.uam.eps.bmi.recsys.recommender.similarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import es.uam.eps.bmi.recsys.data.Ratings;

/**
 * Calcula la similitud entre usuarios por el metodo del coeficiente de
 * correlacion de Pearson.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class PearsonSimilarity extends CosineUserSimilarity {

	Map<Integer, Double> userProm;

	public PearsonSimilarity(Ratings ratings) {
		super(ratings);
	}

	@Override
	protected double simAux(int x, int y) {

		if (this.userProm == null) {
			this.userProm = new HashMap<>();
		}

		Double acc = 0.0;
		Double acc2u = 0.0;
		Double acc2v = 0.0;

		Set<Integer> x1 = this.ratings.getItems(x);
		Set<Integer> y1 = this.ratings.getItems(y);

		// Items valorados por ambos usuarios.
		HashSet<Integer> xy = new HashSet<>(x1);
		xy.retainAll(y1); // interseccion

		// Si no tienen elementos en comun, su similitud sera
		// 0 / algo ---> 0. Lo mismo pasa si algun user no
		// ha valorado nada ( algo / algo * 0 ) ---> 0.
		if (xy.size() == 0 || x1.size() == 0 || y1.size() == 0) {
			return 0.0;
		}

		// Calculamos las puntuaciones promedios de ambos usuarios.
		Double xPromedio;
		if (this.userProm.containsKey(x) == false) {
			xPromedio = 0.0;
			for (Integer item : this.ratings.getItems(x)) {
				xPromedio += this.ratings.getRating(x, item);
			}
			xPromedio = (double) xPromedio / this.ratings.getItems(x).size();
			this.userProm.put(x, xPromedio);
		} else {
			xPromedio = this.userProm.get(x);
		}

		Double yPromedio;
		if (this.userProm.containsKey(y) == false) {
			yPromedio = 0.0;
			for (Integer item : this.ratings.getItems(y)) {
				yPromedio += this.ratings.getRating(y, item);
			}
			yPromedio = (double) yPromedio / this.ratings.getItems(y).size();
			this.userProm.put(y, yPromedio);
		} else {
			yPromedio = this.userProm.get(y);
		}

		// Items que ambos users han valorado.
		for (Integer item : xy) {

			Double rx = this.ratings.getRating(x, item);
			Double ry = this.ratings.getRating(y, item);

			acc += (rx - xPromedio) * (ry - yPromedio);
			acc2u += (rx - xPromedio) * (rx - xPromedio);
			acc2v += (ry - yPromedio) * (ry - yPromedio);
		}

		// Resultado final.
		Double ret = (double) acc / Math.sqrt(acc2u * acc2v);
		if (ret.isNaN())
			return 0.0;
		else
			return ret;
	}

	@Override
	public String toString() {
		return "Pearson correlation";
	}

}