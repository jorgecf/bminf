package es.uam.eps.bmi.search.index;

public class WebCrawler {

	private IndexBuilder indexBuilder;
	private int maxDocs;
	private String seedFile;

	public WebCrawler(IndexBuilder indexBuilder, int maxDocs, String seedFile) {
		this.indexBuilder = indexBuilder;
		this.maxDocs = maxDocs;
		this.seedFile = seedFile;
	}

}
