#5Tran
----------

- Supports unit testing (sbt test-only)

    - AstTraversalTest 0-30
    - CppTest 0-30
    - HeaderAstTest 0-30
    - HeaderTest 0-30

- Supports printing the output.h, output.cpp, and main.cpp files individually

    For a single input
    (with sbt)
    - run-main edu.nyu.oop.PrintHeaderFile [number]
    - run-main edu.nyu.oop.PrintCppFile [number]
    - run-main edu.nyu.oop.PrintMainFile [number]

    (with make)
    - make header start=[number] end=[number]
    - make cpp start=[number] end=[number]
    - make main start=[number] end=[number]

    For a range
    (with sbt)
    - run-main edu.nyu.oop.PrintHeaderFile [start] [end]
    - run-main edu.nyu.oop.PrintCppFile [start] [end]
    - run-main edu.nyu.oop.PrintMainFile [start] [end]

    (with make)
    - make header start=[start] end=[end]
    - make cpp start=[start] end=[end]
    - make main start=[start] end=[end]

    For all inputs
    (with sbt)
    - run-main edu.nyu.oop.PrintHeaderFile
    - run-main edu.nyu.oop.PrintCppFile
    - run-main edu.nyu.oop.PrintMainFile

    (with  make)
    - make header
    - make cpp
    - make main

- Supports printing the output.h, output.cpp, and main.cpp together (Full Translation)

    (with sbt)
    - run-main edu.nyu.oop.ImplementationUtil [number]
    - run-main edu.nyu.oop.ImplementationUtil [start] [end]
    - run-main edu.nyu.oop.ImplementationUtil

    (with make)
    - make translate start=[number] end=[number]
    - make translate start=[start] end=[end]
    - make translate

- Supports Comparison of Java and C++ outputs

    With Makefile
    (to just compare)
    - make compare start=[number] end=[number]
    - make compare start=[start] end=[end]
    - make compare

    (to translate and compare)
    - make 5tran start=[number] end=[number]
    - make 5tran start=[start] end=[end]
    - make 5tran

    Without Makefile (no variability of the range)
    - 5tran.sh

- Supports inputs

    - Phase 1 0-50
    - Phase 2 0-50
    - Phase 3 0-50
    - Phase 4 0-50
    - Phase 5 0-50, except 49 

Run "make help" for other Makefile commands

Translator
----------

This README should document its features and any details a user might need to know about your translator.

Use Github Markdown https://help.github.com/articles/github-flavored-markdown/

The Speaker is responsible for it being up to date and accurate.

Don't forget to update xtc.properties with your team and project name.

Example command to execute translator from Sbt

```runxtc -printJavaAst src/test/java/inputs/Test000/Test000.java ```


```
Project Map
-----------

├── README.md
│
├── build.sbt (managed library dependencies and c++ compilation configuration)
│
├── .sbtrc (like bash aliases but for sbt)
│
├── .gitignore (prevent certain files from being commmited to the git repo)
│
├── lib (unmanaged library dependencies, like xtc and its source) 
│
├── logs (logger output)
│   └── xtc.log 
│
├── output (supporting java_lang library)
│   ├── java_lang.cpp
│   ├── java_lang.h
│   ├── ptr.h
│
├── project (sbt configuration, shouldn't need to be touched)
│
├── schema (ast schema & examples)
│   ├── cpp.ast
│   └── inheritance.ast
│
├── src 
|   ├── main
|   │   ├── java
|   │   │   └── edu (translator source code)
|   │   └── resources
|   │       └── xtc.properties (translator properties file)
|   └── test
|       └── java
|           ├── edu (translator unit tests)
|           └── inputs (translator test inputs)
|
└── testOutputs
    ├── astOutputs (AST traversal output)
    |   ├── Test000.txt
    |   |   ...
    |   └── Test050.txt
    |
    ├── mutatedAstOutputs (mutated AST output)
    |   ├── test000.txt
    |   |   ...
    |   └── test050.txt
    |
    ├── translationOutputs (actual translated outputs)
    |   ├── test000
    |   |   ├── bin
    |   |   |   ├── inputs/test000
    |   |   |   └── a.out
    |   |   |
    |   |   ├── output (translated codes' outputs)
    |   |   |   ├── cpp_output.txt
    |   |   |   └── java_output.txt
    |   |   |
    |   |   ├── java_lang.cpp
    |   |   ├── java_lang.h
    |   |   ├── main.cpp
    |   |   ├── output.cpp
    |   |   ├── output.h
    |   |   └── ptr.h
    |   |   ...
    |   └── test050
    |
    └── input_tests.txt
```
