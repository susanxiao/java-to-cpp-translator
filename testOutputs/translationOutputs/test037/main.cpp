
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test037;

int main(void)
{

	B b = new __B();

		b->__vptr->methodMObjectObject(b, (Object) new __A(), (Object) b);
		b->__vptr->methodMAObject(b, (A) b, new __Object());
		b->__vptr->methodMObjectA(b, (Object) b, (A) b);
	return 0;
}

//------------------

