#5Tran
----------

- Support unit testing (sbt test-only)
    - AstTraversalTest 0-17
    - CppTest 0-17
    - HeaderAstTest 0-17
    - HeaderTest 0-17

- Support printing the output.h, output.cpp, and main.cpp files individualy
    - run-main edu.nyu.oop.PrintHeaderFile [number]
    - run-main edu.nyu.oop.PrintCppFile [number]
    - run-main edu.nyu.oop.PrintMainFile [number]

- Support printing the output.h, output.cpp, and main.cpp together (Implementation)
    - run-main edu.nyu.oop.ImplementationUtil [number]

- Support inputs
    - Phase 1 0-20
    - Phase 2 0-20
    - Phase 3 0-20
    - Phase 4 0-15 and 17
    - Phase 5 0-15 and 17

- Support testing inputs
    - run ImplementationUtil over the entire range of inputs. This gets the updated .h .cpp files.
    - bash bashScript.sh; (from shell)
        - compiles all the C++ generated code
        - Redirects outputs to files   
    - run-main edu.nyu.oop.StdOutputChecking (from SBT)
        - outputs result of the translation on the inputs
        
        
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
