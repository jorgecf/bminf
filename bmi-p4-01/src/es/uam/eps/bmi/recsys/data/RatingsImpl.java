package es.uam.eps.bmi.recsys.data;

import java.util.Set;

public class RatingsImpl implements Ratings {

	public RatingsImpl(String ratingsFile, String separator) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void rate(int user, int item, Double rating) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Double getRating(int user, int item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Integer> getUsers(int item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Integer> getItems(int user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Integer> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Integer> getItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int nRatings() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Ratings[] randomSplit(double ratio) {
		// TODO Auto-generated method stub
		return null;
	}

}
