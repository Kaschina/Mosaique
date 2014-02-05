package mos.init;

public class UiConfiguration extends Configuration {

	public UiConfiguration(String source, String destination, String mosaiquesource, String info,
			int tilesize, int waittime,  int numThreads) {
		super();
		check(source, info, mosaiquesource, tilesize, waittime, destination, numThreads);
	}
	
}