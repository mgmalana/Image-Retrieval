package imageRetrievalMethods;

import model.Image;

import java.util.ArrayList;

/**
 * Created by mgmalana on 03/11/2016.
 */
public class CenteringCoherence extends ColorCoherence {
    //returns coherence vector. int[a][b]. a: 0 if coherent center, 1 if non coherent center, 2 coherent non center, 3 if non coherent center , b: color
    @Override
    protected int[][] getColorCoherenceVector(int[][] matrix, int[][] coherentMap, int labelCount) {
        int[] labelInstanceCounter = new int[labelCount];
        int[][] ccv = new int[NUM_COLOR_INDEX][4];
        int threshold = (int) (coherentMap.length * coherentMap[0].length * THRESHOLD_MULT);

        //counter number of instances of specific labels
        for (int[] row: coherentMap){
            for (int cell: row){
                labelInstanceCounter[cell]++;
            }
        }

        countCCVCenter(matrix, coherentMap, ccv,labelInstanceCounter, threshold);
        countCCVNonCenter(matrix, coherentMap, ccv,labelInstanceCounter, threshold);
        return ccv;
    }

    private void countCCVCenter(int[][] matrix, int[][] coherentMap, int[][] ccv, int[] labelInstanceCounter, int threshold) {
        //count ccv
        int widthNCenter = (int) (matrix.length * (1 - CenteringRefinement.CENTER_PERCENTAGE) / 2);
        int heightNCenter = (int) (matrix[0].length * (1 - CenteringRefinement.CENTER_PERCENTAGE) / 2);

        for (int i = widthNCenter; i < matrix.length - widthNCenter; i++){
            for (int j = heightNCenter; j < matrix[0].length - heightNCenter; j++){
                if(labelInstanceCounter[coherentMap[i][j]] >= threshold){
                    ccv[matrix[i][j]][0]++ ;
                } else {
                    ccv[matrix[i][j]][1]++ ;
                }
            }
        }
    }

    private void countCCVNonCenter(int[][] matrix, int[][] coherentMap, int[][] ccv, int[] labelInstanceCounter, int threshold) {
        int widthNCenter = (int) (matrix.length * (1 - CenteringRefinement.CENTER_PERCENTAGE) / 2);
        int heightNCenter = (int) (matrix[0].length * (1 - CenteringRefinement.CENTER_PERCENTAGE) / 2);

        //top part
        for (int i = 0; i < widthNCenter; i++){
            for (int j = 0; j < matrix[0].length; j++){
                if(labelInstanceCounter[coherentMap[i][j]] >= threshold){
                    ccv[matrix[i][j]][2]++ ;
                } else {
                    ccv[matrix[i][j]][3]++ ;
                }
            }
        }

        //bottom part
        for (int i = matrix.length - widthNCenter; i < matrix.length; i++){
            for (int j = 0; j < matrix[0].length; j++){
                if(labelInstanceCounter[coherentMap[i][j]] >= threshold){
                    ccv[matrix[i][j]][2]++ ;
                } else {
                    ccv[matrix[i][j]][3]++ ;
                }
            }
        }

        //left part excluding top part and bottom part
        for (int i = widthNCenter; i < matrix.length - widthNCenter; i++){
            for (int j = 0; j < heightNCenter; j++){
                if(labelInstanceCounter[coherentMap[i][j]] >= threshold){
                    ccv[matrix[i][j]][2]++ ;
                } else {
                    ccv[matrix[i][j]][3]++ ;
                }
            }
        }

        //right part excluding top part and bottom part
        for (int i = widthNCenter; i < matrix.length - widthNCenter; i++){
            for (int j = matrix[0].length - heightNCenter; j < matrix[0].length; j++){
                if(labelInstanceCounter[coherentMap[i][j]] >= threshold){
                    ccv[matrix[i][j]][2]++ ;
                } else {
                    ccv[matrix[i][j]][3]++ ;
                }
            }
        }
    }

}
