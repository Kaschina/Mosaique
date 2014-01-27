package mos.img.calc;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import mos.avg.handler.AverageCreator;
import mos.avg.handler.ImageValue;
import mos.init.RnW;

/**
 * 
 * @author Melanie Meier Creates a mosaiqueImage in different ways. The source
 *         can be a list, that stores imagevalues - the path and the rgb-value
 *         of images or directories that contain images. If wanted, the
 *         informations about images are written to a list.
 */
public class CreateMosaique {
	/**
	 * the sourceImage.
	 */
	private final BufferedImage source;
	/**
	 * the list with all tiles of the sourceImage.
	 */
	private final ArrayList<BufferedImage> tiles;
	/**
	 * the height of tiles.
	 */
	private final int tilesHeight;
	/**
	 * the width of tiles.
	 */
	private final int tilesWidth;
	/**
	 * the height of the sourceImage.
	 */
	private final int sourceHeight;
	/**
	 * the width of the sourceImage.
	 */
	private final int sourceWidth;

	private Factory imgFactory;

	/**
	 * the list with all imageValues that can be used to create a mosaic.
	 */
	private ArrayList<ImageValue> mosaiqueList;
	private RnW readWrite;
	private HashMap<Integer, String> diffList;
	private int type;
	private JFrame frame;
	private JLabel label;

	/**
	 * public constructor.
	 * 
	 * @param tiles
	 *            the tiles of a sourceImage.
	 * @param source
	 *            the sourceImage.
	 */
	public CreateMosaique(ArrayList<BufferedImage> tiles, BufferedImage source) {
		this.tiles = tiles;
		this.tilesHeight = tiles.get(0).getHeight();
		this.tilesWidth = tiles.get(0).getWidth();
		this.source = source;
		this.sourceHeight = source.getHeight();
		this.sourceWidth = source.getWidth();
		this.mosaiqueList = new ArrayList<ImageValue>();
		this.type = source.getType();
		this.imgFactory = Factory.getInstance();
		readWrite = new RnW();

		frame = new JFrame("testbild");
		frame.setVisible(true);
		frame.setSize(sourceWidth + 100, sourceHeight + 100);
		label = new JLabel();
		label.setIcon(new ImageIcon(source));
		frame.add(BorderLayout.CENTER, label);
		System.out.println(sourceWidth + " " + sourceHeight + " Grösse des Bildes");
		
	}

	/**
	 * create a mosaiqueImage with informations of a infoFile.
	 * 
	 * @param info
	 *            the File that stores a list of Imagevalues.
	 * @return mosaique the mosaiqueImage.
	 */
	public BufferedImage createImageFromList(File info) {

		BufferedImage mosaique = null;

		mosaiqueList = readWrite.readList(info);
		mosaique = createImage();
		return mosaique;
	}

