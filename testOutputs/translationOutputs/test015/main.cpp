
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test015;

int main (int argc, char ** args) 
{

	A a = new __A();

	B other = new __B();

	other->parent.some = a;

	a->__vptr->methodPrintOther(a, (A) other);

	return 0;
}

//------------------

