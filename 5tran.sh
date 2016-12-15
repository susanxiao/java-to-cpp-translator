#!/bin/bash
# VARIABLES
start=0
end=50
packages="edu.nyu.oop"
testArgs="1 2 3 4"

#CLEANING OUTPUT FOLDERS
echo "Removing past outputs for $start to $end"
for ((i=start; i <= end; i++)); do
	formatNum=$(printf "%03d" $i)
	cppOutputPath="./testOutputs/translationOutputs/test$formatNum"
	test -d $cppOutputPath/output || mkdir $cppOutputPath/output
	rm $cppOutputPath/output/*
done
echo "Removed past outputs from testOutputs/translationOutputs"

#TRANSLATE
echo "Translating inputs $start to $end"
sbt --error 'set showSuccess := false' "run-main $packages.ImplementationUtil $start $end"
echo "Translated outputs are in testOutputs/translationOutputs"

#COPY JAVA_LANG CODE
echo "Copying java_lang.cpp and java_lang.h"
for ((i=start; i <= end; i++)); do
	formatNum=$(printf "%03d" $i)
	cppOutputPath="./testOutputs/translationOutputs/test$formatNum"
	cp ./output/* $cppOutputPath
done
echo "Copied java_lang files into translationOutputs directories"

#COMPILING JAVA CODE
echo "Compiling and outputting Java code for $start to $end"
for ((i=start; i <= end; i++)); do
	formatNum=$(printf "%03d" $i)
	javaInputPath="./src/test/java/inputs/test$formatNum"
	cppOutputPath="./testOutputs/translationOutputs/test$formatNum"
	test -d $cppOutputPath/bin || mkdir $cppOutputPath/bin
	test -d $cppOutputPath/output || mkdir $cppOutputPath/output
	sudo javac -d $cppOutputPath/bin $javaInputPath/Test$formatNum.java
	sudo chown -R $USER: $cppOutputPath/bin/inputs
	java -classpath $cppOutputPath/bin inputs/test$formatNum/Test$formatNum $testArgs > $cppOutputPath/output/java_output.txt 2>&1
done
echo "Java code outputted to java_output.txt"

#COMPILING C++ CODE
echo "Compiling and outputting C++ code for $start to $end"
for ((i=start; i <= end; i++)); do
	printf -v formatNum "%03d" $i
	cppOutputPath="./testOutputs/translationOutputs/test$formatNum"
	test -d $cppOutputPath/bin || mkdir $cppOutputPath/bin
	test -d $cppOutputPath/output || mkdir $cppOutputPath/output
	sudo g++ $cppOutputPath/*.cpp -o $cppOutputPath/bin/a.out
	sudo $cppOutputPath/bin/a.out $testArgs > $cppOutputPath/output/cpp_output.txt 2>&1
done
echo "C++ code outputted to cpp_output.txt"

#CHECKING OUTPUTS
echo "Checking outputs"
sbt --error 'set showSuccess := false' "run-main $packages.StdOutputChecking $start $end"
echo "Output comparisons are in testOutputs/input_tests.txt"
