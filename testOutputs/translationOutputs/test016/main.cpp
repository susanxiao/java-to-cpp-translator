
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test016;

int main(void)
{

	A a = new __A();

	B other = new __B();

	Class k0 = other->__vptr->getClass(other);
	checkClass(k0, a);

		other->some = (B) a;
	Class k1 = a->__vptr->getClass(a);
	checkClass(k1, other);

		a->__vptr->methodPrintOther(a, (A) other);
	return 0;
}

//------------------

