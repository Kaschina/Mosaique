package mos.init;

public class UiConfiguration extends Configuration {

	public UiConfiguration(String source, String destination, String mosaiquesource, String info,
			int tilesize, int waittime,  int numThreads, boolean random) {
		super();
		check(source, info, mosaiquesource, tilesize, waittime, destination, numThreads, random);
	}
	
}