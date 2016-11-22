#5Tran
----------

- Supports unit testing (sbt test-only)

    - AstTraversalTest 0-17
    - CppTest 0-17
    - HeaderAstTest 0-17
    - HeaderTest 0-17

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

    Without Makefile
    - run full translation over the entire range of inputs. This gets the updated .h .cpp files.
    - bash bashScript.sh; (from shell)
        - compiles all the C++ generated code
        - Redirects outputs to files   
    - run-main edu.nyu.oop.StdOutputChecking (from SBT)
        - outputs result of the translation on the inputs

- Supports inputs

    - Phase 1 0-20
    - Phase 2 0-20
    - Phase 3 0-20
    - Phase 4 0-15 and 17
    - Phase 5 0-15 and 17  
        
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
├── output (target c++ source & supporting java_lang library)
│   ├── java_lang.cpp
│   ├── java_lang.h
│   ├── main.cpp
│   ├── output.cpp
│   └── output.h
│
├── project (sbt configuration, shouldn't need to be touched)
│
├── schema (ast schema & examples)
│   ├── cpp.ast
│   └── inheritance.ast
│
└── src 
    ├── main
    │   ├── java
    │   │   └── edu (translator source code)
    │   └── resources
    │       └── xtc.properties (translator properties file)
    └── test
        └── java
            ├── edu (translator unit tests)
            └── inputs (translator test inputs)
```
