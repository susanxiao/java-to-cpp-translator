
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test009;

int main(void)
{

	A a = new __A();

	cout << a->self->__vptr->toString(a) << endl;
	return 0;
}

//------------------

