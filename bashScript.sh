#!/bin/bash
echo "";
echo "Compiling 0->20";
sudo g++ testOutputs/translationOutputs/test000/main.cpp testOutputs/translationOutputs/test000/java_lang.cpp -o testOutputs/translationOutputs/test000/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test001/main.cpp testOutputs/translationOutputs/test001/java_lang.cpp testOutputs/translationOutputs/test001/output.cpp -o testOutputs/translationOutputs/test001/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test002/main.cpp testOutputs/translationOutputs/test002/java_lang.cpp testOutputs/translationOutputs/test002/output.cpp -o testOutputs/translationOutputs/test002/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test003/main.cpp testOutputs/translationOutputs/test003/java_lang.cpp testOutputs/translationOutputs/test003/output.cpp -o testOutputs/translationOutputs/test003/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test004/main.cpp testOutputs/translationOutputs/test004/java_lang.cpp testOutputs/translationOutputs/test004/output.cpp -o testOutputs/translationOutputs/test004/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test005/main.cpp testOutputs/translationOutputs/test005/java_lang.cpp testOutputs/translationOutputs/test005/output.cpp -o testOutputs/translationOutputs/test005/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test006/main.cpp testOutputs/translationOutputs/test006/java_lang.cpp testOutputs/translationOutputs/test006/output.cpp -o testOutputs/translationOutputs/test006/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test007/main.cpp testOutputs/translationOutputs/test007/java_lang.cpp testOutputs/translationOutputs/test007/output.cpp -o testOutputs/translationOutputs/test007/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test008/main.cpp testOutputs/translationOutputs/test008/java_lang.cpp testOutputs/translationOutputs/test008/output.cpp -o testOutputs/translationOutputs/test008/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test009/main.cpp testOutputs/translationOutputs/test009/java_lang.cpp testOutputs/translationOutputs/test009/output.cpp -o testOutputs/translationOutputs/test009/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test010/main.cpp testOutputs/translationOutputs/test010/java_lang.cpp testOutputs/translationOutputs/test010/output.cpp -o testOutputs/translationOutputs/test010/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test011/main.cpp testOutputs/translationOutputs/test011/java_lang.cpp testOutputs/translationOutputs/test011/output.cpp -o testOutputs/translationOutputs/test011/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test012/main.cpp testOutputs/translationOutputs/test012/java_lang.cpp testOutputs/translationOutputs/test012/output.cpp -o testOutputs/translationOutputs/test012/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test013/main.cpp testOutputs/translationOutputs/test013/java_lang.cpp testOutputs/translationOutputs/test013/output.cpp -o testOutputs/translationOutputs/test013/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test014/main.cpp testOutputs/translationOutputs/test014/java_lang.cpp testOutputs/translationOutputs/test014/output.cpp -o testOutputs/translationOutputs/test014/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test015/main.cpp testOutputs/translationOutputs/test015/java_lang.cpp testOutputs/translationOutputs/test015/output.cpp -o testOutputs/translationOutputs/test015/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test016/main.cpp testOutputs/translationOutputs/test016/java_lang.cpp testOutputs/translationOutputs/test016/output.cpp -o testOutputs/translationOutputs/test016/bin/a.out ;
sudo g++ testOutputs/translationOutputs/test017/main.cpp testOutputs/translationOutputs/test017/java_lang.cpp testOutputs/translationOutputs/test017/output.cpp -o testOutputs/translationOutputs/test017/bin/a.out ;
# sudo g++ testOutputs/translationOutputs/test018/main.cpp testOutputs/translationOutputs/test018/java_lang.cpp testOutputs/translationOutputs/test018/output.cpp -o testOutputs/translationOutputs/test018/bin/a.out ;
# sudo g++ testOutputs/translationOutputs/test019/main.cpp testOutputs/translationOutputs/test019/java_lang.cpp testOutputs/translationOutputs/test019/output.cpp -o testOutputs/translationOutputs/test019/bin/a.out ;
# sudo g++ testOutputs/translationOutputs/test020/main.cpp testOutputs/translationOutputs/test020/java_lang.cpp testOutputs/translationOutputs/test020/output.cpp -o testOutputs/translationOutputs/test020/bin/a.out ;
echo "Compiled 0->20";

echo "";
echo "Directing output to output/cpp_output.txt";
sudo testOutputs/translationOutputs/test000/bin/a.out > testOutputs/translationOutputs/test000/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test001/bin/a.out > testOutputs/translationOutputs/test001/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test002/bin/a.out > testOutputs/translationOutputs/test002/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test003/bin/a.out > testOutputs/translationOutputs/test003/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test004/bin/a.out > testOutputs/translationOutputs/test004/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test005/bin/a.out > testOutputs/translationOutputs/test005/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test006/bin/a.out > testOutputs/translationOutputs/test006/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test007/bin/a.out > testOutputs/translationOutputs/test007/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test008/bin/a.out > testOutputs/translationOutputs/test008/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test009/bin/a.out > testOutputs/translationOutputs/test009/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test010/bin/a.out > testOutputs/translationOutputs/test010/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test011/bin/a.out > testOutputs/translationOutputs/test011/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test012/bin/a.out > testOutputs/translationOutputs/test012/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test013/bin/a.out > testOutputs/translationOutputs/test013/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test014/bin/a.out > testOutputs/translationOutputs/test014/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test015/bin/a.out > testOutputs/translationOutputs/test015/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test016/bin/a.out > testOutputs/translationOutputs/test016/output/cpp_output.txt;
sudo testOutputs/translationOutputs/test017/bin/a.out > testOutputs/translationOutputs/test017/output/cpp_output.txt;
# sudo testOutputs/translationOutputs/test018/bin/a.out > testOutputs/translationOutputs/test018/output/cpp_output.txt;
# sudo testOutputs/translationOutputs/test019/bin/a.out > testOutputs/translationOutputs/test019/output/cpp_output.txt;
# sudo testOutputs/translationOutputs/test020/bin/a.out > testOutputs/translationOutputs/test020/output/cpp_output.txt;

