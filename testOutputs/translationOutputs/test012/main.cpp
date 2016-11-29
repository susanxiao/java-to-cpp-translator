
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test012;

int main (int argc, char ** argv) 
{

	//get command line arguments. convert between args(java) and argv(c++). for test22, 23...
	__rt::Array<java::lang::String> args(argc-1);
	for(int a=1; a<argc;a++){
		java::lang::String argument = new __String(argv[a]);
		args.__data[a-1] = argument;
	}

	A a = new __A();

	a->__vptr->methodSetA(a, new __String("A"));

	B1 b1 = new __B1();

	b1->__vptr->methodSetA(b1, new __String("B1"));

	B2 b2 = new __B2();

	b2->__vptr->methodSetA(b2, new __String("B2"));

	C c = new __C();

	c->__vptr->methodSetA(c, new __String("C"));

	a->__vptr->methodPrintOther(a, a);

	a->__vptr->methodPrintOther(a, (A) b1);

	a->__vptr->methodPrintOther(a, (A) b2);

	a->__vptr->methodPrintOther(a, (A) c);

	return 0;
}

//------------------

