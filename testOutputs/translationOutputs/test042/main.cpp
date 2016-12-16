
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test042;

int main(void)
{

	A a = new __A();

		a->__vptr->methodMA(a, a)->__vptr->methodM(new __A());
	B b = new __B();

		b->__vptr->methodMB(b, b)->__vptr->methodM(new __B());
		b->__vptr->methodMA(b, (A) b)->__vptr->methodM(new __A());
	return 0;
}

//------------------

