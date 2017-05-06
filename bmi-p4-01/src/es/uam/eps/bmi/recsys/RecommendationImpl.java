package es.uam.eps.bmi.recsys;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElement;

/**
 * Recomendacion, asigna a cada user un ranking de elementos recomendados.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 *
 */
public class RecommendationImpl implements Recommendation {

	/* Mapa de userID - Ranking asignado */
	private Map<Integer, Ranking> data;

	public RecommendationImpl() {
		this.data = new HashMap<>();
	}

	@Override
	public Set<Integer> getUsers() {
		return this.data.keySet();
	}

	@Override
	public Ranking getRecommendation(int user) {
		return this.data.get(user);
	}

	@Override
	public void add(int user, Ranking ranking) {
		this.data.put(user, ranking);
	}

	@Override
	public void print(PrintStream out) {
	}

	@Override
	public void print(PrintStream out, int userCutoff, int itemCutoff) {

		Iterator<Entry<Integer, Ranking>> e = this.data.entrySet().iterator();

		// Iteramos sobre cada usuario ( e.next() ).
		int stopUser = 0;
		while (e.hasNext()) {

			Entry<Integer, Ranking> nxt = e.next(); // siguiente user

			// Ranking de este user,
			Iterator<RankingElement> rit = nxt.getValue().iterator();

			int stopItem = 0;

			// Iteramos sobre cada elemento del ranking del usuario actual.
			while (rit.hasNext()) {
				RankingElement rn = rit.next();
				out.println(nxt.getKey() + "\t" + rn.getID() + "\t" + rn.getScore());

				if (++stopItem == itemCutoff)
					break;
			}

			if (++stopUser == userCutoff)
				break;
		}

	}

}