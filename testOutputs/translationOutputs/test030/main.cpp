
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test030;

int main(void)
{

	__rt::Array<A>* as = new __rt::Array<A>(5);

		cout << as->__vptr->getClass(as)->__vptr->toString(as->__vptr->getClass(as)) << endl;
	return 0;
}

//------------------

