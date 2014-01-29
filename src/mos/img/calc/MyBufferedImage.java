package mos.img.calc;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MyBufferedImage {

    private final String path;
    private BufferedImage original;

    public MyBufferedImage(String path) {
        this.path = path;
        try {
            this.original = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Bild nicht lesbar!");
        }
    }

    public String getPath() {
        return path;
    }
    
    public int getType() {
        return original.getType();
    }
    
    public BufferedImage getOriginal() {
    	return original;
    }

    public BufferedImage getResizedImage(int tileSize, int type) {
        BufferedImage dimg = new BufferedImage(tileSize, tileSize, type);
        Graphics2D g = dimg.createGraphics();
        g.drawImage(original, 0, 0, tileSize, tileSize, 0, 0,
                original.getWidth(), original.getHeight(), null);
        return dimg;
    }
}
