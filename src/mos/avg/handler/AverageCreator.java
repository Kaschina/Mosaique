package mos.avg.handler;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Melanie Meier
 *         The Class calculates the average of the rgb-channel of an image.
 * 
 */
public class AverageCreator {

    /**
     * returns the average rgb value of a bufferedImage.
     * With getRgb()the rgb value of every pixel is stored in an intArray.
     * All red, green and blue values of every pixel are added and divided
     * by the number of pixels.
     * 
     * @param buffIm
     *            the buff to be analyzed.
     * @return the average as shortArray[r,g,b]
     */
    private short[] privGetAverage(BufferedImage buffIm) {
        short[] averageRgb;
        try {
        int width = buffIm.getWidth();
        int height = buffIm.getHeight();
        int[] pixels = new int[width * height];
        /**
         * stores the rgb-value of every pixel.
         */
        int[] rgbAtPos = buffIm.getRGB(0, 0, width, height, pixels, 0, width);

        long[] rgb = new long[3];
        int size = rgbAtPos.length;

        /**
         * get the red, green and blue values of every entry.
         */
        for (int i = 0; i < size; i++) {
            long rgbLong = rgbAtPos[i];
            /**
             * filter each value of rgb.
             */
            rgb[0] += (rgbLong & 0xFF0000) >> 16;
            rgb[1] += (rgbLong & 0x00FF00) >> 8;
            rgb[2] += (rgbLong & 0x0000FF);
        }

        /**
         * get the average of each color by dividing with size of pixelArray
         * and don´t forget the decimal place...
         */
        averageRgb = new short[3];
        for (int i = 0; i < 3; i++) {
            averageRgb[i] = (short) Math.round((double) rgb[i] / size);
        }

        }
        catch(Exception e) {
            System.err.println("average rgb lässt sich nicht bilden von " );
            averageRgb = null;
        }
        return averageRgb;
    }

    /**
     * returns the average of buffIm.
     * A public Method that calls the private getAverage() method.
     * 
     * @param buffIm
     *            the image.
     * @return the average rgb-value of buffIm
     *         using the private method.
     */
    public short[] getAverage(BufferedImage buffIm) {
        return privGetAverage(buffIm);
    }

}
