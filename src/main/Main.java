package main;

import model.Image;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public class Main {

    public File initFileChooser(){
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files","jpg", "jpeg");
        chooser.setFileFilter(filter);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
        return chooser.getSelectedFile();
    }

    public static void main(String[] args) {
        Main main = new Main();
        //File selectedFile = main.initFileChooser();

        File selectedFile = new File("/Users/mgmalana/Documents/Image-Retrieval/images/0.jpg");

        try {

            if(selectedFile != null){
                Image queryImage = new Image(selectedFile);
                ImageRetrieve imageRetrieve = new ImageRetrieve();

                File[] similarImages = imageRetrieve.getSimilarImages(queryImage, selectedFile.getParentFile(),
                        ImageRetrieve.imageRetrieval.CENTERING);

                System.out.println("Top " + similarImages.length + " similar images are ");
                for (File file: similarImages){
                    System.out.print(file.getName() +", ");
                }

            } else {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
