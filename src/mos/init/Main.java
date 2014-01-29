package mos.init;

/**
 * A mosaique image is developped.
 *
 * @author Melanie Meier.
 *
 */
abstract class Main {

    private static long time;

    /**
     * the main method.
     *
     * @param args 4 arguments are needed, 5 possible.
     */
    public static void main(String[] args) {
        Configuration config = new Configuration(args);
        /**
         * If errors in arguments, exit program.
         */
        if (!config.isErrorFree()) {
            System.exit(0);
        } else {
            Initializer.init(config);
        }
    }
}
