package imageRetrievalMethods;

import com.sun.deploy.util.ArrayUtil;
import com.sun.tools.javac.util.ArrayUtils;
import model.Image;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mgmalana on 30/10/2016.
 */
public class ColorHistogramMethod extends  ImageRetrievalMethod{
    public double THRESHOLD = 0.005;
    public static int NUM_COLOR_INDEX = 159;

    public double getSimilarity(Image query, Image toCompare){
        double[] queryNH = getNormalizedHistogram(query.getLUVMatrix());
        double[] toCompareNH = getNormalizedHistogram(toCompare.getLUVMatrix());
        return getSimilarity(queryNH, toCompareNH);
    }

    private double getSimilarity(double[] queryNH, double[] toCompareNH) {
        double sim = 0;
        int thresholdCounter = 0;

        for(int i = 0; i < queryNH.length; i++){
            if(queryNH[i] > THRESHOLD) {
                sim += (1 - Math.abs(queryNH[i] - toCompareNH[i]) /
                        ((queryNH[i] > toCompareNH[i]) ? queryNH[i] : toCompareNH[i])); //Max(queryNH[i], toCompareNH[i]);

                thresholdCounter++;
            }
        }
        return (1.0/thresholdCounter) * sim;
    }

    private double[] getNormalizedHistogram(int[][] matrix) {
        double[] nhArray = new double[NUM_COLOR_INDEX];
        int[] histogramMap = new int[NUM_COLOR_INDEX];
        double countElement = matrix.length * matrix[0].length;

        //traverses the matrix and increments the color counter
        for (int[] lArray: matrix) {
            for (int luv: lArray) {
                histogramMap[luv]++;
            }
        }

        for (int i = 0; i < NUM_COLOR_INDEX; i++){
            nhArray[i] = histogramMap[i] / countElement;
        }

        return nhArray;
    }
}
