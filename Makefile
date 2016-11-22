# VARIABLES
start = 0
end = 20
packages=edu.nyu.oop

# TARGETS
help :
	@echo "----5-Tran Java to C++ Translator----"
	@echo "packages = $(packages)"
	@echo "-------------------------------------"
	@echo "To modify the input range, input"
	@echo "     start=[startNumber] and/or"
	@echo "     end=[endNumber]"
	@echo "at the end of your target."
	@echo "For example:"
	@echo "     make 5tran start=0 end=10"
	@echo "will fully translate inputs 0->10"
	@echo "start = $(start)"
	@echo "end = $(end)"
	@echo "-------------------------------------"
	@echo "TARGETS:"
	@echo "make ast ---------"
	@echo "     astCreate --- To generate Java ASTs"
	@echo "make mutate ------"
	@echo "     astMutate --- To generate Mutated C++ ASTs"
	@echo "make header ------"
	@echo "     printHeader - To print output.h files"
	@echo "make cpp ---------"
	@echo "     printCpp ---- To print output.cpp files"
	@echo "make main --------"
	@echo "     printMaine -- To print main.cpp files"
	@echo "make translate ---"
	@echo "     printAll ---- To print the full translation"
	@echo "make compare ----- To compare already translated outputs"
	@echo "make 5tran -------"
	@echo "     doAll ------- To fully translate and compare outputs"
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
	@sbt "run-main $(packages).PrintHeaderFile $(start) $(end)" ;
	@echo "Translated outputs are in testOutputs/printHeaderOutputs"
cpp printCpp :
	@echo "Printing output.cpp file for $(start) to $(end)" ;
	@sbt "run-main $(packages).PrintCppFile $(start) $(end)" ;
	@echo "Translated outputs are in testOutputs/printCppOutputs"
main printMain :
	@echo "Printing main.cpp file for $(start) to $(end)" ;
	@sbt "run-main $(packages).PrintMainFile $(start) $(end)" ;
	@echo "main.cpp outputs are in testOutputs/mainFileOutputs"
translate printAll:
	@echo "Translating inputs $(start) to $(end)" ;
	@sbt "run-main $(packages).ImplementationUtil $(start) $(end)" ;
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
	@sbt "run-main $(packages).StdOutputChecking" ;
	@echo "Output comparisons are in testOutputs/input_tests.txt"
5tran doAll : translate compare