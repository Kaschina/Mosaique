/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mos.init;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import mos.img.calc.CreateMosaique;
import mos.img.calc.DirectoryAnalyzer;
import mos.img.calc.ImageAnalyzer;
import mos.img.calc.TileCalculator;
import mos.ui.Uiuiui;

/**
 * 
 * @author melanie
 */
public class Initializer extends Thread {

	Uiuiui ui;
	Configuration config;
	ImageIcon icon;
	public Initializer(Uiuiui ui, Configuration config, ImageIcon icon) {
		this.ui = ui;
		this.config = config;
		this.icon = icon;
	}
	@Override
	public void run() {
		init();
	}
	/**
	 * get all informations from config and start editing the desired picture.
	 * 
	 * @param config
	 *            your config
	 */
	public void init() {

		// just to know how long it takes
		long time = -System.currentTimeMillis();

		int numberOfThreads = config.getNumThreads();
		int waittime = (int)config.getWaittime();
		// get path of picture that has to be edited.
		File source = config.getSource();
		BufferedImage sourceImage = null;
		try {
			sourceImage = ImageIO.read(source);
		} catch (IOException e1) {
			System.err.print("Bild beschaedigt.");
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

				CreateMosaique creator = null;
				System.out.println("erzeuge creator");
				
				creator = new CreateMosaique(tiles, sourceImage,
						numberOfThreads, waittime, config.isRandomized(), ui, icon);
				BufferedImage result = null;
				File mosaiqueSource = config.getMosaiquesource();
				/**
				 * If only argument for mosaiquesource is given, get the
				 * rgb-values of each image of mosaiquesource.
				 */
				if (mosaiqueSource != null) {
					/**
					 * get each image of mosaiquesource.
					 */
					System.out.println("create from source");
					result = creator.createImageFromSource(mosaiqueSource);
				}/**
				 * If only argument for info is given, get the list from file
				 * info.
				 */
				else if (config.getInfo() != null) {
					File info = config.getInfo();
				result = creator.createImageFromList(info);
				}

				/**
				 * store result at destination.
				 */
				File dest = config.getDest();
				if (dest.isDirectory()) {
					String newFile = dest.getAbsolutePath() + "/"
							+ source.getName().substring(0, source.getName().indexOf(".")) + "mosaique.png";
					System.out.println(newFile);
					dest = new File(newFile);

					try {
						dest.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					ImageIO.write(result, "png", dest);
					
					System.out.println("print " + dest);
					System.out
							.println(time + System.currentTimeMillis() + "ms");
				} catch (IOException e) {
					System.out.println(e);
				}
			} else {
				System.err.println("Kacheln konnten nicht erstellt werden.");
			}
		}
	}

	public int getOptTileSize(BufferedImage img, int tileSize) {
		if (tileSize == 0) {
			tileSize = 1;
		}
		TileCalculator calculator = new TileCalculator(img.getHeight(),
				img.getWidth());
		return calculator.getOptSize(tileSize);
	}
	
	public void createInfoFile(String info, String mosaiqueSource) {
		DirectoryAnalyzer dirana = new DirectoryAnalyzer();
		dirana.createList(new File(mosaiqueSource), new File(info));
		
	}
}
