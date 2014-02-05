package mos.img.calc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Painter {

	private final BufferedImage image;
	private final int tileSize;
	private final BufferedImage griddedImage;
	private final int height;
	private final int width;
	
	public Painter(BufferedImage image, int tileSize) {
		this.image = image;
		this.tileSize = tileSize;
		this.height = image.getHeight();
		this.width = image.getWidth();
		this.griddedImage = copyOriginal();
		
	}
	
	/**
	 * create a copy of original image
	 * @return a copy of original
	 */
	private BufferedImage copyOriginal() {
		BufferedImage result = new BufferedImage(height, width, image.getType());
		Graphics2D graphics= result.createGraphics();
		graphics.drawImage(image, 0, 0, height, width, 0,0, height, width, null);
		return result;
	}
	
	/**
	 * 
	 * @return griddedimage
	 */
	public BufferedImage getCopy() {
		return griddedImage;
	}
	public BufferedImage paintGrid() {
		
		BufferedImage grid = new BufferedImage(width, height, image.getType());
		Graphics2D graphics = grid.createGraphics();
		graphics.drawImage(image, 0, 0, height, width, 0,0, height, width, null);
		graphics.setColor(new Color(0, 255, 255));
		for (int i = 0; i <= width; i+= tileSize) {
			for (int j = 0; j <= height; j += tileSize) {
				graphics.drawLine(0, i, width, i);
				graphics.drawLine(j, 0, j, width);
			}
		graphics.drawLine(0, height -1, width, height -1);
		graphics.drawLine(height -1, 0, height - 1,  width);
		}
		return grid;
	}
}
