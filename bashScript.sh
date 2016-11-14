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
( cd src/test/java;  bash bashScript2.sh; );
echo "";










