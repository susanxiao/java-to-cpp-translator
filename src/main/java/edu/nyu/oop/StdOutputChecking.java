package edu.nyu.oop;

import java.io.*;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * Created by Garrett on 11/13/16.
 */
public class StdOutputChecking {
    public static void main(String[] args) {
        int start = 0;
        int end = 20;

        if (args.length > 1) {
            start = ImplementationUtil.getInteger(args[0]);
            end = ImplementationUtil.getInteger(args[1]);
        }
        else if (args.length > 0) {
            int value = ImplementationUtil.getInteger(args[0]);
            if (value >= 0) {
                start = value;
                end = value;
            }
        }

        StringBuilder s = new StringBuilder();
        StringBuilder fail = new StringBuilder("\nFailed:\n");
        for (int i = start; i <= end; i++) {
            String cppOutput = String.format("./testOutputs/translationOutputs/test%03d/output/cpp_output.txt", i);
            String javaOutput = String.format("./testOutputs/translationOutputs/test%03d/output/java_output.txt", i);
            String inputName = String.format("test%03d", i);
            File cppFile = new File(cppOutput);
            File javaFile = new File(javaOutput);
            try {
                Scanner cppInput = new Scanner(cppFile);
                Scanner javaInput = new Scanner(javaFile);
                boolean isEqual = true;
                String message = "";
                while (cppInput.hasNext() && javaInput.hasNext()) {
                    String cppInputLine = cppInput.nextLine();
                    String javaInputLine = javaInput.nextLine();

                    if (!cppInputLine.equals(javaInputLine)) {
                        if (javaInputLine.startsWith("inputs.test")) {

                            String[] cpp = cppInputLine.split("@");
                            String[] java = javaInputLine.split("@");

                            if (!cpp[0].equals(java[0]))
                                isEqual = false;

                            message = " - Location";
                        }
                        else if (javaInputLine.startsWith("Exception")) {
                            if (!javaInputLine.contains(cppInputLine))
                                isEqual = false;
                            else {
                                while (javaInputLine.startsWith("\tat") && javaInput.hasNext())
                                    javaInputLine = javaInput.nextLine();
                            }

                            message = " - Exception";
                        }
                        else
                            isEqual = false;
                    }
                    if (!isEqual) break;
                }

                if (cppInput.hasNext()) //not both null
                    isEqual = false;

                if (javaInput.hasNext()) {
                    isEqual = false;
                    String javaInputLine = javaInput.nextLine();
                    if(javaInputLine.startsWith("Exception"))
                        message = " - Exception";
                    else if (javaInputLine.startsWith("inputs.test"))
                        message = " - Location";
                }

                cppInput.close();
                javaInput.close();

                String equal = (isEqual ? "Passed" : "Failed") + message;
                if (!isEqual)
                    fail.append(inputName.toLowerCase() + " -> " + equal + "\n");

                s.append(inputName.toLowerCase() + " -> " + equal + "\n");

            } catch (IOException e) {
                s.append(inputName.toLowerCase() + " -> Failed - Not Started\n");
                fail.append(inputName.toLowerCase() + " -> Failed - Not Started\n");
                //e.printStackTrace();
            }
        }
        s.append(fail);
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
