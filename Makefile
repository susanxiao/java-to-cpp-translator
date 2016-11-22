# VARIABLES
start = 0
end = 20
packages=edu.nyu.oop.

# TARGETS
ast astCreate :
	@echo "Creating AST for $(start) to $(end)" ;
	#TODO: construct java AST main method so we can circumvent calling test-only ;
	@echo "Java AST outputs are in testOutputs/astOutputs"
mutate astMutate :
	@echo "Mutating AST for $(start) to $(end)" ;
	#TODO: also construct this main method ;
	@echo "Mutated ASTs are in testOutputs/mutatedAstOutputs"
header printHeader :
	@echo "Printing output.h file for $(start) to $(end)" ;
	@sbt "run-main $(packages)PrintHeaderFile $(start) $(end)" ;
	@echo "Translated outputs are in testOutputs/printHeaderOutputs"
cpp printCpp :
	@echo "Printing output.cpp file for $(start) to $(end)" ;
	@sbt "run-main $(packages)PrintCppFile $(start) $(end)" ;
	@echo "Translated outputs are in testOutputs/printCppOutputs"
main printMain :
	@echo "Printing main.cpp file for $(start) to $(end)" ;
	@sbt "run-main $(packages)PrintMainFile $(start) $(end)" ;
	@echo "main.cpp outputs are in testOutputs/mainFileOutputs"
translate :
	@echo "Translating inputs $(start) to $(end)" ;
	@sbt "run-main $(packages)ImplementationUtil $(start) $(end)" ;
	@echo "Translated outputs are in testOutputs/translationOutputs"
compare :
	@echo "Compiling and outputting C++ code for $(start) to $(end)" ;
	@num=$(start) ; while [ $$num -le $(end) ] ; do \
		formatNum=`printf "%03d" $$num` ; \
		cppOutputPath=`expr ./testOutputs/translationOutputs/test$$formatNum` ; \
		test -d $$cppOutputPath/bin || mkdir $$cppOutputPath/bin ; \
		test -d $$cppOutputPath/output || mkdir $$cppOutputPath/output ; \
		sudo g++ $$cppOutputPath/*.cpp -o $$cppOutputPath/bin/a.out ; \
		sudo $$cppOutputPath/bin/a.out > $$cppOutputPath/output/cpp_output.txt ; \
		num=`expr $$num + 1` ; \
	done ;
	@echo "C++ code outputted to cpp_output.txt" ;
	@echo "Compiling and outputting Java code for $(start) to $(end)" ;
	@num=$(start) ; while [ $$num -le $(end) ] ; do \
		formatNum=`printf "%03d" $$num` ; \
		javaInputPath=`expr ./src/test/java/inputs/test$$formatNum` ; \
		cppOutputPath=`expr ./testOutputs/translationOutputs/test$$formatNum` ; \
		test -d $$cppOutputPath/bin || mkdir $$cppOutputPath/bin ; \
		test -d $$cppOutputPath/output || mkdir $$cppOutputPath/output ; \
		sudo javac -d $$cppOutputPath/bin $$javaInputPath/Test$$formatNum.java ; \
		java -classpath $$cppOutputPath/bin inputs/test$$formatNum/Test$$formatNum > $$cppOutputPath/output/java_output.txt ; \
		num=`expr $$num + 1` ; \
	done ;
	@echo "Java code outputted to java_output.txt" ;
	@echo "Checking outputs" ;
	@sbt "run-main $(packages)StdOutputChecking" ;
	@echo "Output comparisons are in testOutputs/input_tests.txt"
5tran : output compare
