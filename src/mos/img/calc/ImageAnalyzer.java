package mos.img.calc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * 
 * @author Melanie Meier. Analyzes a bufferedImage.
 * 
 */
public class ImageAnalyzer {

	private final BufferedImage image;
	private final int height;
	private final int width;

	/**
	 * public constructor.
	 * 
	 * @param image
	 *            the bufferedImage.
	 */
	public ImageAnalyzer(BufferedImage image) {
		this.image = image;
		this.height = image.getHeight();
		this.width = image.getWidth();
	}

	/**
	 * returns the height of a bufferedImage.
	 * 
	 * @return height the height in pixels.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * returns he width of a bufferedImage.
	 * 
	 * @return width the width in pixels.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * create tiles of the bufferedImage. The tiles of the image are saved in an
	 * arrayList by splitting the image in its accordingly subImages.
	 * 
	 * @param tileSize the size of a tile
	 * @return an arrayList with every tile as BufferedImage.
	 */
	public ArrayList<BufferedImage> createTiles(int tileSize) {
		ArrayList<BufferedImage> tiles = new ArrayList<BufferedImage>();

		if (width % tileSize != 0 || height % tileSize != 0) {
			TileCalculator tileCal = new TileCalculator(height, width);
			tileSize = tileCal.getOptSize(tileSize);
		}
		for (int x = 0; x < width; x += tileSize) {
			for (int y = 0; y < height; y += tileSize) {
				BufferedImage tile = image.getSubimage(x, y, tileSize,
				        tileSize);
				tiles.add(tile);
			}
		}
		return tiles;
	}

}
