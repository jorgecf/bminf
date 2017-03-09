package es.uam.eps.bmi.search.index.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map.Entry;

import es.uam.eps.bmi.search.index.Config;
import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.index.structure.PostingsList;

public class EfficientIndexBuilder extends BaseIndexBuilder {

	private double startMem;
	private double usedMem;
	// int mb = 1024 * 1024;
	Runtime instance;

	public EfficientIndexBuilder() {
		super();
		instance = Runtime.getRuntime();
		int max = 1024;
		startMem = instance.freeMemory()/1E+9;
		usedMem = 0;

	}

	@Override
	protected void serializeIndex(String indexPath) throws IOException {
		// super.serializeIndex(indexPath);

		Runtime instance = Runtime.getRuntime();
		int mb = 1024 * 1024;
		int max = 1024;

		System.out.println(
				"EfficientIndexBuilder.serializeIndex()usada:" + (instance.totalMemory() - instance.freeMemory()) / mb);

	}

	@Override
	public void putDictionary(String term, int docID, int freq) {

		System.out.println(term+":");
		System.out.println("Ram usada PRE insercion: " + this.usedMem+" GB");
		super.putDictionary(term, docID, freq);

		this.usedMem += this.startMem - (this.instance.freeMemory()/1E+9);
		System.out.println("Ram usada POS insercion: " + this.usedMem+" GB");
		
		if(this.usedMem>=1){
			System.exit(0);
		} // serializar, fusionar

	}

	@Override
	protected Index getCoreIndex() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
