package mos.init;

public class UiConfiguration extends Configuration {

	public UiConfiguration(String source, String info, String mosaiquesource,
			int tilesize, long waittime, String destination, int numThreads) {
		super();
		check(source, info, mosaiquesource, tilesize, waittime, destination, numThreads);
	}
}