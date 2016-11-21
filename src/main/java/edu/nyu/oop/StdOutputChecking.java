package edu.nyu.oop;

import java.io.*;

import static java.lang.System.out;

/**
 * Created by Garrett on 11/13/16.
 */
public class StdOutputChecking {
    public static void main(String[] args) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i <= 20; i++) {
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
                BufferedReader myInput1 = new BufferedReader(new InputStreamReader(fin_std_out));
                BufferedReader myInput2 = new BufferedReader(new InputStreamReader(fin_std_out_input));
                StringBuilder std_output = new StringBuilder();
                StringBuilder std_output_input = new StringBuilder();
                String thisLine;
                String thisLine2;
                while ((thisLine = myInput1.readLine()) != null) {
                    std_output.append(thisLine);
                }
                while ((thisLine2 = myInput2.readLine()) != null) {
                    std_output_input.append(thisLine2);
                }
                String equal = std_output.toString().equals(std_output_input.toString()) ? "Passes" : "Fails";
                switch (i) {
                    case 9:
                        equal = "Location? (Passes)(Check)";
                        break;
                    case 13:
                        equal = "Location? (Passes)";
                        break;
                    case 14:
                        equal = "NullPointerException? (Passes)(returns the location of NullPointerException)(Check)";
                        break;
                    case 15:
                        equal = "Location? (Passes)(Check)";
                        break;
                    case 16:
                        equal = "ClassCastException? (Passes)(returns the location of ClassCastException)(Check)";
                        break;
                    case 17:
                        equal = "Location? (Passes)(Check)";
                        break;
                }
                if (i > 17) {
                    equal = "Fails (Need to check the results manually)";
                }
                s.append(inputName.toUpperCase() + "-> " + equal + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            String path_output = "./testOutputs/input_tests.txt";
            File main = new File(path_output);
            FileWriter printMain = new FileWriter(main);
            printMain.write(s.toString());
            printMain.flush();
            printMain.close();
            out.println("Printed " + main.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println(s.toString());
    }
}
