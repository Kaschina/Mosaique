package mos.img.calc;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
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
	//private final int tilesWidth;
	/**
	 * the height of the sourceImage.
	 */
	private final int sourceHeight;
	/**
	 * the width of the sourceImage.
	 */
	private final int sourceWidth;

	private final Factory imgFactory;

	private final int numberOfThreads;
	
	private final RnW readWrite;
	private final int type;
	private JLabel label;
//	private final int waittime;
	private BufferedImage griddedImage;
	HashMap<Integer, MyBufferedImage> fittingImages;

	public CreateMosaique(ArrayList<BufferedImage> tiles, BufferedImage source,
			int numberOfThreads, int waittime, JLabel label) {
		this(tiles, source, numberOfThreads, waittime);
		this.label = label;

		this.label.setText("test");
		this.label.paintImmediately((int) label.getLocation().getX(),
				(int) label.getLocation().getY(), label.getWidth(),
				label.getHeight());
		this.label.getParent().getParent().repaint(0, 0, 800, 800);
		
		ImageIcon icon = (ImageIcon) label.getIcon();
		this.griddedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		this.griddedImage.getGraphics().drawImage(icon.getImage(), 0,0, icon.getImageObserver());
		
	}

	/**
	 * public constructor.
	 * 
	 * @param tiles
	 *            the tiles of a sourceImage.
	 * @param source
	 *            the sourceImage.
	 * @param numberOfThreads
	 *            the number of threads
	 */
	public CreateMosaique(ArrayList<BufferedImage> tiles, BufferedImage source,
			int numberOfThreads, int waittime) {
		this.tiles = tiles;
		this.tilesHeight = tiles.get(0).getHeight();
		//this.tilesWidth = tiles.get(0).getWidth();
		this.sourceHeight = source.getHeight();
		this.sourceWidth = source.getWidth();
		this.type = source.getType();
		this.imgFactory = Factory.getInstance();
		this.numberOfThreads = numberOfThreads;
		this.readWrite = new RnW();
		//this.waittime = waittime;
		this.fittingImages = new HashMap<Integer, MyBufferedImage>();
	}
	
	public void updateFittingImages(int index, MyBufferedImage bi) {
		fittingImages.put(index, bi);
	}

	/**
	 * create a mosaiqueImage with informations of a infoFile.
	 * 
	 * @param info
	 *            the File that stores a list of Imagevalues.
	 * @return mosaique the mosaiqueImage.
	 */
	public BufferedImage createImageFromList(File info) {
		ArrayList<ImageValue> mosaiqueList = readWrite.readList(info);
		BufferedImage mosaique = createImage(mosaiqueList);
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
		DirectoryAnalyzer dirana = new DirectoryAnalyzer();
		
		ArrayList<ImageValue> mosaiquelist = dirana.createList(mosaiqueSource);
		BufferedImage mosaique = createImage(mosaiquelist);
		return mosaique;

	}
	
	/**
	 * create a mosaiqueImage from given tiles.
	 * 
	 * @return mosaique the mosaiqueImage.
	 */
	private BufferedImage createImage(ArrayList<ImageValue> mosaiqueList) {
		/**
		 * list that stores the rgb-values of every tile
		 */
		ArrayList<short[]> rgbOfTiles = getRgbOfTiles();

		/**
		 * calculate the difference of rgb-values of every tile and every image
		 * of the source. Store the index of the fitting images in a list.
		 */

		int numberOfTiles = rgbOfTiles.size();


		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
		for (int i = 0; i < numberOfTiles; i++) {
			short[] rgbTile = rgbOfTiles.get(i);
			
			// myfittingImages is created
			Runnable worker = new FittingImageCalculator(this, rgbTile, mosaiqueList, i, imgFactory);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
			
		}
		executor = Executors.newFixedThreadPool(numberOfThreads);
		for (Map.Entry<Integer, MyBufferedImage> entry : fittingImages.entrySet()) {
			int index = entry.getKey();
			//get resized image of actual tile
			BufferedImage tile = entry.getValue().getResizedImage(tilesHeight, type);
			Point point = calculate(sourceWidth, sourceHeight, tilesHeight, index);

			Runnable worker = new ImagePainter(this, tile, index, point);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		return griddedImage;
	}
	
	public void paint(BufferedImage tile, Point point) {
		griddedImage.createGraphics().drawImage(tile, (int)point.getX(), (int)point.getY(), null);
		label.setIcon(new ImageIcon(griddedImage));
		label.paintImmediately(0, 0, 400, 400);
	}
	
	public Point calculate(int sourceWidth, int sourceHeight, int tilesize, int index) {
		// Anzahl Kacheln nach oben berechnen
		int numHeight = sourceHeight / tilesize;
		int x = index / numHeight;
		int y = index % numHeight;
		x *= tilesize;
		y *= tilesize;

		return new Point (x , y);
		
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
	HashMap<Integer, String> calculateDifference(short[] rgbTile, ArrayList<ImageValue>mosaiqueList) {
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

	public int getNumberOfThreads() {
		return numberOfThreads;
	}

}

class ImagePainter implements Runnable {
	CreateMosaique cm;
	BufferedImage tile;
	int index;
	Point point;
	public ImagePainter(CreateMosaique cm, BufferedImage tile, int index, Point point) {
		this.cm = cm;
		this.tile = tile;
		this.index = index;
		this.point = point;
	}
	
	@Override
	public void run() {
		paint();
	}
	private void paint() {
		cm.paint(tile, point);
	}
}

class FittingImageCalculator implements Runnable {
	short[] rgbTile;
	ArrayList<ImageValue> mosaiqueList;
	CreateMosaique cm;
	private Factory imgFactory;
	private int index;
	
	public FittingImageCalculator(CreateMosaique cm, short[] rgbTile, 
			ArrayList<ImageValue> mosaiqueList, int index, Factory imgFactory) {
		this.cm = cm;
		this.rgbTile = rgbTile;
		this.mosaiqueList = mosaiqueList;
		this.index = index;
		this.imgFactory = imgFactory;
	}
	@Override
	public void run() {
		processCommand();
	}
	
	private void processCommand() {
		HashMap<Integer, String>diffList = new HashMap<Integer, String>();

		/**
		 * the list with the differences of one tile and every image of
		 * mosaiqueList.
		 */
		diffList = cm.calculateDifference(rgbTile, mosaiqueList);

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
		cm.updateFittingImages(index, myFittingImage);
	}
}