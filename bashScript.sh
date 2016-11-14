#!/bin/bash
echo "";
echo "Compiling 0->13";
sudo g++ testOutputs/translationOutputs/test000/main.cpp testOutputs/translationOutputs/test000/java_lang.cpp -o testOutputs/translationOutputs/test000/a.out ;
sudo g++ testOutputs/translationOutputs/test001/main.cpp testOutputs/translationOutputs/test001/java_lang.cpp testOutputs/translationOutputs/test001/output.cpp -o testOutputs/translationOutputs/test001/a.out ;
sudo g++ testOutputs/translationOutputs/test002/main.cpp testOutputs/translationOutputs/test002/java_lang.cpp testOutputs/translationOutputs/test002/output.cpp -o testOutputs/translationOutputs/test002/a.out ;
sudo g++ testOutputs/translationOutputs/test003/main.cpp testOutputs/translationOutputs/test003/java_lang.cpp testOutputs/translationOutputs/test003/output.cpp -o testOutputs/translationOutputs/test003/a.out ;
sudo g++ testOutputs/translationOutputs/test004/main.cpp testOutputs/translationOutputs/test004/java_lang.cpp testOutputs/translationOutputs/test004/output.cpp -o testOutputs/translationOutputs/test004/a.out ;
sudo g++ testOutputs/translationOutputs/test005/main.cpp testOutputs/translationOutputs/test005/java_lang.cpp testOutputs/translationOutputs/test005/output.cpp -o testOutputs/translationOutputs/test005/a.out ;
sudo g++ testOutputs/translationOutputs/test006/main.cpp testOutputs/translationOutputs/test006/java_lang.cpp testOutputs/translationOutputs/test006/output.cpp -o testOutputs/translationOutputs/test006/a.out ;
sudo g++ testOutputs/translationOutputs/test007/main.cpp testOutputs/translationOutputs/test007/java_lang.cpp testOutputs/translationOutputs/test007/output.cpp -o testOutputs/translationOutputs/test007/a.out ;
sudo g++ testOutputs/translationOutputs/test008/main.cpp testOutputs/translationOutputs/test008/java_lang.cpp testOutputs/translationOutputs/test008/output.cpp -o testOutputs/translationOutputs/test008/a.out ;
sudo g++ testOutputs/translationOutputs/test009/main.cpp testOutputs/translationOutputs/test009/java_lang.cpp testOutputs/translationOutputs/test009/output.cpp -o testOutputs/translationOutputs/test009/a.out ;
sudo g++ testOutputs/translationOutputs/test010/main.cpp testOutputs/translationOutputs/test010/java_lang.cpp testOutputs/translationOutputs/test010/output.cpp -o testOutputs/translationOutputs/test010/a.out ;
sudo g++ testOutputs/translationOutputs/test011/main.cpp testOutputs/translationOutputs/test011/java_lang.cpp testOutputs/translationOutputs/test011/output.cpp -o testOutputs/translationOutputs/test011/a.out ;
sudo g++ testOutputs/translationOutputs/test012/main.cpp testOutputs/translationOutputs/test012/java_lang.cpp testOutputs/translationOutputs/test012/output.cpp -o testOutputs/translationOutputs/test012/a.out ;
sudo g++ testOutputs/translationOutputs/test013/main.cpp testOutputs/translationOutputs/test013/java_lang.cpp testOutputs/translationOutputs/test013/output.cpp -o testOutputs/translationOutputs/test013/a.out ;
echo "Compiled 0->13";

echo "";
echo "Directing output to std_output.txt";
sudo testOutputs/translationOutputs/test000/a.out > testOutputs/translationOutputs/test000/std_output.txt;
sudo testOutputs/translationOutputs/test001/a.out > testOutputs/translationOutputs/test001/std_output.txt;
sudo testOutputs/translationOutputs/test002/a.out > testOutputs/translationOutputs/test002/std_output.txt;
sudo testOutputs/translationOutputs/test003/a.out > testOutputs/translationOutputs/test003/std_output.txt;
sudo testOutputs/translationOutputs/test004/a.out > testOutputs/translationOutputs/test004/std_output.txt;
sudo testOutputs/translationOutputs/test005/a.out > testOutputs/translationOutputs/test005/std_output.txt;
sudo testOutputs/translationOutputs/test006/a.out > testOutputs/translationOutputs/test006/std_output.txt;
sudo testOutputs/translationOutputs/test007/a.out > testOutputs/translationOutputs/test007/std_output.txt;
sudo testOutputs/translationOutputs/test008/a.out > testOutputs/translationOutputs/test008/std_output.txt;
sudo testOutputs/translationOutputs/test009/a.out > testOutputs/translationOutputs/test009/std_output.txt;
sudo testOutputs/translationOutputs/test010/a.out > testOutputs/translationOutputs/test010/std_output.txt;
sudo testOutputs/translationOutputs/test011/a.out > testOutputs/translationOutputs/test011/std_output.txt;
sudo testOutputs/translationOutputs/test012/a.out > testOutputs/translationOutputs/test012/std_output.txt;
sudo testOutputs/translationOutputs/test013/a.out > testOutputs/translationOutputs/test013/std_output.txt;
echo "Directed output to std_output.txt";

echo "";
( cd src/test/java;
echo "Directing outputs of inputs to std_output_input.txt"
sudo javac inputs/test000/Test000.java; java inputs/test000/Test000 > inputs/test000/std_output_input.txt;
sudo javac inputs/test001/Test001.java; java inputs/test001/Test001 > inputs/test001/std_output_input.txt;
sudo javac inputs/test002/Test002.java; java inputs/test002/Test002 > inputs/test002/std_output_input.txt;
sudo javac inputs/test003/Test003.java; java inputs/test003/Test003 > inputs/test003/std_output_input.txt;
sudo javac inputs/test004/Test004.java; java inputs/test004/Test004 > inputs/test004/std_output_input.txt;
sudo javac inputs/test005/Test005.java; java inputs/test005/Test005 > inputs/test005/std_output_input.txt;
sudo javac inputs/test006/Test006.java; java inputs/test006/Test006 > inputs/test006/std_output_input.txt;
sudo javac inputs/test007/Test007.java; java inputs/test007/Test007 > inputs/test007/std_output_input.txt;
sudo javac inputs/test008/Test008.java; java inputs/test008/Test008 > inputs/test008/std_output_input.txt;
sudo javac inputs/test009/Test009.java; java inputs/test009/Test009 > inputs/test009/std_output_input.txt;
sudo javac inputs/test010/Test010.java; java inputs/test010/Test010 > inputs/test010/std_output_input.txt;
sudo javac inputs/test011/Test011.java; java inputs/test011/Test011 > inputs/test011/std_output_input.txt;
sudo javac inputs/test012/Test012.java; java inputs/test012/Test012 > inputs/test012/std_output_input.txt;
sudo javac inputs/test013/Test013.java; java inputs/test013/Test013 > inputs/test013/std_output_input.txt;
echo "Directed output to std_output_.txt";
);

echo "";










