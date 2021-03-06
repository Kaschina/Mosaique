package mos.init;

import java.io.File;

abstract class Configuration {

	private boolean errorFree;
	private File source;
	private File info;
	private File mosaiquesource;
	private Integer tilesize;
	private int waittime;
	private File dest;
	private int numThreads;
	private boolean random;

	public Configuration() {
	}

	public void check(File file, File file2, File file3,
			int tilesize, int waittime, File file4, int numThreads, boolean random) {

		this.source = file;
		this.info = file2;
		this.mosaiquesource = file3;
		this.tilesize = tilesize;
		this.waittime = waittime;
		this.dest = file4;
		this.numThreads = numThreads;
		this.random = random;
		errorFree = getErrors();
	}

	protected void check(String source, String info, String mosaiquesource,
			int tilesize, int waittime, String destination, int numThreads, boolean random) {
		check(new File(source), new File(info), new File(mosaiquesource), tilesize, waittime, new File(destination), numThreads, random);
		
	}

	private boolean getErrors() {
		if (this.source == null)
			return false;

		if (this.mosaiquesource == null
				&& (!this.info.isFile() || this.info.canRead())) {
			return false;
		}
		if (this.info != null && this.mosaiquesource != null
				&& (!this.info.isFile() || !this.info.canWrite())) {
			return false;
		}
		if (this.mosaiquesource != null
				&& (!this.mosaiquesource.isDirectory() || !this.mosaiquesource
						.canRead())) {
			return false;
		}

		if (getDest() != null && getDest().exists() && !getDest().canWrite()) {
		}
		return true;
	}

	/**
	 * Returns whether the parameters could be parsed without an error.
	 * 
	 * @return <code>true</code> if no error occurred.
	 */
	public boolean isErrorFree() {
		return errorFree;
	}

	/**
	 * Returns the source directory.
	 * 
	 * @return The source directory.
	 */
	public File getSource() {
		return source;
	}
	
	public boolean isRandomized() {
		return random;
	}

	/**
	 * Returns the information (intermediate storage) file.
	 * 
	 * @return The storage file.
	 */
	public File getInfo() {
		if (info.isFile())
			return info;
		return null;
	}

	/**
	 * Returns the source directory that holds the PNG tiles.
	 * 
	 * @return The original file
	 */
	public File getMosaiquesource() {
		if (mosaiquesource.isDirectory())
			return mosaiquesource;
		return null;
	}

	/**
	 * Returns the desired tile size.
	 * 
	 * @return The desired tile size
	 */
	public Integer getTilesize() {
		return tilesize;
	}

	/**
	 * Returns the number of milliseconds to wait after each step.
	 * 
	 * @return The milliseconds to wait
	 */
	public int getWaittime() {
		return waittime;
	}

	/**
	 * Returns the target mosaique file.
	 * 
	 * @return The target file.
	 */
	public File getDest() {
		return dest;
	}

	public int getNumThreads() {
		return numThreads;
	}

	public int getOption() {
		// TODO Auto-generated method stub
		return 0;
	}

}
