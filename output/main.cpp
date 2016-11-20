
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test017;

int main(void)
{

	A a = new __A(5);
	cout << a->__vptr->self(a)->__vptr->toString(a->__vptr->self(a))->data << endl;

	return 0;
}

//------------------

