
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test039;

int main(void)
{

	B b = new __B();

		b->__vptr->methodMAObject(b, new __A(), (Object) b);
		b->__vptr->methodMBObject(b, b, new __Object());
		b->__vptr->methodMObjectB(b, (Object) b, b);
	return 0;
}

//------------------

