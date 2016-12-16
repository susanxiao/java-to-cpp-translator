
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test046;

int main(void)
{

	B b = new __B();

	A a = new __A();

		b->__vptr->methodMA(b, a->__vptr->methodMA(a, a->__vptr->methodMA(a, (A) b)))->__vptr->methodM(new __A());
	return 0;
}

//------------------

