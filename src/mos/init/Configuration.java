package mos.init;

import java.io.File;

abstract class Configuration {

	private boolean errorFree;
	private File source;
	private File info;
	private File mosaiquesource;
	private Integer tilesize;
	private Long waittime;
	private File dest;
	private int numThreads;

	public Configuration() {
	}
	
	public void check(File file, File file2, File file3,
			int tilesize, long waittime, File file4, int numThreads) {

		this.source = file;
		this.info = file2;
		this.mosaiquesource = file3;
		this.tilesize = tilesize;
		this.waittime = waittime;
		this.dest = file4;
		this.numThreads = numThreads;
		errorFree = getErrors();
	}

	protected void check(String source, String info, String mosaiquesource,
			int tilesize, long waittime, String destination, int numThreads) {
		check(new File(source), new File(info), new File(mosaiquesource), tilesize, waittime, new File(destination), numThreads);
		
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

	/**
	 * Returns the information (intermediate storage) file.
	 * 
	 * @return The storage file.
	 */
	public File getInfo() {
		return info;
	}

	/**
	 * Returns the source directory that holds the PNG tiles.
	 * 
	 * @return The original file
	 */
	public File getMosaiquesource() {
		return mosaiquesource;
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
	public Long getWaittime() {
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
