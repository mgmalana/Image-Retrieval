package main;

import model.Image;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

    public static void generateHTMLResults(File selectedFile, File [] similarImages){
        try{
            File file = new File("Results.html");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("<html>");
            bw.write("<body>");
            bw.write("<h2>Query: <h2><br>");
            bw.write("<img src=\"" + selectedFile.getPath() + "\"</img><br>");
            bw.write("<h2>Top " +  similarImages.length + "similar images found: </h2><br>");
            for (File f: similarImages){
                bw.write("<img src=\"" + f.getPath() + "\"</img><hr>");
                //System.out.print(f.getName() +", ");
            }
            bw.write("</body>");
            bw.write("</html>");
            bw.close();
        } catch (IOException e){

        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        //File selectedFile = main.initFileChooser();

        File selectedFile = new File("/Users/Kate/IdeaProjects/Image-Retrieval/images/0.jpg");

        try {

            if(selectedFile != null){
                Image queryImage = new Image(selectedFile);
                ImageRetrieve imageRetrieve = new ImageRetrieve();

                File[] similarImages = imageRetrieve.getSimilarImages(queryImage, selectedFile.getParentFile(),
                        ImageRetrieve.imageRetrieval.COHERENCE);

//                System.out.println("Top " + similarImages.length + " similar images are ");
//                for (File file: similarImages){
//                    System.out.print(file.getName() +", ");
//                }
                generateHTMLResults(selectedFile, similarImages);

            } else {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
