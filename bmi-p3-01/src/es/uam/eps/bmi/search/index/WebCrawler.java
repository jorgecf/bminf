package es.uam.eps.bmi.search.index;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import es.uam.eps.bmi.search.index.impl.BaseIndexBuilder;

/**
 * Crawler basico que crawlea.
 * 
 * @author Jorge Cifuentes
 * @author Alejandro Martin
 */
public class WebCrawler {

	private BaseIndexBuilder indexBuilder;
	private int maxDocs;
	private String seedFile;
	private Set<String> disallowed;
	private PriorityQueue<String> crawlingList;
	private Set<String> alreadyCrawled;
	private int indexedDocs;

	private File webgraph;
	private PrintWriter fw;

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

		this.webgraph = new File("graph/", Config.graphFileName);
		webgraph.getParentFile().mkdirs();
		webgraph.createNewFile();
		this.fw = new PrintWriter(this.webgraph);

		crawlingList.addAll(seedUrls);

		while (this.indexedDocs < this.maxDocs) {
			this.crawlingList.addAll(this.expandCrawl(this.crawlingList));
			System.out.println(" [fin iteracion] CrawlingList (frontera): " + crawlingList.size());
		}

		// Salvamos todo
		this.indexBuilder.save("index/crawled");
		this.fw.close();
	}

	private Collection<String> expandCrawl(Collection<String> urls) throws IOException {

		Collection<String> ret = new PriorityQueue<>();

		for (String url : urls) {

			if (this.indexedDocs == this.maxDocs) {
				System.out.println("[info] index lleno, parando");
				break;
			}

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
				System.out.println("[error] con " + robotsUrl);
			}

			// Obtenemos el contenido a indexar
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

							// archivo de links para su uso por PageRank
							fw.write(url + "\t" + next + "\n");

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