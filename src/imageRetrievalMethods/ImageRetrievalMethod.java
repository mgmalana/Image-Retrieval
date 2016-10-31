package imageRetrievalMethods;

import model.Image;

/**
 * Created by mgmalana on 31/10/2016.
 */
public abstract class ImageRetrievalMethod {
    public abstract double getSimilarity(Image query, Image toCompare);
}
