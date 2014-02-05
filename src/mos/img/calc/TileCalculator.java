package mos.img.calc;
/**
 * 
 * @author Melanie Meier
 *         Calculates the closest possible square tileSize to the tileSize the
 *         user chose.
 * 
 */
public class TileCalculator {

    private final int imageHeight;
    private final int imageWidth;

    /**
     * public constructor.
     * 
     * @param height
     *            the height of the image
     * @param width
     *            the width of the image
     */
    public TileCalculator(int height, int width) {
        this.imageHeight = height;
        this.imageWidth = width;
    }
    /**
     * return the closest possible tilesize.
     * Uses one method, that increases desired tilesize and checks if it fits to
     * the image size.
     * Uses one method, that decreases desired tilesize and checks if it fits to
     * the image size.
     * if both methods return legal values, the one is chosen, that is closer to
     * the user input.
     * 
     * @param tileSize
     *            the user input.
     * @return the closest possible tilesize.
     */
    public int getOptSize(int tileSize) {
        int result = 0;
    
        // suche nach Werten > dem gewnschten Wert.
        int increasedSize = getIncreased(tileSize);
        // suche nach Werten < dem gewnschten Wert.
        int decreasedSize = getDecreased(tileSize);
        // vergleiche die Ergebnisse.
        if (increasedSize == 0) {
            if (decreasedSize == 0) {
                result = 0;
            } else {
                result = decreasedSize;
            }
        }
        else if (decreasedSize == 0) {
            result = increasedSize;
            // wenn es zwei g�ltige Ergebnisse gibt, wird das mit dem kleinsten Abstand zum gew�nschten Wert �bergeben.
        } else if (increasedSize > 0 && decreasedSize > 0) {
        	// berechne die Differenzen
            int upDif = Math.abs(tileSize - increasedSize);
            int lowDif = Math.abs(tileSize - decreasedSize);

            if (upDif > lowDif) {
                result = decreasedSize;
            } else {
                result = increasedSize;
            }
        }
		return result;
    }

    /**
     * look for square tilesize that is lower than the desired one.
     * 
     * @param tileSize the userinput
     * 
     * @return the fitting tilesize or -1 if not found.
     */
    private int getDecreased(int tileSize) {
        for (int i = tileSize; i > 20; i--) {
            if (imageHeight % i == 0 && imageWidth % i == 0) {
                return i;
            }
        }
        return 0;
    }

    /**
     * look for square tilesize that is higher than the desired one.
     * 
     * @param tileSize the userinput.
     * @return a fitting tilesize or -1 if not found.
     */
    private int getIncreased(int tileSize) {
    	int lengthOfShortSide = imageHeight;
    	if (lengthOfShortSide > imageWidth) {
    		lengthOfShortSide = imageWidth;
    	}
        for (int i = tileSize; i < lengthOfShortSide; i++) {
            if (imageHeight % i == 0 && imageWidth % i == 0) {
                return i;
            }
        }
        return 0;
    }

}
