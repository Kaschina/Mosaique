package mos.init;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import mos.img.calc.CreateMosaique;
import mos.img.calc.ImageAnalyzer;

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
	 * @param args
	 *            4 arguments are needed, 5 possible.
	 */
	public static void main(String[] args) {
		Configuration config = new Configuration(args);
		/**
		 * If errors in arguments, exit program.
		 */
		if (!config.isErrorFree()) {
			System.exit(0);
		} else {
			time = -System.currentTimeMillis();
			init(config);
		}
	}

	private static void init(Configuration config) {

		File source = config.getSource();
		BufferedImage sourceImage = null;
		try {
			sourceImage = ImageIO.read(source);
		} catch (IOException e1) {
			System.err.print("Bild beschaedigt.");
			e1.printStackTrace();
		}

		/**
		 * if bufferedImage is errorfree analyze it and get its tiles.
		 */
		if (sourceImage != null) {
			ImageAnalyzer analyzer = new ImageAnalyzer(sourceImage);
			int tileSize = config.getTilesize();

			ArrayList<BufferedImage> tiles = analyzer.createTiles(tileSize);
			if (tiles.size() > 0) {
				/**
				 * check what option is chosen.
				 */

				CreateMosaique creator = new CreateMosaique(tiles, sourceImage);
				BufferedImage result = null;

				/**
				 * If only argument for mosaiquesource is given, get the
				 * rgb-values of each image of mosaiquesource.
				 */
				if (config.getMosaiquesource() != null) {
					/**
					 * get each image of mosaiquesource.
					 */
					File mosaiqueSource = config.getMosaiquesource();
					result = creator.createImageFromSource(mosaiqueSource);
					/**
					 * if argument for info is given also write results to list.
					 */
					if (config.getInfo() != null) {
						File info = config.getInfo();

						creator.writeInfoList(info);
					}

					/**
					 * If only argument for info is given, get the list from
					 * file info.
					 */
					else if (config.getInfo() != null) {
						File info = config.getInfo();
						mosaiqueSource = config.getMosaiquesource();
						result = creator.createImageFromList(info);

					} else {
						System.err.println("no valid option found");
					}
				}
				/**
				 * store result at destination.
				 */
				File dest = config.getDest();

				try {
					ImageIO.write(result, "png", dest);
					System.out.println("Bild erfolgreich gespeichert unter "
					        + dest.getAbsolutePath());
					System.out.println(time + System.currentTimeMillis() + "ms");
					System.exit(0);
				} catch (IOException e) {
					System.out.println(e);
				}
			} else {
				System.err.println("Kacheln konnten nicht erstellt werden.");
			}

		}
	}

}
