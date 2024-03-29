package model;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Created by Kate on 11/02/2016.
 */
public class PerceptualMatrix {

    private double[][] dmatrix;

    public double[][] readMatrix(){
        File file = new File("res/d-matrix.dat");
        dmatrix = new double[159][159];
        try {
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextDouble()) {
                double color = scanner.nextDouble();
                double numPairs = scanner.nextDouble() * 2;
                double[] temp = new double[(int) numPairs];
                for (int i = 0; i < numPairs; i++) {
                    temp[i] = scanner.nextDouble();
                }
                for (int i = 0; i < 159; i++) {
                    dmatrix[(int) color][i] = 0.0;
                }
                for (int i = 0; i < numPairs - 1; i+=2) {
                    dmatrix[(int) color][(int) temp[i]] = temp[i + 1];

                }
                dmatrix[(int)color][(int)color] = 1.0;
                }
            } catch (FileNotFoundException e){
                e.printStackTrace();}

        return dmatrix;
    }



}





