package mos.init;

import java.io.File;


import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;

/**
 * This class handles the program arguments.
 * 
 * @author Thomas Karcher
 * 
 */
public class Configuration {

    @Option(name = "-s", aliases = { "--source" }, required = true,
            usage = "Filename for the original image that should be mosaiquized.")
    private File source;

    @Option(name = "-i", aliases = { "--info" }, required = false,
            usage = "Filename for the intermediate storage of mosaique tile information.")
    private File info;

    @Option(name = "-m", aliases = { "--mosaiquesource" }, required = false,
            usage = "Directory that holds the PNG files that act as mosaique tiles.")
    private File mosaiquesource;

    @Option(name = "-t", aliases = { "--tilesize" }, required = true,
            usage = "Desired size of the mosaique's tiles (approximately).")
    private Integer tilesize;

    @Option(name = "-d", aliases = { "--dest" }, required = false,
            usage = "Filename for the destination (mosaique) image.")
    private File dest;
    
    @Option(name = "-w", aliases = { "--waittime" }, required = false,
            usage = "Milliseconds to wait after each step.")
    private Long waittime;
    
    @Option(name = "-p", aliases = { "--parallelism" }, required = false,
            usage = "Number of threads used for parallel execution (if not given, default is used).")
    private int numThreads = 1;

    private boolean errorFree = false;

    /**
     * Parses the command line arguments using <a
     * href="http://args4j.java.net/">args4j</a> .
     * 
     * @param args
     *            The program arguments:
     *            <ul>
     *            <li>-s (--source) DIRECTORY</li>
     *            <li>-t (--target) FILE</li>
     *            <li>-f (--sourcefile) FILE</li>
     *            </ul>
     */
    public Configuration(String... args) {
        CmdLineParser parser = new CmdLineParser(this);
        parser.setUsageWidth(80);

        try {
            parser.parseArgument(args);

            if (args.length == 0) {
                throw new CmdLineException(parser, "No options given. Don't know what to do...");
            }
            
            if (getInfo() != null && getMosaiquesource() == null
                    && (!getInfo().isFile() || !getInfo().canRead())) {
                throw new CmdLineException(parser,
                        "--info is not a readable file");
            }
            if (getInfo() != null && getMosaiquesource() != null
                    && (!getInfo().isFile() || !getInfo().canWrite())) {
                throw new CmdLineException(parser,
                        "--info is not a writable file.");
            }
            if (getMosaiquesource() != null
                    && (!getMosaiquesource().isDirectory() || !getMosaiquesource().canRead())) {
                throw new CmdLineException(parser,
                        "--mosaiquesource is not a readable directory.");
            }

            if (getDest() != null && getDest().exists() && !getDest().canWrite()) {
                throw new CmdLineException(parser,
                        "--dest is not a writable file.");
            }

            errorFree = true;
        } catch (CmdLineException e) {
            System.err.println("java -jar Mosaique.jar [options...]");
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("Example: java -jar Mosaique.jar"
                    + parser.printExample(ExampleMode.ALL) + "\n");
            System.err.println(e.getMessage());
        }
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