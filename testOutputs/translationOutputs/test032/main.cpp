
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test032;

int main(void)
{

	A a = new __A();

	unsigned char b = 1;

	Class k0 = a->__vptr->getClass(a);
	checkClass(k0, b);

		a->__vptr->methodM_(a, (A) b);
		a->__vptr->methodM_(a, a);
		a->__vptr->methodM_(a, a);
		a->__vptr->methodM_(a, a);
		a->__vptr->methodM_(a, new A());
		a->__vptr->methodM_(a, new Object());
	return 0;
}

//------------------