echo "Directed output to output/cpp_output.txt";

echo "";
(
echo "Directing outputs of inputs to output/java_output.txt 2>&1"
sudo javac -d ./testOutputs/translationOutputs/test000/bin ./src/test/java/inputs/test000/Test000.java;
java -classpath ./testOutputs/translationOutputs/test000/bin inputs/test000/Test000 > ./testOutputs/translationOutputs/test000/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test001/bin ./src/test/java/inputs/test001/Test001.java;
java -classpath ./testOutputs/translationOutputs/test001/bin inputs/test001/Test001 > ./testOutputs/translationOutputs/test001/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test002/bin ./src/test/java/inputs/test002/Test002.java;
java -classpath ./testOutputs/translationOutputs/test002/bin inputs/test002/Test002 > ./testOutputs/translationOutputs/test002/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test003/bin ./src/test/java/inputs/test003/Test003.java;
java -classpath ./testOutputs/translationOutputs/test003/bin inputs/test003/Test003 > ./testOutputs/translationOutputs/test003/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test004/bin ./src/test/java/inputs/test004/Test004.java;
java -classpath ./testOutputs/translationOutputs/test004/bin inputs/test004/Test004 > ./testOutputs/translationOutputs/test004/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test005/bin ./src/test/java/inputs/test005/Test005.java;
java -classpath ./testOutputs/translationOutputs/test005/bin inputs/test005/Test005 > ./testOutputs/translationOutputs/test005/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test006/bin ./src/test/java/inputs/test006/Test006.java;
java -classpath ./testOutputs/translationOutputs/test006/bin inputs/test006/Test006 > ./testOutputs/translationOutputs/test006/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test007/bin ./src/test/java/inputs/test007/Test007.java;
java -classpath ./testOutputs/translationOutputs/test007/bin inputs/test007/Test007 > ./testOutputs/translationOutputs/test007/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test008/bin ./src/test/java/inputs/test008/Test008.java;
java -classpath ./testOutputs/translationOutputs/test008/bin inputs/test008/Test008 > ./testOutputs/translationOutputs/test008/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test009/bin ./src/test/java/inputs/test009/Test009.java;
java -classpath ./testOutputs/translationOutputs/test009/bin inputs/test009/Test009 > ./testOutputs/translationOutputs/test009/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test010/bin ./src/test/java/inputs/test010/Test010.java;
java -classpath ./testOutputs/translationOutputs/test010/bin inputs/test010/Test010 > ./testOutputs/translationOutputs/test010/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test011/bin ./src/test/java/inputs/test011/Test011.java;
java -classpath ./testOutputs/translationOutputs/test011/bin inputs/test011/Test011 > ./testOutputs/translationOutputs/test011/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test012/bin ./src/test/java/inputs/test012/Test012.java;
java -classpath ./testOutputs/translationOutputs/test012/bin inputs/test012/Test012 > ./testOutputs/translationOutputs/test012/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test013/bin ./src/test/java/inputs/test013/Test013.java;
java -classpath ./testOutputs/translationOutputs/test013/bin inputs/test013/Test013 > ./testOutputs/translationOutputs/test013/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test014/bin ./src/test/java/inputs/test014/Test014.java;
java -classpath ./testOutputs/translationOutputs/test014/bin inputs/test014/Test014 > ./testOutputs/translationOutputs/test014/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test015/bin ./src/test/java/inputs/test015/Test015.java;
java -classpath ./testOutputs/translationOutputs/test015/bin inputs/test015/Test015 > ./testOutputs/translationOutputs/test015/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test0016/bin ./src/test/java/inputs/test016/Test016.java;
java -classpath ./testOutputs/translationOutputs/test016/bin inputs/test016/Test016 > ./testOutputs/translationOutputs/test016/output/java_output.txt 2>&1;
sudo javac -d ./testOutputs/translationOutputs/test017/bin ./src/test/java/inputs/test017/Test017.java;
java -classpath ./testOutputs/translationOutputs/test017/bin inputs/test017/Test017 > ./testOutputs/translationOutputs/test017/output/java_output.txt 2>&1;
# sudo javac -d ./testOutputs/translationOutputs/test018/bin ./src/test/java/inputs/test018/Test018.java;
#java -classpath ./testOutputs/translationOutputs/test018/bin inputs/test018/Test018 > ./testOutputs/translationOutputs/test018/output/java_output.txt 2>&1;
# sudo javac -d ./testOutputs/translationOutputs/test019/bin ./src/test/java/inputs/test019/Test019.java;
#java -classpath ./testOutputs/translationOutputs/test019/bin inputs/test019/Test019 > ./testOutputs/translationOutputs/test019/output/java_output.txt 2>&1;
# sudo javac -d ./testOutputs/translationOutputs/test020/bin ./src/test/java/inputs/test020/Test020.java;
#java -classpath ./testOutputs/translationOutputs/test020/bin inputs/test020/Test020 > ./testOutputs/translationOutputs/test020/output/java_output.txt 2>&1;
echo "Directed output to output/java_output.txt";
);

echo "";










