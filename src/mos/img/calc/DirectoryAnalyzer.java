package mos.img.calc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import mos.avg.handler.AverageCreator;
import mos.avg.handler.ImageValue;
import mos.init.RnW;

public class DirectoryAnalyzer {
	
	public ArrayList<ImageValue> createList(File mosaiqueSource) {
		RnW read = new RnW();
		/**
		 * get all images of a directory.
		 */
		File[] images = read.getImages(mosaiqueSource);
		/**
		 * get rgb-value of every image and save it in a list.
		 */
		ArrayList<ImageValue> mosaiqueList = new ArrayList<ImageValue>();
		AverageCreator av = new AverageCreator();
		for (File image : images) {
			BufferedImage im;
			try {
				im = ImageIO.read(image);
				short[] rgb = av.getAverage(im);
				ImageValue x = new ImageValue(rgb, image.getAbsolutePath());
				mosaiqueList.add(x);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		return mosaiqueList;
	}
	
	public  void createList(File mosaiqueSource, File destination) {
		ArrayList<ImageValue> resultList = createList(mosaiqueSource);
		RnW write = new RnW();
		write.writeList(destination, resultList);
	}
	
	

}
