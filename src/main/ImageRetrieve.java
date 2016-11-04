package main;

import com.sun.image.codec.jpeg.ImageFormatException;
import imageRetrievalMethods.*;
import model.Image;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by mgmalana on 31/10/2016.
 */
public class ImageRetrieve {
    public enum imageRetrieval {
        COLORHISTOGRAM("Color Histogram"),
        CENTERING ("Centering Refinement"),
        PERCEPTUAL ("Perceptual Similarity incorporated"),
        COHERENCE ("Color Coherence"),
        CENTERCOHERENCE("Centering Refinement and Color Coherence");

        private String name;
        imageRetrieval(String name){
            this.name = name;
        }

        // the toString just returns the given name
        @Override
        public String toString() {
            return name;
        }
    };

    public File[] getSimilarImages(Image queryImage, File imagesFolder, imageRetrieval colorhistogram, int numResults) {
        List<Image> sortedImages = new LinkedList<>();
        ImageRetrievalMethod method;
        File[] sortedFilesArray = new File[numResults];

        System.out.println("Method: " + colorhistogram);

        switch (colorhistogram) {
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
            case CENTERCOHERENCE:
                method = new CenteringCoherence();
                break;
            default:
                method = new ColorHistogramMethod(); //default method
        }

        File[] files = imagesFolder.listFiles();

        for (File directory : files) {
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

        for (int i = 0; i < numResults; i++) {
            sortedFilesArray[i] = sortedImages.get(i).getFile();
        }

        return sortedFilesArray;
    }
}