
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test034;

int main(void)
{

	A a = new __A();

	uint8_t b = 1;

	double d;

		a->__vptr->methodMByte(a, b);
		a->__vptr->methodMInt(a, b + b);
		a->__vptr->methodMDouble(a, d);
	return 0;
}

//------------------

