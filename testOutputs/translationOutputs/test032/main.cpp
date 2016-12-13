
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

	uint8_t b = 1;

		a->__vptr->methodMInt(a, (int32_t) b);
		a->__vptr->methodMA(a, a);
		a->__vptr->methodMDouble(a, 1.0);
		a->__vptr->methodMObject(a, (Object) a);
		a->__vptr->methodMAObject(a, new __A(), (Object) a);
		a->__vptr->methodMObjectObject(a, new __Object(), (Object) a);
	return 0;
}

//------------------
