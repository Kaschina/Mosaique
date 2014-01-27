package mos.img.calc;

import java.util.HashMap;

public class Factory {

	
	private static Factory factory;

	private HashMap<String, MyBufferedImage> pool;
	
	/**
	 * private constructor creates an instance of factory and a hashmap. 
	 */
	private Factory() {
		pool = new HashMap<String, MyBufferedImage>();
	}
	
	/**
	 * only one instance of factory can be created.
	 * @return factory. 
	 */
	public static Factory getInstance() {
		if (factory == null) {
			factory = new Factory();
		}
		return factory;
	}
/**
 * gives an existing mybufferedImage back or creates a new one, if not existing yet.
 * @param pathOfFittingImage the path of the mybufferedImage
 * @return mybufferedImage
 */
	public MyBufferedImage getMyBufferedImage(String pathOfFittingImage) {
	   
		
		if (pool.containsKey(pathOfFittingImage)) {
			return pool.get(pathOfFittingImage);
		} else {
			MyBufferedImage im = new MyBufferedImage(pathOfFittingImage);
			pool.put(pathOfFittingImage, im);
			return im;
		}
		
    }
	
	
}