	/**
	 * Create a MosaiqueImage from a directory with images. For every image the
	 * rgb values are set and saved in mosaiquelist.
	 * 
	 * @param mosaiqueSource
	 *            the directory that contains images.
	 * @return mosaique the mosaiqueImage
	 */
	public BufferedImage createImageFromSource(File mosaiqueSource) {
		BufferedImage mosaique = null;
		/**
		 * get all images of a directory.
		 */
		File[] images = readWrite.getImages(mosaiqueSource);
		/**
		 * get rgb-value of every image and save it in a list.
		 */
		mosaiqueList = new ArrayList<ImageValue>();
		AverageCreator av = new AverageCreator();
		for (int i = 0; i < images.length; i++) {
			BufferedImage im = null;
			try {
				im = ImageIO.read(images[i]);
				short[] rgb = av.getAverage(im);
				ImageValue x = new ImageValue(rgb, images[i].getAbsolutePath());
				mosaiqueList.add(x);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		mosaique = createImage();
		return mosaique;
	}

	/**
	 * create a mosaiqueImage from given tiles.
	 * 
	 * @return mosaique the mosaiqueImage.
	 */
	private BufferedImage createImage() {

		/**
		 * list that stores the rgb-values of every tile
		 */
		ArrayList<short[]> rgbOfTiles = getRgbOfTiles();

		/**
		 * calculate the difference of rgb-values of every tile and every image
		 * of the source. Store the index of the fitting images in a list.
		 */
		ArrayList<MyBufferedImage> fittingImages = new ArrayList<MyBufferedImage>();

		int numberOfTiles = rgbOfTiles.size();

		for (int i = 0; i < numberOfTiles; i++) {
			short[] rgbTile = rgbOfTiles.get(i);
			diffList = new HashMap<Integer, String>();

			/**
			 * the list with the differences of one tile and every image of
			 * mosaiqueList.
			 */
			diffList = calculateDifference(rgbTile);

			/**
			 * search the element with the smallest difference.
			 */
			ArrayList<Integer> sortedList = new ArrayList<Integer>();
			sortedList.addAll(diffList.keySet());
			Collections.sort(sortedList);
			int smallestElem = sortedList.get(0);

			/**
			 * the path to the fitting image.
			 */
			String pathOfFittingImage = diffList.get(smallestElem);
			MyBufferedImage myFittingImage = imgFactory
			        .getMyBufferedImage(pathOfFittingImage);
			fittingImages.add(myFittingImage);
		}

		frame.repaint();
		int index = 1;
		Iterator<MyBufferedImage> it = fittingImages.iterator();
		System.out.println(fittingImages.size() + " Teile");
		
		for (int x = 0; x < sourceWidth; x += tilesWidth) {
			for (int y = 0; y < sourceHeight; y += tilesHeight) {
				MyBufferedImage now = it.next();
				/**
				 * the fitting tile at the actual position in the sourceImage.
				 */
				BufferedImage tile = now.getResizedImage(
				        tilesHeight, type);
				/**
				 * scale original down and insert it at the accordingly
				 * position.
				 */
				source.createGraphics().drawImage(tile, x, y, null);
				/**try {
	                Thread.sleep(4000);
                } catch (InterruptedException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
                }**/
					frame.repaint();
					index++;
			}
		}
		System.out.println("fertig!");
		return source;

	}

	/**
	 * Compares the rgb value of an imageTile with each image of mosaiqueList.
	 * The difference of each value (red, green and blue) is squared and the
	 * result of the three values are added. The results are stored in a list.
	 * 
	 * @param rgbTile
	 *            the tile to be compared with the mosaiquelist.
	 * @return a list with the differences of each image of mosaiquelist.
	 */
	private HashMap<Integer, String> calculateDifference(short[] rgbTile) {
		HashMap<Integer, String> differenceList = new HashMap<Integer, String>();
		int size = mosaiqueList.size();
		int tileSize = rgbTile.length;

		for (int i = 0; i < size; i++) {
			int diff = 0;
			short[] rgbMosSource = mosaiqueList.get(i).getRgb();
			for (int j = 0; j < tileSize; j++) {
				int x = rgbTile[j];
				int y = rgbMosSource[j];
				diff += Math.pow((Math.abs(x - y)), 2);
			}
			differenceList.put(diff, mosaiqueList.get(i).getPath());
		}
		return differenceList;
	}

	/**
	 * The rgb-value of every image is analyzed and stored in a list.
	 * 
	 * @return rgbOfTiles the list that contains the rgb-values of every tile.
	 */
	private ArrayList<short[]> getRgbOfTiles() {
		/**
		 * get the rgb-values of each tile and store in a list.
		 */
		AverageCreator av = new AverageCreator();
		int numberOfTiles = tiles.size();
		ArrayList<short[]> rgbOfTiles = new ArrayList<short[]>();

		for (int index = 0; index < numberOfTiles; index++) {
			BufferedImage tile = tiles.get(index);
			short[] rgbTile = av.getAverage(tile);
			rgbOfTiles.add(rgbTile);
		}
		return rgbOfTiles;
	}

	/**
	 * writes the mosaiqueList to a file.
	 * 
	 * @param info
	 *            the file where mosaiqueList has to be written to.
	 */
	public void writeInfoList(File info) {
		readWrite.writeList(info, mosaiqueList);
	}

}
