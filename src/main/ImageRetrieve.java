package main;

import com.sun.image.codec.jpeg.ImageFormatException;
import imageRetrievalMethods.CenteringRefinement;
import imageRetrievalMethods.ColorCoherence;
import imageRetrievalMethods.ColorHistogramMethod;
import imageRetrievalMethods.ColorHistogramPerceptual;
import imageRetrievalMethods.ImageRetrievalMethod;
import model.Image;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by mgmalana on 31/10/2016.
 */
public class ImageRetrieve {
    public enum imageRetrieval {COLORHISTOGRAM, CENTERING, PERCEPTUAL, COHERENCE};
    public final static int NUM_RESULTS = 10;

    public File[] getSimilarImages(Image queryImage, File imagesFolder, imageRetrieval colorhistogram) {
        List<Image> sortedImages = new LinkedList<>();
        ImageRetrievalMethod method;
        File[] sortedFilesArray = new File[NUM_RESULTS];

        switch (colorhistogram){
            case COLORHISTOGRAM:
                method = new ColorHistogramMethod();
                break;
            case CENTERING:
                method = new CenteringRefinement();
                break;
            case COHERENCE:
                method = new ColorCoherence();
                break;
            case PERCEPTUAL:
                method = new ColorHistogramPerceptual();
                break;
            default:
                method = new ColorHistogramMethod(); //default method
        }

        File[] files = imagesFolder.listFiles();

        for(File directory: files){
            try {
                Image toCompare = new Image(directory);

                double sim = method.getSimilarity(queryImage, toCompare);
                System.out.println("Similarity of " + queryImage.getFile().getName() + " and " + toCompare.getFile().getName()
                        + " is " + sim);
                toCompare.setSimilarity(sim);
                sortedImages.add(toCompare);
            } catch (ImageFormatException e) {
                System.err.println(directory + " is not a JPEG file");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        sortedImages.sort(new Comparator<Image>() {
            @Override
            public int compare(Image o1, Image o2) { //comparator reversed
                if (o1.getSimilarity() < o2.getSimilarity())
                    return 1;
                else if (o1.getSimilarity() > o2.getSimilarity())
                    return -1;
                else
                    return 0;
            }
        });

        sortedImages.remove(queryImage);

        for (int i = 0; i < NUM_RESULTS; i++){
            sortedFilesArray[i] = sortedImages.get(i).getFile();
        }

        ///////TODO: Remove query image from the sortedImages
        return sortedFilesArray;
    }
}
