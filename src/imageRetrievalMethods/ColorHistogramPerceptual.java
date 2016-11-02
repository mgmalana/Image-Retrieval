package imageRetrievalMethods;

import model.Image;
import model.PerceptualMatrix;

/**
 * Created by Kate on 11/02/2016.
 */
public class ColorHistogramPerceptual extends ImageRetrievalMethod {
    public double THRESHOLD = 0.005;

    public double getSimilarity(Image query, Image toCompare){
        PerceptualMatrix perceptualMatrix = new PerceptualMatrix();
        double[][] dMatrix = perceptualMatrix.readMatrix();
        double[] queryNH = getNormalizedHistogram(query.getLUVMatrix());
        double[] toCompareNH = getNormalizedHistogram(toCompare.getLUVMatrix());
        double[] simExactCol = getSimExactCol(queryNH, toCompareNH);
        double[] simPerCol = getSimPerCol(simExactCol, dMatrix);
        double[] simColorQII = getSimColorQII(simExactCol, simPerCol);
        double simColor = getSimColor(simColorQII, queryNH);

        return simColor;
    }



    private double[] getSimExactCol(double[] queryNH, double[] toCompareNH){ //SimExactCol(Q, I, i)
        double[] simExactCol = new double[queryNH.length];

        for(int i = 0; i < queryNH.length; i++){
            if(queryNH[i] > THRESHOLD) {
                simExactCol[i] = (1 - Math.abs(queryNH[i] - toCompareNH[i]) /
                        ((queryNH[i] > toCompareNH[i]) ? queryNH[i] : toCompareNH[i])); //Max(queryNH[i], toCompareNH[i]);
            }
        }

        return simExactCol;
    }

    private double[] getSimPerCol(double[] simExactCol, double[][] dMatrix){
        double[] simPerCol = new double[NUM_COLOR_INDEX];
        for(int i = 0; i<NUM_COLOR_INDEX; i++){
            for(int j = 0; j<NUM_COLOR_INDEX; j++){
                simPerCol[i] += simExactCol[i] * dMatrix[i][j];
            }
        }
        return simPerCol;
    }

    private double[] getSimColorQII(double[] simExactCol, double[] simPerCol){
        double[] simColorQII = new double[NUM_COLOR_INDEX];
        for(int i = 0; i<NUM_COLOR_INDEX; i++){
            simColorQII[i] = simExactCol[i] * (1 + simPerCol[i]);
        }
        return simColorQII;
    }

    private double getSimColor(double[] simColorQII, double[] queryNH){
        double simColor = 0;
        for(int i = 0; i<NUM_COLOR_INDEX; i++){
            simColor += (simColorQII[i] * queryNH[i]);
        }
        return simColor;
    }


    private int[] getHistogram(int[][] matrix){
        int[] histogramMap = new int[NUM_COLOR_INDEX];

        //traverses the matrix and increments the color counter
        for (int[] lArray: matrix) {
            for (int luv: lArray) {
                histogramMap[luv]++;
            }
        }

        return histogramMap;
    }

    private double[] getNormalizedHistogram(int[][] matrix) {
        double[] nhArray = new double[NUM_COLOR_INDEX];
        int[] histogramMap = getHistogram(matrix);

        double countElement = matrix.length * matrix[0].length;


        for (int i = 0; i < NUM_COLOR_INDEX; i++){
            nhArray[i] = histogramMap[i] / countElement;
        }

        return nhArray;
    }


}

