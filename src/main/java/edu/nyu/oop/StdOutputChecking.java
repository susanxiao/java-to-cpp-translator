package edu.nyu.oop;

import java.io.*;

import java.nio.file.Files;

import static java.lang.System.out;

/**
 * Created by Garrett on 11/13/16.
 */
public class StdOutputChecking {
    public static void main(String[] args) {
        for (int i = 0; i < 14; i++) {
            String path1 = String.format("./testOutputs/translationOutputs/test%03d/std_output.txt", i);
            String path2 = String.format("./src/test/java/inputs/test%03d/std_output_input.txt", i);
            String inputName = String.format("test%03d", i);

            File file1 = new File(path1);
            File file2 = new File(path2);
            FileInputStream fin_std_out = null;
            FileInputStream fin_std_out_input = null;
            try {
                fin_std_out = new FileInputStream(file1);
                fin_std_out_input = new FileInputStream(file2);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader myInput1 = new BufferedReader(new InputStreamReader(fin_std_out));
            BufferedReader myInput2 = new BufferedReader(new InputStreamReader(fin_std_out_input));
            StringBuilder std_output = new StringBuilder();
            StringBuilder std_output_input = new StringBuilder();
            try {
                String thisLine;
                while ((thisLine = myInput1.readLine()) != null) {
                    std_output.append(thisLine);
                }
                String thisLine2;
                while ((thisLine2 = myInput2.readLine()) != null) {
                    std_output_input.append(thisLine2);
                }
                String equal = std_output.toString().equals(std_output_input.toString()) ? "Passes" : "Fails";
                out.println(inputName.toUpperCase() + "->" + equal);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
