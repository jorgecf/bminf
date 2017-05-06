package es.uam.eps.bmi.sna.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;

public class GradesTest {
	
	public static void main (String a[]) throws IOException {
		
		FileWriter archivo1 = new FileWriter("graph/twitter_grades.txt");
		PrintWriter pw1 = new PrintWriter(archivo1);
		
		int i = 0;
		
		BufferedReader br1 = new BufferedReader(new FileReader("graph/twitter.csv"));
		
		HashMap<String, Integer> h = new HashMap<String, Integer>();
		HashMap<String, Integer> hS = new HashMap<String, Integer>();
		
		String line = br1.readLine();
		while (line != null) {
	        String[] str = line.split(",");
	        String i1 = str[0];
	        String i2 = str[1];
	        if (hS.containsKey(i1)) {
	        	hS.put(i1, (hS.get(i1))+1);
	        } else {
	        	hS.put(i1, 1);
	        	h.put(i1, i);
	        	i++;
	        }
	        if (hS.containsKey(i2)) {
	        	hS.put(i2, (hS.get(i2))+1);	        	
	        } else {
	        	hS.put(i2, 1);
	        	h.put(i2, i);
	        	i++;
	        }
	        line = br1.readLine();
	    }
		
		Set<String> e = hS.keySet();
		for (String elem : e) {
			pw1.println(h.get(elem)+ " " + hS.get(elem));
		}
		
		archivo1.close();
		br1.close();
		
		// Todas menos Twitter
		/*
		 FileWriter archivo1 = new FileWriter("graph/small1_grades.txt");
		PrintWriter pw1 = new PrintWriter(archivo1);
		
		BufferedReader br1 = new BufferedReader(new FileReader("graph/small1.csv"));
		
		HashMap<Integer, Integer> h = new HashMap<Integer, Integer>();
		
		String line = br1.readLine();
		while (line != null) {
	        String[] str = line.split(",");
	        int i1 = Integer.parseInt(str[0]);
	        int i2 = Integer.parseInt(str[1]);
	        if (h.containsKey(i1)) {
	        	h.put(i1, (h.get(i1))+1);
	        } else {
	        	h.put(i1, 1);
	        }
	        if (h.containsKey(i2)) {
	        	h.put(i2, (h.get(i2))+1);
	        } else {
	        	h.put(i2, 1);
	        }
	        line = br1.readLine();
	    }
		
		Set<Integer> e = h.keySet();
		for (Integer elem : e) {
			pw1.println(elem + " " + h.get(elem));
		}
		
		archivo1.close();
		 */
		
	}

}
