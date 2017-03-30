package es.uam.eps.bmi.search.test;

import java.io.IOException;

import es.uam.eps.bmi.search.index.WebCrawler;
import es.uam.eps.bmi.search.index.impl.PositionalIndexBuilderImpl;

/**
 * Tester de el Crawler.
 * 
 * @author Alejandro Martin
 * @author Jorge Cifuentes
 *
 */
public class TestCrawler {
	public static void main(String a[]) throws IOException {
		WebCrawler wc = new WebCrawler(new PositionalIndexBuilderImpl(), 10, "collections/crawl.txt");
		wc.crawl();
	}
}