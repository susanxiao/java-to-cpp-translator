
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test006;

int main(void)
{

	A a = new __A();

	a->__vptr->almostSetFld("B");

	cout << a->__vptr->getFld(a)->data  << endl;

	a->__vptr->setFld("B");

	cout << a->__vptr->getFld(a)->data  << endl;

	return 0;
}

//------------------


