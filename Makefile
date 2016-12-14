# VARIABLES
start = 0
end = 50
packages=edu.nyu.oop
testArgs = "1 2 3 4"

# TARGETS
help :
	@echo "----5-Tran Java to C++ Translator----"
	@echo "packages = $(packages)"
	@echo "testArgs = $(testArgs)"
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
	@echo "make copy --------"
	@echo "     javalang ---- To copy java_lang.cpp and java_lang.h into translationOutputs"
	@echo "make clean ------- To remove past outputs from translationOutputs"
	@echo "make compare ----- To compare already translated outputs"
	@echo "make 5tran -------"
	@echo "     doAll ------- To fully translate and compare outputs"
ast astCreate :
	@echo "Creating AST for $(start) to $(end)" ;
	@sbt --error 'set showSuccess := false' "run-main $(packages).AstTraversal $(start) $(end)" ;
	@echo "Java AST outputs are in testOutputs/astOutputs"
mutate astMutate :
	@echo "Mutating AST for $(start) to $(end)" ;
	@sbt --error 'set showSuccess := false' "run-main $(packages).AstMutator $(start) $(end)" ;
	@echo "Mutated ASTs are in testOutputs/mutatedAstOutputs"
header printHeader :
	@echo "Printing output.h file for $(start) to $(end)" ;
	@sbt --error 'set showSuccess := false' "run-main $(packages).PrintHeaderFile $(start) $(end)" ;
	@echo "Translated outputs are in testOutputs/translationOutputs"
cpp printCpp :
	@echo "Printing output.cpp file for $(start) to $(end)" ;
	@sbt --error 'set showSuccess := false' "run-main $(packages).PrintCppFile $(start) $(end)" ;
	@echo "Translated outputs are in testOutputs/translationOutputs"
main printMain :
	@echo "Printing main.cpp file for $(start) to $(end)" ;
	@sbt --error 'set showSuccess := false' "run-main $(packages).PrintMainFile $(start) $(end)" ;
	@echo "main.cpp outputs are in testOutputs/translationOutputs"
translate printAll:
	@echo "Translating inputs $(start) to $(end)" ;
	@sbt --error 'set showSuccess := false' "run-main $(packages).ImplementationUtil $(start) $(end)" ;
	@echo "Translated outputs are in testOutputs/translationOutputs"
clean:
	@echo "Removing past outputs for $(start) to $(end)" ;
	@num=$(start) ; while [ $$num -le $(end) ] ; do \
		formatNum=`printf "%03d" $$num` ; \
		cppOutputPath=`expr ./testOutputs/translationOutputs/test$$formatNum` ; \
		test -d $$cppOutputPath/output || mkdir $$cppOutputPath/output ; \
		rm $$cppOutputPath/output/*  ; \
		num=`expr $$num + 1` ; \
	done ;
	@echo "Removed past outputs from testOutputs/translationOutputs" ;
copy javalang :
	@echo "Copying java_lang.cpp and java_lang.h" ;
	@num=$(start) ; while [ $$num -le $(end) ] ; do \
		formatNum=`printf "%03d" $$num` ; \
		cppOutputPath=`expr ./testOutputs/translationOutputs/test$$formatNum` ; \
		cp ./output/* $$cppOutputPath ; \
		num=`expr $$num + 1` ; \
	done ;
	@echo "Copied java_lang files into translationOutputs directories"
compile :
	@echo "Compiling and outputting Java code for $(start) to $(end)" ;
	@num=$(start) ; while [ $$num -le $(end) ] ; do \
		formatNum=`printf "%03d" $$num` ; \
		javaInputPath=`expr ./src/test/java/inputs/test$$formatNum` ; \
		cppOutputPath=`expr ./testOutputs/translationOutputs/test$$formatNum` ; \
		test -d $$cppOutputPath/bin || mkdir $$cppOutputPath/bin ; \
		test -d $$cppOutputPath/output || mkdir $$cppOutputPath/output ; \
		sudo javac -d $$cppOutputPath/bin $$javaInputPath/Test$$formatNum.java ; \
		sudo chown -R $$USER: $$cppOutputPath/bin/inputs ; \
		java -classpath $$cppOutputPath/bin inputs/test$$formatNum/Test$$formatNum $(testArgs)> $$cppOutputPath/output/java_output.txt 2>&1 ; \
		num=`expr $$num + 1` ; \
	done ;
	@echo "Java code outputted to java_output.txt" ;
	@echo "Compiling and outputting C++ code for $(start) to $(end)" ;
	@num=$(start) ; while [ $$num -le $(end) ] ; do \
		formatNum=`printf "%03d" $$num` ; \
		cppOutputPath=`expr ./testOutputs/translationOutputs/test$$formatNum` ; \
		test -d $$cppOutputPath/bin || mkdir $$cppOutputPath/bin ; \
		test -d $$cppOutputPath/output || mkdir $$cppOutputPath/output ; \
		sudo g++ $$cppOutputPath/*.cpp -o $$cppOutputPath/bin/a.out ; \
		sudo $$cppOutputPath/bin/a.out $(testArgs) > $$cppOutputPath/output/cpp_output.txt 2>&1 ; \
		num=`expr $$num + 1` ; \
	done ;
	@echo "C++ code outputted to cpp_output.txt" ;
compare :
	@echo "Checking outputs" ;
	@sbt --error 'set showSuccess := false' "run-main $(packages).StdOutputChecking $(start) $(end)" ;
	@echo "Output comparisons are in testOutputs/input_tests.txt"
5tran doAll : clean translate copy compile compare
