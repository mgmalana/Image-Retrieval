package imageRetrievalMethods;

import model.Image;

/**
 * Created by mgmalana on 31/10/2016.
 * Used Manhattan distance for getting the distance between the histograms
 */
public class CenteringRefinement extends ImageRetrievalMethod{
    protected static final double CENTER_PERCENTAGE = .75;

    @Override
    public double getSimilarity(Image query, Image toCompare) {
        double distance = 0;
        int[] queryCenterBucket = getCenterCount(query.getLUVMatrix());
        int[] toCompareCenterBucket = getCenterCount(toCompare.getLUVMatrix());
        int[] queryNonCenterBucket = getNonCenterCount(query.getLUVMatrix());
        int[] toCompareNonCenterBucket = getNonCenterCount(toCompare.getLUVMatrix());

        for (int i = 0; i < NUM_COLOR_INDEX; i++){
            distance+= Math.abs(queryCenterBucket[i] - toCompareCenterBucket[i]);
            distance+= Math.abs(queryNonCenterBucket[i] - toCompareNonCenterBucket[i]);
        }

        return -distance; //because distance is inverse to similarity
    }

    private int[] getCenterCount(int[][] luvMatrix) {
        int[] colorHistogram = new int[NUM_COLOR_INDEX];
        int widthNCenter = (int) (luvMatrix.length * (1 - CENTER_PERCENTAGE) / 2);
        int heightNCenter = (int) (luvMatrix[0].length * (1 - CENTER_PERCENTAGE) / 2);

        for (int i = widthNCenter; i < luvMatrix.length - widthNCenter; i++){
            for (int j = heightNCenter; j < luvMatrix[0].length - heightNCenter; j++){
                colorHistogram[luvMatrix[i][j]]++;
            }
        }

        return colorHistogram;
    }

    private int[] getNonCenterCount(int[][] luvMatrix) {
        int[] colorHistogram = new int[NUM_COLOR_INDEX];
        int widthNCenter = (int) (luvMatrix.length * (1 - CENTER_PERCENTAGE) / 2);
        int heightNCenter = (int) (luvMatrix[0].length * (1 - CENTER_PERCENTAGE) / 2);

        //top part
        for (int i = 0; i < widthNCenter; i++){
            for (int j = 0; j < luvMatrix[0].length; j++){
                colorHistogram[luvMatrix[i][j]]++;
            }
        }

        //bottom part
        for (int i = luvMatrix.length - widthNCenter; i < luvMatrix.length; i++){
            for (int j = 0; j < luvMatrix[0].length; j++){
                colorHistogram[luvMatrix[i][j]]++;
            }
        }

        //left part excluding top part and bottom part
        for (int i = widthNCenter; i < luvMatrix.length - widthNCenter; i++){
            for (int j = 0; j < heightNCenter; j++){
                colorHistogram[luvMatrix[i][j]]++;
            }
        }

        //right part excluding top part and bottom part
        for (int i = widthNCenter; i < luvMatrix.length - widthNCenter; i++){
            for (int j = luvMatrix[0].length - heightNCenter; j < luvMatrix[0].length; j++){
                colorHistogram[luvMatrix[i][j]]++;
            }
        }

        return colorHistogram;
    }
}
