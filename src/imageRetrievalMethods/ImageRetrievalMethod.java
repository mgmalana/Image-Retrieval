package imageRetrievalMethods;

import model.Image;

/**
 * Created by mgmalana on 31/10/2016.
 */
public abstract class ImageRetrievalMethod {
    public static int NUM_COLOR_INDEX = 159;

    public abstract double getDistance(Image query, Image toCompare);
}
