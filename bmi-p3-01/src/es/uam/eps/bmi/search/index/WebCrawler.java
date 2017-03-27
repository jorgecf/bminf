package es.uam.eps.bmi.search.index;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import es.uam.eps.bmi.search.index.impl.BaseIndexBuilder;

public class WebCrawler {

	private BaseIndexBuilder indexBuilder;
	private int maxDocs;
	private String seedFile;
	private Set<String> disallowed;
	private PriorityQueue<String> crawlingList;

	public WebCrawler(BaseIndexBuilder indexBuilder, int maxDocs, String seedFile) {
		this.indexBuilder = indexBuilder;
		this.maxDocs = maxDocs;
		this.seedFile = seedFile;

		this.disallowed = new HashSet<>();
		this.crawlingList = new PriorityQueue<>();
	}

	public void crawl() throws IOException {

		List<String> seedUrls = Files.readAllLines(Paths.get(this.seedFile));

		crawlingList.addAll(seedUrls);

		while (this.crawlingList.size() < this.maxDocs) {
			this.crawlingList.addAll(this.expandCrawl(this.crawlingList));
			System.out.println("CRAWLINGLIST: " + crawlingList.size());
		}
	}

	private Collection<String> expandCrawl(Collection<String> urls) throws IOException {

		Collection<String> ret = new PriorityQueue<>();

		for (String url : urls) {

			System.out.println("url " + url);

			// URL base
			URL baseUrl = new URL(url);
			String robotsUrl = baseUrl.getProtocol() + "://" + baseUrl.getHost() + "/robots.txt";

			// Leemos el archivo de robots.txt
			try {
				Scanner sc = new Scanner(new URL(robotsUrl).openStream(), "UTF-8");
				String out = sc.useDelimiter("\\A").next();

				for (String line : out.split(System.lineSeparator())) {

					// mensajes disallow
					if (line.toLowerCase().startsWith("disallow")) {
						String[] dont = line.split(" ");
						if (dont.length > 1)
							disallowed.add(url + dont[1]);
					}
				}

				sc.close();
			} catch (Exception e) {
				System.out.println("[error] con esta url...");
			}
			// Obtenemos el contenido a indexar
			// TODO clase config
			Document d = Jsoup.connect(url).get();

			// Indexamos el contenido
			String text = d.body().text();
			this.indexBuilder.indexText(text, url);

			// Obtenemos los links contenidos en cada web
			for (Element e : d.select("a[href]")) {
				String next = e.attr("abs:href");
				if (disallowed.contains(next) == false) {
					// crawlingList.add(next);
					ret.add(next);
				} else {
					System.out.println("no se puede agregar este: " + next);
				}
			}

		}

		return ret;
	}

}