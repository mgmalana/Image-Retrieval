package model;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import imageReader.CieConvert;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by mgmalana on 30/10/2016.
 */
public class Image {
    private int[][] LUVMatrix; //[x][y]
    private File file;
    private double similarity; //similarity to the query

    public Image(File file) throws IOException, ImageFormatException {
        this.file = file;

        FileInputStream in = new FileInputStream(file);

        // decodes the JPEG data stream into a BufferedImage

        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
        BufferedImage bufferedImage = decoder.decodeAsBufferedImage();

        LUVMatrix = new int[bufferedImage.getWidth()][bufferedImage.getHeight()];

        //saves the luv values to the matrix
        for (int i = 0; i < LUVMatrix.length; i++){
            for (int j = 0; j < LUVMatrix[0].length; j++){
                ColorModel colorModel = bufferedImage.getColorModel();
                CieConvert colorCIE = new CieConvert();
                int rgbValue = bufferedImage.getRGB(i, j);

                colorCIE.setValues(colorModel.getRed(rgbValue)/255.0,
                        colorModel.getGreen(rgbValue)/255.0,
                        colorModel.getBlue(rgbValue)/255.0);

                LUVMatrix[i][j] =colorCIE.IndexOf();
            }
        }
    }

    public int[][] getLUVMatrix() {
        return LUVMatrix;
    }

    public File getFile() {
        return file;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        return file.equals(image.file);

    }

    @Override
    public int hashCode() {
        return file.hashCode();
    }
}
