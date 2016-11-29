
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test005;

int main (int argc, char ** argv) 
{

	//get command line arguments. convert between args(java) and argv(c++). for test22, 23...
	__rt::Array<java::lang::String> args(argc-1);
	for(int a=1; a<argc;a++){
		java::lang::String argument = new __String(argv[a]);
		args.__data[a-1] = argument;
	}

	B b = new __B();

	A a1 = new __A();

	A a2 = (A) b;

	cout << a1->__vptr->toString(a1) << endl;

	cout << a2->__vptr->toString(a2) << endl;

	return 0;
}

//------------------

