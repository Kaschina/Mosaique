package mos.init;

import java.io.File;

import org.kohsuke.args4j.CmdLineException;


import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;

/**
 * This class handles the program arguments.
 * 
 * @author Thomas Karcher
 * 
 */

public final class TermConfiguration extends Configuration {
    @Option(name = "-s", aliases = { "--source" }, required = true,
            usage = "Filename for the original image that should be mosaiquized.")
    private String source;

    @Option(name = "-i", aliases = { "--info" }, required = false,
            usage = "Filename for the intermediate storage of mosaique tile information.")
    private String info;

    @Option(name = "-m", aliases = { "--mosaiquesource" }, required = false,
            usage = "Directory that holds the PNG files that act as mosaique tiles.")
    private String mosaiquesource;

    @Option(name = "-t", aliases = { "--tilesize" }, required = true,
            usage = "Desired size of the mosaique's tiles (approximately).")
    private Integer tilesize;

    @Option(name = "-d", aliases = { "--dest" }, required = false,
            usage = "Filename for the destination (mosaique) image.")
    private String dest;
    
    @Option(name = "-w", aliases = { "--waittime" }, required = false,
            usage = "Milliseconds to wait after each step.")
    private Long waittime;
    
    @Option(name = "-p", aliases = { "--parallelism" }, required = false,
            usage = "Number of threads used for parallel execution (if not given, default is used).")
    private int numThreads = 1;
    
    
    public TermConfiguration(String... args) {
    	super();
    	
        CmdLineParser parser = new CmdLineParser(this);
        parser.setUsageWidth(80);
        

        try {
            parser.parseArgument(args);

            if (args.length == 0) {
                throw new CmdLineException(parser, "No options given. Don't know what to do...");
            }
            check( getSource(),  getInfo(),  getMosaiquesource(),
			 getTilesize(), getWaittime(),  getDest(),  getNumThreads());
        } catch (CmdLineException e) {
            System.err.println("java -jar Mosaique.jar [options...]");
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("Example: java -jar Mosaique.jar"
                    + parser.printExample(ExampleMode.ALL) + "\n");
            System.err.println(e.getMessage());
        }
    }
}