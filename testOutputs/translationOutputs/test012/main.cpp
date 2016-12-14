
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test012;

int main(void)
{

	A a = new __A();

		a->__vptr->methodSetA(a, new __String("A"));
	B1 b1 = new __B1();

		b1->__vptr->methodSetA(b1, new __String("B1"));
	B2 b2 = new __B2();

		b2->__vptr->methodSetA(b2, new __String("B2"));
	C c = new __C();

		c->__vptr->methodSetA(c, new __String("C"));
		a->__vptr->methodPrintOther(a, a);
			Class k0 = a->__vptr->getClass(a);
	checkClass(k0, b1);

a->__vptr->methodPrintOther(a, (A) b1);
			Class k1 = a->__vptr->getClass(a);
	checkClass(k1, b2);

a->__vptr->methodPrintOther(a, (A) b2);
			Class k2 = a->__vptr->getClass(a);
	checkClass(k2, c);

a->__vptr->methodPrintOther(a, (A) c);
	return 0;
}

//------------------

