#!/bin/bash
echo "Compiling 0->13"
sudo g++ test001/main.cpp test001/java_lang.cpp test001/output.cpp -o test001/a.out ;
sudo g++ test002/main.cpp test002/java_lang.cpp test002/output.cpp -o test002/a.out ;
sudo g++ test003/main.cpp test003/java_lang.cpp test003/output.cpp -o test003/a.out ;
sudo g++ test004/main.cpp test004/java_lang.cpp test004/output.cpp -o test004/a.out ;
sudo g++ test005/main.cpp test005/java_lang.cpp test005/output.cpp -o test005/a.out ;
sudo g++ test006/main.cpp test006/java_lang.cpp test006/output.cpp -o test006/a.out ;
sudo g++ test007/main.cpp test007/java_lang.cpp test007/output.cpp -o test007/a.out ;
sudo g++ test008/main.cpp test008/java_lang.cpp test008/output.cpp -o test008/a.out ;
sudo g++ test009/main.cpp test009/java_lang.cpp test009/output.cpp -o test009/a.out ;
sudo g++ test010/main.cpp test010/java_lang.cpp test010/output.cpp -o test010/a.out ;
sudo g++ test011/main.cpp test011/java_lang.cpp test011/output.cpp -o test011/a.out ;
sudo g++ test012/main.cpp test012/java_lang.cpp test012/output.cpp -o test012/a.out ;
sudo g++ test013/main.cpp test013/java_lang.cpp test013/output.cpp -o test013/a.out ;
echo "Compiled 0->13"

echo "Directing output to std_output_.txt"
sudo test000/a.out > test000/std_output.txt;
sudo test001/a.out > test001/std_output.txt;
sudo test002/a.out > test002/std_output.txt;
sudo test003/a.out > test003/std_output.txt;
sudo test004/a.out > test004/std_output.txt;
sudo test005/a.out > test005/std_output.txt;
sudo test006/a.out > test006/std_output.txt;
sudo test007/a.out > test007/std_output.txt;
sudo test008/a.out > test008/std_output.txt;
sudo test009/a.out > test009/std_output.txt;
sudo test010/a.out > test010/std_output.txt;
sudo test011/a.out > test011/std_output.txt;
sudo test012/a.out > test012/std_output.txt;
sudo test013/a.out > test013/std_output.txt;
echo "Directed output to std_output_.txt"

echo "Directing outputs of inputs to std_output_input.txt"

echo "Directed output to std_output_.txt"

echo "Directing outputs of inputs to std_output_input.txt"




