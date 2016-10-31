
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test002;

int main(void)
{

	A a = new __A();

	Object o = (Object) a;

	cout << o->__vptr->toString(o)->data  << endl;

	return 0;
}

//------------------


