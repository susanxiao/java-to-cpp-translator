
//------------------

#include <iostream>
#include <sstream>
#include "java_lang.h"

#include "output.h"

using namespace java::lang;
using namespace std;
using namespace inputs::test014;

int main(void)
{

	A a = new __A();

	A other = a->some;

	a->__vptr->printOther(a, other);

	return 0;
}

//------------------
