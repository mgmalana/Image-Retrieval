package main;

import model.Image;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.Random;

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
                bw.write("<img src=\"" + f.getPath() + "\"</img>" +
                        f.getName() +
                        "<hr>");
                //System.out.print(f.getName() +", ");
            }
            bw.write("</body>");
            bw.write("</html>");
            bw.close();

            Desktop.getDesktop().browse(file.toURI());
        } catch (IOException e){

        }

    }

    public static void main(String[] args) {
        Main main = new Main();
        boolean isTesting = false;

        if(isTesting) {
            try {
                main.testing();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File selectedFile = main.initFileChooser();

            try {

                if(selectedFile != null){
                    Image queryImage = new Image(selectedFile);
                    ImageRetrieve imageRetrieve = new ImageRetrieve();

                    File[] similarImages = imageRetrieve.getSimilarImages(queryImage, selectedFile.getParentFile(),
                            ImageRetrieve.imageRetrieval.CENTERCOHERENCE, 10);

    //                System.out.println("Top " + similarImages.length + " similar images are ");
    //                for (File file: similarImages){
    //                    System.out.print(file.getName() +", ");
    //                }
                    generateHTMLResults(selectedFile, similarImages); //generates and open the html file


                } else {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void testing() throws IOException {
        int[] filePerClass = {15 ,120, 202, 353, 471, 529, 657, 734, 859, 921};
        String directory = "images/";
        int numResults = 99;

        ImageRetrieve.imageRetrieval[] methods = ImageRetrieve.imageRetrieval.values();
        ImageRetrieve imageRetrieve = new ImageRetrieve();

        for(int fileIndex: filePerClass){ //per image
            for(ImageRetrieve.imageRetrieval method: methods){ //per method
                System.out.println("Query image: " + fileIndex + " method: " + method.toString());
                File file = new File(directory + fileIndex + ".jpg");
                Image queryImage = new Image(file);

                File[] results = imageRetrieve.getSimilarImages(queryImage, file.getParentFile(), method, numResults);

                writeToCSV(fileIndex, method.toString(), results);
                generateHTMLResults(file, results, method.toString());
            }
        }
    }

    private void writeToCSV(int fileIndex, String method, File[] results) throws FileNotFoundException {
        PrintWriter printer = new PrintWriter(new FileOutputStream(new File("results.csv"), true));

        printer.append(fileIndex+","+ method+",");

        for (File result: results){
            printer.append(result.getName() + ",");
        }

        printer.append("\n");

        printer.close();
    }

    public static void generateHTMLResults(File selectedFile, File [] similarImages, String method){
        try{
            File file = new File("results/" + selectedFile.getName()+"_" + method+".html");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("<html>");
            bw.write("<body>");
            bw.write("<h2>Query: <h2><br>");
            bw.write("<img src=\"../" + selectedFile.getPath() + "\"</img><br>");
            bw.write("<h2>Top " +  similarImages.length + "similar images found: </h2><br>");
            for (File f: similarImages){
                bw.write("<img src=\"../" + f.getPath() + "\"</img>" +
                        f.getName() +
                        "<hr>");
                //System.out.print(f.getName() +", ");
            }
            bw.write("</body>");
            bw.write("</html>");
            bw.close();
        } catch (IOException e){

        }

    }
}
