package es.uam.eps.bmi.sna.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

public class GradesTest {
	
	public static void main (String a[]) throws IOException {
		
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
		
	}

}
