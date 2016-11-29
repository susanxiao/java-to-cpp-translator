
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test006;

int main (int argc, char ** args) 
{

int main(void)
{

	A a = new __A();

	a->__vptr->methodAlmostSetFld(a, new __String("B"));

	cout << a->__vptr->methodGetFld(a) << endl;

	a->__vptr->methodSetFld(a, new __String("B"));

	cout << a->__vptr->methodGetFld(a) << endl;

	return 0;
}

//------------------

