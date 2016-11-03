package imageRetrievalMethods;

import model.Image;

import java.util.ArrayList;

/**
 * Created by mgmalana on 01/11/2016.
 */
public class ColorCoherence extends ImageRetrievalMethod{
    public final static int NORTH = 0;
    public final static int WEST = 1;
    public final static int NORTHWEST = 2;

    protected final static double THRESHOLD_MULT = 0.05;

    @Override
    public double getSimilarity(Image query, Image toCompare) {
        int[][] queryCV = getCoherenceVector(query.getLUVMatrix());
        int[][] toCompareCV = getCoherenceVector(toCompare.getLUVMatrix());
        double distance = 0;

        for (int i = 0; i < queryCV.length; i++){
            for (int j = 0; j < queryCV[0].length; j++){
                distance+= Math.abs(queryCV[i][j] - toCompareCV[i][j]);
            }
        }

        return -distance; //because distance is inverse to similarity
    }

    //returns coherence vector. int[a][b]. a: 0 if coherent, 1 if non coherent, b: color
    protected int[][] getCoherenceVector(int[][] matrix) {
        ArrayList<Integer> parentChild = new ArrayList<>(); //index is the label. and the value is the parent

        int[][] coherentMap = getCoherentMapFirstPass(matrix, parentChild);
        updateCoherentMapSecondPass(coherentMap, parentChild);

        return getColorCoherenceVector(matrix, coherentMap, parentChild.size());
    }

    protected int[][] getCoherentMapFirstPass(int[][] matrix, ArrayList<Integer> parentChild) {
        int [][] coherentLabelMap = new int[matrix.length][matrix[0].length]; //0 = means no label
        int currentLabelCounter = 0;

        parentChild.add(currentLabelCounter);

        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[0].length; j++){
                //flags are used to know if pixel are of equal value to the specified position;
                boolean [] directionFlags = new boolean[3];
                int [] pixelLabels = new int[3];
                int currentLabel = currentLabelCounter;  //used for the label of cohorentLabelMap[i][j]

                if (i > 0 && matrix[i-1][j] == matrix[i][j]) { //check if north pixel is same value
                    directionFlags[NORTH] = true;
                    pixelLabels[NORTH] = coherentLabelMap[i-1][j];
                }
                if (j > 0 && matrix[i][j-1] == matrix[i][j]){ //check if west pixel is same value
                    directionFlags[WEST] = true;
                    pixelLabels[WEST] = coherentLabelMap[i][j-1];
                }
                if(i > 0 && j > 0 && matrix[i - 1][j - 1] == matrix[i][j]){ //check if north west pixel is same value
                    directionFlags[NORTHWEST] = true;
                    pixelLabels[NORTHWEST] = coherentLabelMap[i-1][j-1];
                }

                for(int k = 0; k < directionFlags.length; k++){
                    if(directionFlags[k]){
                        currentLabel = Math.min(currentLabel, pixelLabels[k]);
                    }
                }

                for(int k = 0; k < directionFlags.length; k++) {
                     if(currentLabel < pixelLabels[k]){
                         parentChild.set(pixelLabels[k], currentLabel);
                     }
                }

                if(currentLabel == currentLabelCounter){
                    currentLabelCounter++;
                    parentChild.add(currentLabelCounter);
                }

                coherentLabelMap[i][j] = currentLabel;
            }
        }

        return coherentLabelMap;
    }

    //merge some of the labels depending on the parentChild relationship
    protected void updateCoherentMapSecondPass(int[][] coherentLabelMap, ArrayList<Integer> parentChild) {
        //change the parentChild so that child directly points to the main parent
        for (int i = 0; i < parentChild.size(); i++){
            int parent = parentChild.get(i);

            if(parent != i && parentChild.get(parent) != parent){
                parentChild.set(i, parentChild.get(parent));
            }
        }

        for (int i = 0; i <  coherentLabelMap.length; i++){
            for (int j = 0; j < coherentLabelMap[0].length; j++){
                if(parentChild.get(coherentLabelMap[i][j]) != coherentLabelMap[i][j]){
                    coherentLabelMap[i][j] = parentChild.get(coherentLabelMap[i][j]);
                }
            }
        }
    }

    protected int[][] getColorCoherenceVector(int[][] matrix, int[][] coherentMap, int labelCount) {
        int[] labelInstanceCounter = new int[labelCount];
        int[][] ccv = new int[NUM_COLOR_INDEX][2];
        int threshold = (int) (coherentMap.length * coherentMap[0].length * THRESHOLD_MULT);

        //counter number of instances of specific labels
        for (int[] row: coherentMap){
            for (int cell: row){
                labelInstanceCounter[cell]++;
            }
        }

        //count ccv
        for (int i = 0; i < coherentMap.length; i++){
            for (int j = 0; j < coherentMap[0].length; j++){
                if(labelInstanceCounter[coherentMap[i][j]] >= threshold){
                    ccv[matrix[i][j]][0]++ ;
                } else {
                    ccv[matrix[i][j]][1]++ ;
                }
            }
        }

        return ccv;
    }

    private void displayMap(int[][] coherentMap) {
        System.out.println();

        for (int[] row: coherentMap){
            for (int cell: row) {
                System.out.print(cell + ", ");
            }
            System.out.println();
        }
    }


}
