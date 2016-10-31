package imageRetrievalMethods;

import model.Image;
import model.LUV;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mgmalana on 30/10/2016.
 */
public class ColorHistogramMethod extends  ImageRetrievalMethod{
    public double THRESHOLD = 0.0005; //TODO: Adjust this. masyadong maliit ata.

    public double getSimilarity(Image query, Image toCompare){
        LUV[][] queryMatrix = query.getLUVMatrix();
        LUV[][] toCompareMatrix = toCompare.getLUVMatrix();
        LUV[] colorArrayList = getArrayColors(queryMatrix, toCompareMatrix);

        double[] queryNH = getNormalizedHistogram(colorArrayList, queryMatrix);
        double[] toCompareNH = getNormalizedHistogram(colorArrayList, toCompareMatrix);

        return getSimilarity(queryNH, toCompareNH);
    }

    private double getSimilarity(double[] queryNH, double[] toCompareNH) {
        double sim = 0;
        int thresholdCounter = 0;

        for(int i = 0; i < queryNH.length; i++){
            if(queryNH[i] > THRESHOLD) {
                sim += (1 - (queryNH[i] - toCompareNH[i]) /
                        ((queryNH[i] > toCompareNH[i]) ? queryNH[i] : toCompareNH[i])); //Max(queryNH[i], toCompareNH[i])
                thresholdCounter++;
            }
        }
        return (1.0/thresholdCounter) * sim;
    }

    private double[] getNormalizedHistogram(LUV[] colorArrayList, LUV[][] matrix) {
        double[] nhArray = new double[colorArrayList.length];
        HashMap <LUV, Integer> histogramMap = new HashMap<>(); //temporary container
        double countElement = matrix.length * matrix[0].length;

        //instantiate counter
        for (LUV luv: colorArrayList){
            histogramMap.put(luv, 0);
        }

        //traverses the matrix and increments the color counter
        for (LUV[] lArray: matrix) {
            for (LUV luv: lArray) {
                histogramMap.put(luv, histogramMap.get(luv) + 1);
            }
        }

        for (int i = 0; i < colorArrayList.length; i++){
            nhArray[i] = histogramMap.get(colorArrayList[i]) / countElement;
        }

        return nhArray;
    }

    //returns collection of the colors of both parameters.
    private LUV[] getArrayColors(LUV[][] queryMatrix, LUV[][] toCompareMatrix) {
        Set<LUV> colorArray = new HashSet<>();
        for (int i = 0; i < queryMatrix.length; i++) {
            for (int j = 0; j < queryMatrix[i].length; j++) {
                colorArray.add(queryMatrix[i][j]);
            }
        }

        for (int i = 0; i < toCompareMatrix.length; i++) {
            for (int j = 0; j < toCompareMatrix[i].length; j++) {
                colorArray.add(toCompareMatrix[i][j]);
            }
        }

        return colorArray.toArray(new LUV[colorArray.size()]);
    }
}
