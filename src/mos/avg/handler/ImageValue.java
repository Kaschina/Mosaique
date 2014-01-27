package mos.avg.handler;

import java.io.Serializable;

/**
 * 
 * @author Melanie Meier
 * 
 * This class saves the path of the file and its rgb-average Value.
 */
public class ImageValue implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final byte        tres             = 3;
    /**
     * contains the rgb values[r,g,b];
     */
    private final short[]     rgb;

    /**
     * the path of the image as String.
     */
    private final String      path;

    /**
     * public Constructor.
     * 
     * @param averageRGB
     *            the average rgbValues of the image
     * @param path
     *            the path of the Image
     */
    public ImageValue(short[] averageRGB, String path) {
        this.path = path;
        this.rgb  = averageRGB;
    }

    /**
     * Returns the average RGB-Value as hexValues.
     * 
     * @return a String of hex
     */
    private String valueInHex() {
        String rgbString = "";

        for (int i = 0; i < tres; i++) {
            rgbString += String.format("%02x ", rgb[i]);
        }
        return rgbString.trim();
    }

    /**
     * The object turned to a string.
     *  
     * @return the String representing the ImagevalueObject.
     */
    public String toString() {
        return valueInHex() + " " + getPath() + "\n";
    }

    /**
     * returns the name of the path.
     * 
     * @return path the path of image.
     */
    public String getPath() {
        return path;
    }
    
    /**
     * returns the rgbValue of the image.
     * @return rgb the rgb-value.
     */
    public short[] getRgb() {
        return rgb;
    }
}
