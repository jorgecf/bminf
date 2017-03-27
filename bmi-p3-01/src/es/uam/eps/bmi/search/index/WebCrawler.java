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
	private Set<String> alreadyCrawled;
	private int indexedDocs;

	public WebCrawler(BaseIndexBuilder indexBuilder, int maxDocs, String seedFile) {
		this.indexBuilder = indexBuilder;
		this.maxDocs = maxDocs;
		this.seedFile = seedFile;

		this.indexedDocs = 0;

		this.disallowed = new HashSet<>();
		this.crawlingList = new PriorityQueue<>();
		this.alreadyCrawled = new HashSet<>();
	}

	public void crawl() throws IOException {

		List<String> seedUrls = Files.readAllLines(Paths.get(this.seedFile));

		crawlingList.addAll(seedUrls);

		while (this.indexedDocs < this.maxDocs) {
			this.crawlingList.addAll(this.expandCrawl(this.crawlingList));
			System.out.println("CRAWLINGLIST: " + crawlingList.size());
		}

		this.indexBuilder.save("index/crawled");
	}

	private Collection<String> expandCrawl(Collection<String> urls) throws IOException {

		Collection<String> ret = new PriorityQueue<>();

		for (String url : urls) {

			System.out.println("\t\t\t NUM=" + indexedDocs);

			if (this.indexedDocs == this.maxDocs) {
				System.out.println("[info] index lleno, parando");
				break;
			}

			System.out.println("url " + url);

			// URL base
			URL baseUrl = new URL(url);
			String robotsUrl = baseUrl.getProtocol() + "://" + baseUrl.getHost() + "/robots.txt";

			// Leemos el archivo de robots.txt
			// TODO user-agent
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
				System.out.println("[error] con " + robotsUrl);
			}

			// Obtenemos el contenido a indexar
			// TODO clase config
			if (this.alreadyCrawled.contains(url) == false) {
				try {
					Document d = Jsoup.connect(url).get();

					// Indexamos el contenido
					String text = d.body().text();
					this.indexBuilder.indexText(text, url);
					this.indexedDocs++;

					// Obtenemos los links contenidos en cada web
					for (Element e : d.select("a[href]")) {
						String next = e.attr("abs:href");
						if (disallowed.contains(next) == false) {
							// crawlingList.add(next);
							ret.add(next);
						} else {
							System.out.println("[error] no se puede agregar " + next);
						}
					}
				} catch (Exception e) {
					System.out.println("[error] connectandose a " + url);
				}

				this.alreadyCrawled.add(url);
			} else {
				System.out.println("[info] " + url + " already crawled");
			}

		}

		return ret;
	}

}