package es.uam.eps.bmi.search.index;

import java.io.IOException;

/**
 *
 * @author pablo
 */
public abstract class AbstractIndex implements Index {
	protected Index index;
	protected String indexFolder;

	public AbstractIndex(String path) throws IOException {
		indexFolder = path;
		try {
			load();
		} catch (NoIndexException ex) {
			System.out.println("Warning: created index pointing to " + indexFolder + ", no index files there.");
			for (StackTraceElement trace : ex.getStackTrace())
				System.out.println("\tat: " + trace);
			System.out.println("This is just an informative warning and requires no immediate action ;-)");
		}
		System.out.println("Index loaded successfully");
	}

	public void load() throws IOException {
		load(indexFolder);
	}

	public String getFolder() {
		return indexFolder;
	}
}
