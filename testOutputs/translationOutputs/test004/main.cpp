
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test004;

int main (int argc, char ** args) 
{

	A a = new __A(new __String("A"));
	cout << a->__vptr->methodGetFld(a) << endl;

	return 0;
}

//------------------

