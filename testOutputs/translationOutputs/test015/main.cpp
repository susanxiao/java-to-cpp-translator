
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test015;

int main(void)
{

	A a = new __A();

	B other = new __B();

		other->parent.some = a;
			Class k0 = a->__vptr->getClass(a);
	checkClass(k0, other);

a->__vptr->methodPrintOther(a, (A) other);
	return 0;
}

//------------------

