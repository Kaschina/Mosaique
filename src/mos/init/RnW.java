package mos.init;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import mos.avg.handler.ImageValue;

/**
 * @author Melanie Meier
 * 
 * This class returns all .png files of a directory.
 * Writes a list to a specific file.
 * 
 */
public class RnW {

   
    /**
     * a private method that returns the images of the chosen directory in a
     * fileArray.
     * 
     * @return images an array with imageFiles.
     */
    private File[] listImages(File file) {

        /**
         * a filter that accepts only .png files
         */
        FileFilter filter = new FileFilter() {
            public boolean accept(File x) {
                if (x.getName().endsWith(".png")) {
                    return true;
                }
                return false;
            }
        };

        File[] images = file.listFiles(filter);
        return images;
    }

    /**
     * public method to get all images of a direction using the private method.
     * 
     * @return images the array of imageFiles.
     */
    public File[] getImages(File file) {
    	File[]images = listImages(file);
        if (directoryIsEmpty(images)) {
            System.err.println("Das Verzeichnis enthält keine Bilder!");
        }
        return listImages(file);
    }

    /**
     * returns if a directory contains images or not.
     * @param images 
     * 
     * @return true, if empty. False if not.
     */
    public boolean directoryIsEmpty(File[] images) {
        if (images.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * write a serialized list to a specific file.
     * 
     * @param target
     *            the targetDirectory.
     * @param list
     *            the list to be written.
     */
    public void writeList(File target, ArrayList<ImageValue> list) {
        if (list == null) {
            System.err.println("Liste existiert nicht!");
        } else {
            try {
                ObjectOutputStream outPtStr = new ObjectOutputStream(
                        new FileOutputStream(target));
                outPtStr.writeObject(list);
                outPtStr.flush();
                outPtStr.close();
            } catch (IOException ex) {
                System.err.println("Liste konnte nicht gespeichert werden!");
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public ArrayList<ImageValue> readList(File source) {
        ArrayList<ImageValue> list = new ArrayList<ImageValue>();
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(source));
            list = (ArrayList<ImageValue>) in.readObject();

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return list;

    }

	public void writeFile(String string, String dateiStringInfo) {
	  try {
	    FileWriter fw = new FileWriter(new File(string));
	    fw.write(dateiStringInfo);
	    System.out.println("Datei erfolgreich gespeichert unter: " + string);
    } catch (IOException e) {
	    System.err.println("Datei konnte nicht gespeichert werden. Hier noch einmal die Ausgabe: \n\n" + dateiStringInfo);
	    e.printStackTrace();
    }
		
    }

}
