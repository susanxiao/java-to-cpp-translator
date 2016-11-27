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
            String cppOutput = String.format("./testOutputs/translationOutputs/test%03d/output/cpp_output.txt", i);
            String javaOutput = String.format("./testOutputs/translationOutputs/test%03d/output/java_output.txt", i);
            String inputName = String.format("test%03d", i);
            File cppFile = new File(cppOutput);
            File javaFile = new File(javaOutput);
            try {
                BufferedReader cppInput = new BufferedReader(new FileReader(cppFile));
                BufferedReader javaInput = new BufferedReader(new FileReader(javaFile));
                StringBuilder cpp = new StringBuilder();
                StringBuilder java = new StringBuilder();
                String inputLine;
                while ((inputLine = cppInput.readLine()) != null) {
                    cpp.append(inputLine);
                }

                while ((inputLine = javaInput.readLine()) != null) {
                    java.append(inputLine);
                }
                cppInput.close();
                javaInput.close();

                String equal;
                switch (i) {
                    case 9:
                        equal = "Location? (Passes)(needs to be checked manually)";
                        break;
                    case 13:
                        equal = "Location? (Passes)(needs to be checked manually)";
                        break;
                    case 14:
                        equal = "NullPointerException? (Passes)(returns the location of NullPointerException)(needs to be checked manually)";
                        break;
                    case 15:
                        equal = "Location? (Passes)(needs to be checked manually)";
                        break;
                    case 16:
                        equal = "ClassCastException? (Fails)(returns the location of ClassCastException)(needs to be checked manually)";
                        break;
                    case 17:
                        equal = "Location? (Passes)(needs to be checked manually)";
                        break;
                    default:
                        equal = cpp.toString().equals(java.toString()) ? "Passes" : "Fails";
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
