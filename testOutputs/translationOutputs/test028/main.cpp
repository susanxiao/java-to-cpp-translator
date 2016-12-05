
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test028;

int main(void)
{

	__rt::Array<A>* as = new __rt::Array<A>(10);

		cout << as->__vptr->getClass(as)->__vptr->toString(as->__vptr->getClass(as)) << endl;
	return 0;
}

//------------------

